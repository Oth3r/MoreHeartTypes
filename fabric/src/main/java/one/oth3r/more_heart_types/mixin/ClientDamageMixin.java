package one.oth3r.more_heart_types.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import one.oth3r.more_heart_types.ModData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientDamageMixin extends AbstractClientPlayerEntity {

    @Shadow @Final protected MinecraftClient client;

    public ClientDamageMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "updateHealth", at = @At(value = "HEAD"))
    public void damage(float health, CallbackInfo ci) {
        // make sure the player is vaild
        PlayerEntity player = this.client.player;
        if (player == null) return;
        // bump the tick amount
        ModData.ticksSinceLastDamage = this.getWorld().getTime();
    }
}
