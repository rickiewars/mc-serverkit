package com.rwconnected.serverkit.event;

import com.rwconnected.serverkit.api.economy.Patbox.PbEconomyProvider;
import com.rwconnected.serverkit.api.minecraft.player.Player;
import com.rwconnected.serverkit.api.util.time.SystemTimeProvider;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import com.rwconnected.serverkit.service.LoginStreakService;

public class PlayerJoinEvent implements ServerPlayConnectionEvents.Join {
    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        ServerPlayerEntity player = handler.getPlayer();
        LoginStreakService loginStreakService = new LoginStreakService(
            new SystemTimeProvider(),
            new PbEconomyProvider(server)
        );
        loginStreakService.process(new Player(player));
    }
}
