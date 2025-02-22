package com.rwconnected.serverkit;

import com.rwconnected.serverkit.config.ConfigManager;
import com.rwconnected.serverkit.util.ModRegistries;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerKit implements ModInitializer {
	public static final String MOD_ID = "serverkit";
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static MinecraftServer mcServer = null;

	@Override
	public void onInitialize() {
		LOGGER.info("RWConnected - ServerKit is initializing!");
		ConfigManager.loadConfig();
		ModRegistries.register();
	}

	public static void initServer(MinecraftServer server) {
		mcServer = server;
	}

	public static MinecraftServer getServer() {
		return mcServer;
	}
}