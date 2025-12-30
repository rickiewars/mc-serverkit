package com.rwconnected.serverkit.mixin;

import com.rwconnected.serverkit.ServerKit;
import com.rwconnected.serverkit.api.minecraft.storage.ServerKitPlayerData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerDataMixin implements ServerKitPlayerData {
    Identifier loginStreakKey = Identifier.of(ServerKit.MOD_ID, "login_streak");
    Identifier loginStreakRecordKey = Identifier.of(ServerKit.MOD_ID, "login_streak_record");
    Identifier lastLoginDateKey = Identifier.of(ServerKit.MOD_ID, "last_login_date");

    @Unique private int loginStreak = 1;
    @Unique private int loginStreakRecord = 1;
    @Unique private String lastLoginDate = "";

    @Inject(method = "writeCustomData", at = @At("TAIL"))
    private void writeServerKitData(WriteView view, CallbackInfo ci) {
        view.putInt(loginStreakKey.toString(), loginStreak);
        view.putInt(loginStreakRecordKey.toString(), loginStreakRecord);
        view.putString(lastLoginDateKey.toString(), lastLoginDate);
    }

    @Inject(method = "readCustomData", at = @At("TAIL"))
    private void readServerKitData(ReadView view, CallbackInfo ci) {
        loginStreak = view.getInt(loginStreakKey.toString(), 1);
        loginStreakRecord = view.getInt(loginStreakRecordKey.toString(), 1);
        lastLoginDate = view.getString(lastLoginDateKey.toString(), "");
    }

    @Inject(method = "copyFrom", at = @At("TAIL"))
    private void copyServerKitData(ServerPlayerEntity old, boolean alive, CallbackInfo ci) {
        this.loginStreak = ((ServerKitPlayerData) old).serverkit_getLoginStreak();
        this.loginStreakRecord = ((ServerKitPlayerData) old).serverkit_getLoginStreakRecord();
        this.lastLoginDate = ((ServerKitPlayerData) old).serverkit_getLastLoginDate();
    }

    @Override
    public int serverkit_getLoginStreak() {
        return loginStreak;
    }

    @Override
    public void serverkit_setLoginStreak(int streak) {
        this.loginStreak = streak;
    }

    @Override
    public int serverkit_getLoginStreakRecord() {
        return loginStreakRecord;
    }

    @Override
    public void serverkit_setLoginStreakRecord(int record) {
        this.loginStreakRecord = record;
    }

    @Override
    public String serverkit_getLastLoginDate() {
        return lastLoginDate;
    }

    @Override
    public void serverkit_setLastLoginDate(String date) {
        this.lastLoginDate = date;
    }

}
