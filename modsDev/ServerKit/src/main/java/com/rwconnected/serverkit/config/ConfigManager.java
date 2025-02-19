package com.rwconnected.serverkit.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rwconnected.serverkit.ServerKit;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

@SuppressWarnings("UnusedReturnValue")
public class ConfigManager {
    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .create();

    private static final File configFile = new File("config", Config.FILE_NAME);

    private static void ensureDirectoryExists() {
        File directory = configFile.getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            throw new RuntimeException("Failed to create config directory");
        }
    }

    public static void saveConfig() throws IOException {
        saveConfig(Config.instance());
    }

    private static void saveConfig(Config config) throws IOException {
        ensureDirectoryExists();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8));
        writer.write(GSON.toJson(config));
        writer.close();
    }

    private static Config getConfigData() throws IOException {
        return configFile.exists() ? GSON.fromJson(
            new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8),
            Config.class
        ) : null;
    }

    public static void loadConfig(){
        try {
            ServerKit.LOGGER.info("Loading config");
            Config config = getConfigData();
            if (config != null) {
                Config.init(config);
            }
            // Make sure the config file is in sync with the current config object (in case of default values)
            saveConfig();

        } catch (IOException e){
            throw new RuntimeException("Failed to load config", e);
        }
    }
}
