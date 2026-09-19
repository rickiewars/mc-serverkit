package com.rwconnected.serverkit.mixin;

import com.rwconnected.serverkit.ServerKit;
import com.rwconnected.serverkit.api.minecraft.storage.ServerKitPlayerData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerDataMixin implements ServerKitPlayerData {
    Identifier loginStreakKey = Identifier.fromNamespaceAndPath(ServerKit.MOD_ID, "login_streak");
    Identifier loginStreakRecordKey = Identifier.fromNamespaceAndPath(ServerKit.MOD_ID, "login_streak_record");
    Identifier lastLoginDateKey = Identifier.fromNamespaceAndPath(ServerKit.MOD_ID, "last_login_date");

    @Unique private int loginStreak = 1;
    @Unique private int loginStreakRecord = 1;
    @Unique private String lastLoginDate = "";

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void writeServerKitData(ValueOutput output, CallbackInfo ci) {
        output.putInt(loginStreakKey.toString(), loginStreak);
        output.putInt(loginStreakRecordKey.toString(), loginStreakRecord);
        output.putString(lastLoginDateKey.toString(), lastLoginDate);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readServerKitData(ValueInput input, CallbackInfo ci) {
        loginStreak = input.getIntOr(loginStreakKey.toString(), 1);
        loginStreakRecord = input.getIntOr(loginStreakRecordKey.toString(), 1);
        lastLoginDate = input.getStringOr(lastLoginDateKey.toString(), "");
    }

    @Inject(method = "restoreFrom", at = @At("TAIL"))
    private void copyServerKitData(ServerPlayer old, boolean alive, CallbackInfo ci) {
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
