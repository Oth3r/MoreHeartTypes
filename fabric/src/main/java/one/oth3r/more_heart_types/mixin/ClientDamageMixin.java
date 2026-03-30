package one.oth3r.more_heart_types.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.ClientLevel;
import one.oth3r.more_heart_types.ModData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class ClientDamageMixin extends AbstractClientPlayer {

    public ClientDamageMixin(ClientLevel world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "hurtTo", at = @At(value = "HEAD"))
    public void damage(float health, CallbackInfo ci) {
        float change = this.getHealth() - health;
        if (change > 0.0F) {
            ModData.ticksSinceLastDamage = this.level().getGameTime();
        }

    }
}
