package me.clip.placeholderapi.metrics;

import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.service.NKServiceManager;
import cn.nukkit.plugin.service.RegisteredServiceProvider;
import cn.nukkit.plugin.service.ServicePriority;
import cn.nukkit.utils.Config;
import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.zip.GZIPOutputStream;

public class Metrics {
    public static final int B_STATS_VERSION = 1;
    private static final String URL = "https://bStats.org/submitData/bukkit";
    private boolean enabled;
    private static boolean logFailedRequests;
    private static boolean logSentData;
    private static boolean logResponseStatusText;
    private static String serverUUID;
    private final Plugin plugin;
    private final List<Metrics.CustomChart> charts = new ArrayList();

    public Metrics(Plugin plugin) {
        Preconditions.checkNotNull(plugin);
        this.plugin = plugin;
        File bStatsFolder = new File(plugin.getDataFolder().getParentFile(), "bStats");
        File configFile = new File(bStatsFolder, "config.yml");
        Config config = new Config(configFile);
        LinkedHashMap<String, Object> map = (LinkedHashMap) config.getAll();
        if (!config.isString("serverUuid")) {
            map.put("serverUuid", UUID.randomUUID().toString());
        } else {
            try {
                UUID.fromString(config.getString("serverUuid"));
            } catch (Exception var10) {
                map.put("serverUuid", UUID.randomUUID().toString());
            }
        }

        if (!config.isBoolean("enabled")) {
            map.put("enabled", true);
        }

        if (!config.isBoolean("logFailedRequests")) {
            map.put("logFailedRequests", false);
        }

        if (!config.isBoolean("logSentData")) {
            map.put("logSentData", false);
        }

        if (!config.isBoolean("logResponseStatusText")) {
            map.put("logResponseStatusText", false);
        }

        config.setAll(map);
        config.save();
        this.enabled = config.getBoolean("enabled", true);
        serverUUID = config.getString("serverUuid");
        logFailedRequests = config.getBoolean("logFailedRequests", false);
        logSentData = config.getBoolean("logSentData", false);
        logResponseStatusText = config.getBoolean("logResponseStatusText", false);
        if (this.enabled) {
            boolean found = false;
            Iterator var7 = Server.getInstance().getServiceManager().getKnownService().iterator();

            while (var7.hasNext()) {
                Class service = (Class) var7.next();

                try {
                    service.getField("B_STATS_VERSION");
                    found = true;
                    break;
                } catch (NoSuchFieldException var11) {
                }
            }

            Server.getInstance().getServiceManager().register(Metrics.class, this, plugin, ServicePriority.NORMAL);
            if (!found) {
                this.startSubmitting();
            }
        }

    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void addCustomChart(Metrics.CustomChart chart) {
        Preconditions.checkNotNull(chart);
        this.charts.add(chart);
    }

    private void startSubmitting() {
        final Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (!Metrics.this.plugin.isEnabled()) {
                    timer.cancel();
                } else {
                    Server.getInstance().getScheduler().scheduleTask(Metrics.this.plugin, () -> {
                        Metrics.this.submitData();
                    });
                }
            }
        }, 300000L, 1800000L);
    }

    public JsonObject getPluginData() {
        JsonObject data = new JsonObject();
        String pluginName = this.plugin.getName();
        String pluginVersion = this.plugin.getDescription().getVersion();
        data.addProperty("pluginName", pluginName);
        data.addProperty("pluginVersion", pluginVersion);
        JsonArray customCharts = new JsonArray();
        this.charts.stream().map((customChart) -> {
            return customChart.getRequestJsonObject(this.plugin, logFailedRequests);
        }).filter((chart) -> {
            return chart != null;
        }).forEach((chart) -> {
            customCharts.add(chart);
        });
        data.add("customCharts", customCharts);
        return data;
    }

    private JsonObject getServerData() {
        int playerAmount = Server.getInstance().getOnlinePlayers().size();
        int onlineMode = Server.getInstance().getPropertyBoolean("xbox-auth", false) ? 1 : 0;
        String minecraftVersion = Server.getInstance().getVersion();
        String softwareName = Server.getInstance().getName();
        String javaVersion = System.getProperty("java.version");
        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");
        String osVersion = System.getProperty("os.version");
        int coreCount = Runtime.getRuntime().availableProcessors();
        JsonObject data = new JsonObject();
        data.addProperty("serverUUID", serverUUID);
        data.addProperty("playerAmount", playerAmount);
        data.addProperty("onlineMode", onlineMode);
        data.addProperty("bukkitVersion", minecraftVersion);
        data.addProperty("bukkitName", softwareName);
        data.addProperty("javaVersion", javaVersion);
        data.addProperty("osName", osName);
        data.addProperty("osArch", osArch);
        data.addProperty("osVersion", osVersion);
        data.addProperty("coreCount", coreCount);
        return data;
    }

    private void submitData() {
        JsonObject data = this.getServerData();
        JsonArray pluginData = new JsonArray();
        Server.getInstance().getServiceManager().getKnownService().forEach((service) -> {
            try {
                service.getField("B_STATS_VERSION");
                List providers = null;

                try {
                    Field field = Field.class.getDeclaredField("modifiers");
                    field.setAccessible(true);
                    Field handle = NKServiceManager.class.getDeclaredField("handle");
                    field.setInt(handle, handle.getModifiers() & -17);
                    handle.setAccessible(true);
                    providers = (List) ((Map) handle.get((NKServiceManager) ((NKServiceManager) Server.getInstance().getServiceManager()))).get(service);
                } catch (IllegalArgumentException | SecurityException | IllegalAccessException var8) {
                    if (logFailedRequests) {
                        this.plugin.getLogger().warning("Failed to link to metrics class " + service.getName(), var8);
                    }
                }

                if (providers != null) {
                    Iterator var10 = providers.iterator();

                    while (var10.hasNext()) {
                        RegisteredServiceProvider provider = (RegisteredServiceProvider) var10.next();

                        try {
                            Object plugin = provider.getService().getMethod("getPluginData").invoke(provider.getProvider());
                            if (plugin instanceof JsonObject) {
                                pluginData.add((JsonElement) plugin);
                            }
                        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException var7) {
                        }
                    }
                }
            } catch (NoSuchFieldException var9) {
            }

        });
        data.add("plugins", pluginData);
        (new Thread(() -> {
            try {
                sendData(this.plugin, data);
            } catch (Exception var3) {
                if (logFailedRequests) {
                    this.plugin.getLogger().warning("Could not submit plugin stats of " + this.plugin.getName(), var3);
                }
            }

        })).start();
    }

    private static void sendData(Plugin plugin, JsonObject data) throws Exception {
        Preconditions.checkNotNull(data);
        if (Server.getInstance().isPrimaryThread()) {
            throw new IllegalAccessException("This method must not be called from the main thread!");
        } else {
            if (logSentData) {
                plugin.getLogger().info("Sending data to bStats: " + data.toString());
            }

            HttpsURLConnection connection = (HttpsURLConnection) (new URL("https://bStats.org/submitData/bukkit")).openConnection();
            byte[] compressedData = compress(data.toString());
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Accept", "application/json");
            connection.addRequestProperty("Connection", "close");
            connection.addRequestProperty("Content-Encoding", "gzip");
            connection.addRequestProperty("Content-Length", String.valueOf(compressedData.length));
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "MC-Server/1");
            connection.setDoOutput(true);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            Throwable var5 = null;

            try {
                outputStream.write(compressedData);
                outputStream.flush();
            } catch (Throwable var28) {
                var5 = var28;
                throw var28;
            } finally {
                if (outputStream != null) {
                    if (var5 != null) {
                        try {
                            outputStream.close();
                        } catch (Throwable var26) {
                            var5.addSuppressed(var26);
                        }
                    } else {
                        outputStream.close();
                    }
                }

            }

            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            Throwable var7 = null;

            StringBuilder builder;
            try {
                builder = new StringBuilder();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                }
            } catch (Throwable var30) {
                var7 = var30;
                throw var30;
            } finally {
                if (bufferedReader != null) {
                    if (var7 != null) {
                        try {
                            bufferedReader.close();
                        } catch (Throwable var27) {
                            var7.addSuppressed(var27);
                        }
                    } else {
                        bufferedReader.close();
                    }
                }

            }

            if (logResponseStatusText) {
                plugin.getLogger().info("Sent data to bStats and received response: " + builder.toString());
            }

        }
    }

    private static byte[] compress(String str) throws IOException {
        if (str == null) {
            return null;
        } else {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(outputStream);
            Throwable var3 = null;

            try {
                gzip.write(str.getBytes(StandardCharsets.UTF_8));
            } catch (Throwable var12) {
                var3 = var12;
                throw var12;
            } finally {
                if (gzip != null) {
                    if (var3 != null) {
                        try {
                            gzip.close();
                        } catch (Throwable var11) {
                            var3.addSuppressed(var11);
                        }
                    } else {
                        gzip.close();
                    }
                }

            }

            return outputStream.toByteArray();
        }
    }

    static {
        if (System.getProperty("bstats.relocatecheck") == null || !System.getProperty("bstats.relocatecheck").equals("false")) {
            String defaultPackage = new String(new byte[]{111, 114, 103, 46, 98, 115, 116, 97, 116, 115, 46, 110, 117, 107, 107, 105, 116});
            String examplePackage = new String(new byte[]{121, 111, 117, 114, 46, 112, 97, 99, 107, 97, 103, 101});
            if (Metrics.class.getPackage().getName().equals(defaultPackage) || Metrics.class.getPackage().getName().equals(examplePackage)) {
                throw new IllegalStateException("bStats Metrics class has not been relocated correctly!");
            }
        }

    }

    public static class AdvancedBarChart extends Metrics.CustomChart {
        private final Callable<Map<String, int[]>> callable;

        public AdvancedBarChart(String chartId, Callable<Map<String, int[]>> callable) {
            super(chartId);
            this.callable = callable;
        }

        protected JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            JsonObject values = new JsonObject();
            Map<String, int[]> map = (Map) this.callable.call();
            if (map != null && !map.isEmpty()) {
                boolean allSkipped = true;
                Iterator var5 = map.entrySet().iterator();

                while (true) {
                    Entry entry;
                    do {
                        if (!var5.hasNext()) {
                            if (allSkipped) {
                                return null;
                            }

                            data.add("values", values);
                            return data;
                        }

                        entry = (Entry) var5.next();
                    } while (((int[]) entry.getValue()).length == 0);

                    allSkipped = false;
                    JsonArray categoryValues = new JsonArray();
                    int[] var8 = (int[]) entry.getValue();
                    int var9 = var8.length;

                    for (int var10 = 0; var10 < var9; ++var10) {
                        int categoryValue = var8[var10];
                        categoryValues.add(new JsonPrimitive(categoryValue));
                    }

                    values.add((String) entry.getKey(), categoryValues);
                }
            } else {
                return null;
            }
        }
    }

    public static class SimpleBarChart extends Metrics.CustomChart {
        private final Callable<Map<String, Integer>> callable;

        public SimpleBarChart(String chartId, Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }

        protected JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            JsonObject values = new JsonObject();
            Map<String, Integer> map = (Map) this.callable.call();
            if (map != null && !map.isEmpty()) {
                Iterator var4 = map.entrySet().iterator();

                while (var4.hasNext()) {
                    Entry<String, Integer> entry = (Entry) var4.next();
                    JsonArray categoryValues = new JsonArray();
                    categoryValues.add(new JsonPrimitive((Number) entry.getValue()));
                    values.add((String) entry.getKey(), categoryValues);
                }

                data.add("values", values);
                return data;
            } else {
                return null;
            }
        }
    }

    public static class MultiLineChart extends Metrics.CustomChart {
        private final Callable<Map<String, Integer>> callable;

        public MultiLineChart(String chartId, Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }

        protected JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            JsonObject values = new JsonObject();
            Map<String, Integer> map = (Map) this.callable.call();
            if (map != null && !map.isEmpty()) {
                boolean allSkipped = true;
                Iterator var5 = map.entrySet().iterator();

                while (var5.hasNext()) {
                    Entry<String, Integer> entry = (Entry) var5.next();
                    if ((Integer) entry.getValue() != 0) {
                        allSkipped = false;
                        values.addProperty((String) entry.getKey(), (Number) entry.getValue());
                    }
                }

                if (allSkipped) {
                    return null;
                } else {
                    data.add("values", values);
                    return data;
                }
            } else {
                return null;
            }
        }
    }

    public static class SingleLineChart extends Metrics.CustomChart {
        private final Callable<Integer> callable;

        public SingleLineChart(String chartId, Callable<Integer> callable) {
            super(chartId);
            this.callable = callable;
        }

        protected JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            int value = (Integer) this.callable.call();
            if (value == 0) {
                return null;
            } else {
                data.addProperty("value", value);
                return data;
            }
        }
    }

    public static class DrilldownPie extends Metrics.CustomChart {
        private final Callable<Map<String, Map<String, Integer>>> callable;

        public DrilldownPie(String chartId, Callable<Map<String, Map<String, Integer>>> callable) {
            super(chartId);
            this.callable = callable;
        }

        public JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            JsonObject values = new JsonObject();
            Map<String, Map<String, Integer>> map = (Map) this.callable.call();
            if (map != null && !map.isEmpty()) {
                boolean reallyAllSkipped = true;
                Iterator var5 = map.entrySet().iterator();

                while (var5.hasNext()) {
                    Entry<String, Map<String, Integer>> entryValues = (Entry) var5.next();
                    JsonObject value = new JsonObject();
                    boolean allSkipped = true;

                    for (Iterator var9 = ((Map) map.get(entryValues.getKey())).entrySet().iterator(); var9.hasNext(); allSkipped = false) {
                        Entry<String, Integer> valueEntry = (Entry) var9.next();
                        value.addProperty((String) valueEntry.getKey(), (Number) valueEntry.getValue());
                    }

                    if (!allSkipped) {
                        reallyAllSkipped = false;
                        values.add((String) entryValues.getKey(), value);
                    }
                }

                if (reallyAllSkipped) {
                    return null;
                } else {
                    data.add("values", values);
                    return data;
                }
            } else {
                return null;
            }
        }
    }

    public static class AdvancedPie extends Metrics.CustomChart {
        private final Callable<Map<String, Integer>> callable;

        public AdvancedPie(String chartId, Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }

        protected JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            JsonObject values = new JsonObject();
            Map<String, Integer> map = (Map) this.callable.call();
            if (map != null && !map.isEmpty()) {
                boolean allSkipped = true;
                Iterator var5 = map.entrySet().iterator();

                while (var5.hasNext()) {
                    Entry<String, Integer> entry = (Entry) var5.next();
                    if ((Integer) entry.getValue() != 0) {
                        allSkipped = false;
                        values.addProperty((String) entry.getKey(), (Number) entry.getValue());
                    }
                }

                if (allSkipped) {
                    return null;
                } else {
                    data.add("values", values);
                    return data;
                }
            } else {
                return null;
            }
        }
    }

    public static class SimplePie extends Metrics.CustomChart {
        private final Callable<String> callable;

        public SimplePie(String chartId, Callable<String> callable) {
            super(chartId);
            this.callable = callable;
        }

        protected JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            String value = (String) this.callable.call();
            if (value != null && !value.isEmpty()) {
                data.addProperty("value", value);
                return data;
            } else {
                return null;
            }
        }
    }

    public abstract static class CustomChart {
        private final String chartId;

        CustomChart(String chartId) {
            Preconditions.checkNotNull(chartId);
            if (chartId.isEmpty()) {
                throw new IllegalArgumentException("ChartId cannot be empty!");
            } else {
                this.chartId = chartId;
            }
        }

        private JsonObject getRequestJsonObject(Plugin plugin, boolean logFailedRequests) {
            JsonObject chart = new JsonObject();
            chart.addProperty("chartId", this.chartId);

            try {
                JsonObject data = this.getChartData();
                if (data == null) {
                    return null;
                } else {
                    chart.add("data", data);
                    return chart;
                }
            } catch (Exception var5) {
                if (logFailedRequests) {
                    plugin.getLogger().warning("Failed to get data for custom chart with id " + this.chartId, var5);
                }

                return null;
            }
        }

        protected abstract JsonObject getChartData() throws Exception;
    }
}
