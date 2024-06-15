package one.oth3r.more_heart_types.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import one.oth3r.more_heart_types.MoreHeartTypes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientDamageMixin {

    @Shadow @Final protected MinecraftClient client;

    @Inject(method = "damage", at = @At(value = "HEAD"))
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        // make sure the player is vaild
        PlayerEntity player = this.client.player;
        if (player == null) return;
        // bump the tick amount
        MoreHeartTypes.lastDamageTicks = player.getWorld().getTime();
    }
}
