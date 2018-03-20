package me.clip.placeholderapi.expansion.cloud;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.util.Msg;

public class ExpansionCloudManager {
	
	private final File dir;
	
	private PlaceholderAPIPlugin plugin;
	
	private final TreeMap<Integer, CloudExpansion> remote = new TreeMap<>();
	
	private final List<String> downloading = new ArrayList<>();
	
	private int toUpdate = 0;
    
	public ExpansionCloudManager(PlaceholderAPIPlugin instance) {
		
		plugin = instance;
		dir = new File(instance.getDataFolder() + File.separator + "expansions");

		if (!dir.exists()) {
			try {
				dir.mkdirs();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void clean() {
		remote.clear();
		toUpdate = 0;
		downloading.clear();
	}
	
	public boolean isDownloading(String expansion) {
		return downloading.contains(expansion);
	}
	
	public Map<Integer, CloudExpansion> getCloudExpansions() {
		return remote;
	}
	
	public CloudExpansion getCloudExpansion(String name) {
		
		for (CloudExpansion ex : remote.values()) {
			if (ex.getName().equalsIgnoreCase(name)) {
				return ex;
			}
		}
		return null;
	}
	
	public int getCloudAuthorCount() {
		
		if (remote == null) {
			return 0;
		}
		
		List<String> temp = new ArrayList<>();
		
		for (CloudExpansion ex : remote.values()) {
			if (!temp.contains(ex.getAuthor())) {
				temp.add(ex.getAuthor());
			}
		}
		return temp.size();
	}
	
	public int getToUpdateCount() {
		return toUpdate;
	}
	
	public void decrementToUpdateCount() {
		if (toUpdate > 0) {
			toUpdate--;
		}
	}
	
	public Map<Integer, CloudExpansion> getAllByAuthor(String author) {
		
		if (remote.isEmpty()) {
			return null;
		}
		
		TreeMap<Integer, CloudExpansion> byAuthor = new TreeMap<>();
		boolean first = true;
		
		for (CloudExpansion ex : remote.values()) {
			if (ex.getAuthor().equalsIgnoreCase(author)) {
				if (first) {
					first = false;
					byAuthor.put(0, ex);
				} else {
					byAuthor.put(byAuthor.lastKey()+1, ex);
				}
			}
		}
		
		if (byAuthor.isEmpty()) {
			return null;
		}
		return byAuthor;
	}
	
	public Map<Integer, CloudExpansion> getAllInstalled() {
		
		if (remote.isEmpty()) {
			return null;
		}
		
		TreeMap<Integer, CloudExpansion> has = new TreeMap<>();
		
		boolean first = true;
		
		for (CloudExpansion ex : remote.values()) {
			if (ex.hasExpansion()) {
				if (first) {
					first = false;
					has.put(0, ex);
				} else {
					has.put(has.lastKey()+1, ex);
				}
			}
		}
		
		if (has.isEmpty()) {
			return null;
		}
		return has;
	}
	
	public int getPagesAvailable(Map<Integer, CloudExpansion> map, int amount) {
		
		if (map == null) { 
			return 0;
		}
		
		int pages = map.size() > 0 ? 1 : 0;
		
		if (pages == 0) {
			return pages;
		}
		
		if (map.size() > amount) {
			
			pages = map.size()/amount;
			
			if (map.size() % amount > 0) {
				pages = pages+1;
			}
		}
		return pages;
	}
	
	public Map<Integer, CloudExpansion> getPage(Map<Integer, CloudExpansion> map, int page) {
		
		if (map == null || map.size() == 0) {
			return null;
		}
		
		if (page > getPagesAvailable(map, 10)) {
			return null;
		}
		
		int end = 10*page;
		
		int start = end-10;
		
		end = end-1;
		
		int size = map.size();
		
		if (end > size) {
			end = size-1;
		}
		
		TreeMap<Integer, CloudExpansion> ex = new TreeMap<>();
		
		for (int i = start ; i <= end ; i++) {
			ex.put(i, map.get(i));
		}
		return ex.isEmpty() ? null : ex;
	}
	
	public void fetch() {
		
		plugin.getLogger().info("Fetching available expansion list...");
		
		toUpdate = 0;
		
		new BukkitRunnable() {
			
				@Override
				public void run() {

					StringBuilder sb;

					try {

						URL api = new URL("http://api.extendedclip.com/");
						
						HttpURLConnection connection = (HttpURLConnection) api.openConnection();
						
						connection.setRequestMethod("GET");
						
						connection.connect();
						
						BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						
						sb = new StringBuilder();
						
						String line;

						while ((line = br.readLine()) != null) {
							sb.append(line);
						}

						br.close();
						connection.disconnect();

					} catch (Exception e) {
						return;
					}

					String json = sb.toString();
					JSONParser parser = new JSONParser();
					Object obj = null;

					try {
                        obj = parser.parse(json);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

					if (obj == null) {
                        return;
                    }

					List<CloudExpansion> unsorted = new ArrayList<>();

					if (obj instanceof JSONObject) {

                        JSONObject jo = (JSONObject) obj;

                        for (Object o : jo.keySet()) {

                            JSONObject sub = (JSONObject) jo.get(o);
                            String name = o.toString();
                            String author = (String) sub.get("author");
                            String version = (String) sub.get("version");
                            String link = (String) sub.get("link");
                            String description = (String) sub.get("description");
                            String notes = "";
                            long update = -1;

                            if (sub.get("release_notes") != null) {
                                notes = (String) sub.get("release_notes");
                            }

                            if (sub.get("last_update") != null) {

                                Object u = sub.get("last_update");

                                if (u instanceof Long) {
                                    update = (long) sub.get("last_update");
                                }
                            }

                            CloudExpansion ce = new CloudExpansion(name, author, version, description, link);

                            ce.setReleaseNotes(notes);

                            ce.setLastUpdate(update);

                            PlaceholderExpansion ex = plugin.getExpansionManager().getRegisteredExpansion(name);

                            if (ex != null && ex.isRegistered()) {
                                ce.setHasExpansion(true);
                                if (!ex.getVersion().equals(version)) {
                                    ce.setShouldUpdate(true);
                                    toUpdate++;
                                }
                            }

                            unsorted.add(ce);
                        }

                        int count = 0;

                        unsorted.sort(Comparator.comparing(CloudExpansion::getLastUpdate).reversed());

                        for (CloudExpansion e : unsorted) {
                            remote.put(count, e);
                            count++;
                        }

                        plugin.getLogger().info(count + " placeholder expansions are available on the cloud.");
                        plugin.getLogger().info(toUpdate + " expansions you use have updates.");
                    }
				}
		}.runTaskAsynchronously(plugin);
	}
	
	private void download(URL url, String name) throws IOException {
	    
		InputStream is = null;
		
	    FileOutputStream fos = null;

	    try {
	    	
	        URLConnection urlConn = url.openConnection();
	        
	        is = urlConn.getInputStream();  
	        
	        fos = new FileOutputStream(dir.getAbsolutePath() + File.separator + "Expansion-" + name + ".jar");

	        byte[] buffer = new byte[1024];
	        
	        int l;

	        while ((l = is.read(buffer)) > 0) {  
	            fos.write(buffer, 0, l);
	        }
	    } finally {
	        try {
	            if (is != null) {
	                is.close();
	            }
	        } finally {
	            if (fos != null) {
	                fos.close();
	            }
	        }
	    }
	}
	
	public void downloadExpansion(final String player, final CloudExpansion ex) {
		
		if (downloading.contains(ex.getName())) {
			return;
		}
		
		if (ex.getLink() == null) {
			return;
		}
		
		downloading.add(ex.getName());
		
		plugin.getLogger().info("Attempting download of expansion: " + ex.getName() + (player != null ? " by user: " + player : "") + " from url: " + ex.getLink());

		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            try {

                download(new URL(ex.getLink()), ex.getName());

                plugin.getLogger().info("Download of expansion: " + ex.getName() + " complete!");

            } catch (Exception e) {

                plugin.getLogger().warning("Failed to download expansion: " + ex.getName() + " from: " + ex.getLink());

                Bukkit.getScheduler().runTask(plugin, () -> {

                    downloading.remove(ex.getName());

                    if (player != null) {
                        Player p = Bukkit.getPlayer(player);

                        if (p != null) {
                            Msg.msg(p, "&cThere was a problem downloading expansion: &f" + ex.getName());
                        }
                    }
                });

                return;
            }

            Bukkit.getScheduler().runTask(plugin, () -> {

                downloading.remove(ex.getName());

                if (player != null) {

                    Player p = Bukkit.getPlayer(player);

                    if (p != null) {
                        Msg.msg(p, "&aExpansion &f" + ex.getName() + " &adownload complete!");
                    }
                }
            });

        });
	}
}
