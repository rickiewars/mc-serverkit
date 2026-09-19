package com.rwconnected.serverkit.event;

import com.rwconnected.serverkit.api.economy.Patbox.PbEconomyProvider;
import com.rwconnected.serverkit.api.minecraft.player.Player;
import com.rwconnected.serverkit.api.util.time.SystemTimeProvider;
import com.rwconnected.serverkit.service.LoginStreakService;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class PlayerJoinEvent implements ServerPlayConnectionEvents.Join {
    @Override
    public void onPlayReady(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server) {
        ServerPlayer player = handler.getPlayer();
        LoginStreakService loginStreakService = new LoginStreakService(
            new SystemTimeProvider(),
            new PbEconomyProvider(server)
        );
        loginStreakService.process(new Player(player));
    }
}
