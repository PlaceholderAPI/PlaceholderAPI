package me.clip.placeholderapi.commands.impl.local;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.commands.PlaceholderCommand;
import me.clip.placeholderapi.libs.JSONMessage;
import me.clip.placeholderapi.util.Msg;
import me.clip.placeholderapi.util.PasteUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public class CommandReport extends PlaceholderCommand {
  
  public CommandReport() {
    super("report");
  }
  
  @Override
  public void evaluate(@NotNull final PlaceholderAPIPlugin plugin,
      @NotNull final CommandSender sender, @NotNull final String alias,
      @NotNull @Unmodifiable final List<String> params) {
    Msg.msg(sender,
        "&7Generating Report. Please wait...");
    
    Map<String, String> urls = new HashMap<>();
    CompletableFuture<String> dump = PasteUtil.postDump(plugin)
        .whenComplete((url, ex) -> { 
          if (ex != null) { 
            urls.put("dump", null);
            return; 
          }
          
          urls.put("dump", url); 
        });
    CompletableFuture<String> log = PasteUtil.postLogs(plugin)
        .whenComplete((url, ex) -> {
          if (ex != null) {
            urls.put("log", null);
            return;
          }
          
          urls.put("log", url);
        });

    CompletableFuture.allOf(dump, log).whenComplete((v, ex) -> {
      if (ex != null) {
        plugin.getLogger().log(Level.WARNING, "Failed to generate Report", ex);
        
        Msg.msg(sender,
            "&cFailed to generate Report, check console for details.");
        return;
      }
      
      String dumpUrl = urls.get("dump");
      String logUrl = urls.get("log");
      
      String url = getUrl(dumpUrl, logUrl);
      
      if (url == null) {
        if (dumpUrl == null) {
          dumpUrl = "&c&oNot available";
        }
        if (logUrl == null) {
          logUrl = "&c&oNot available";
        }
        
        Msg.msg(sender,
            "&cFailed to generate GitHub URL. You need to report manually.",
            "&cAvailable pastes:",
            "&7Dump: " + dumpUrl,
            "&7Logs: " + logUrl);
        return;
      }
      
      if (sender instanceof Player) {
        JSONMessage msg = JSONMessage
            .create("GitHub-URL created successfully! Click ")
            .color(ChatColor.GREEN)
            .then("here")
            .color(ChatColor.WHITE)
            .style(ChatColor.UNDERLINE)
            .openURL(url)
            .tooltip(
                JSONMessage.create("Click to open a Issue on GitHub.")
                    .color(ChatColor.GRAY)
            )
            .then(" to open a Issue on GitHub!")
            .color(ChatColor.GREEN);
        
        msg.send((Player) sender);
        return;
      }
      
      // Msg.msg can't be used as it would translate & query-parameters into colors
      sender.sendMessage("§aGitHub-URL created successfully! URL: §f" + url);
    });
  }
  
  private String getUrl (String dumpUrl, String logUrl) {
    if (dumpUrl == null) {
      dumpUrl = "https://paste.helpch.at/";
    }
    if (logUrl == null) {
      logUrl =  "https://paste.helpch.at/";
    }
    
    
    try {
      return "https://github.com/PlaceholderAPI/PlaceholderAPI/issues/new"
          + "?labels=Type%3A+Issue+%28Unconfirmed%29"
          + "&template=bug_report.yml"
          + "&dump=" + URLEncoder.encode(dumpUrl, "UTF-8")
          + "&error=" + URLEncoder.encode(logUrl, "UTF-8");
    } catch (UnsupportedEncodingException ex) {
      return null;
    }
  }
}
