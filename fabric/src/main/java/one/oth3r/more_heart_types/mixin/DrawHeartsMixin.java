package one.oth3r.more_heart_types.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import one.oth3r.more_heart_types.MoreHeartTypes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class DrawHeartsMixin {

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "drawHeart", at = @At(value = "HEAD"), cancellable = true)
    private void drawHeart(DrawContext context, InGameHud.HeartType type, int x, int y, int v, boolean blinking, boolean half, CallbackInfo ci) {
        // only run if normal heart or a container heart
        if (!(type.equals(InGameHud.HeartType.NORMAL) || type.equals(InGameHud.HeartType.CONTAINER))) return;
        // add a container boolean for when the heart type is a container
        boolean container = type.equals(InGameHud.HeartType.CONTAINER);

        // make sure the player is valid
        PlayerEntity player = this.client.player;
        if (player == null) return;

        // starving, apply when hunger effect is active
        if (player.hasStatusEffect(StatusEffects.HUNGER)) {
            Identifier texture = new Identifier("textures/gui/sprites/hud/heart/starve_full.png");
            if (half) texture = new Identifier("textures/gui/sprites/hud/heart/starve_half.png");
            if (container) {
                texture = new Identifier("textures/gui/sprites/hud/heart/starve_container.png");
                if (blinking) texture = new Identifier("textures/gui/sprites/hud/heart/starve_container_blinking.png");
            }
            context.drawTexture(texture, x, y,0,0, 9, 9,9,9);
            ci.cancel();
        }

        // only if the player has taken damage in the last 20 ticks
        if (player.getWorld().getTime() - MoreHeartTypes.lastDamageTicks > 20) return;

        // get the damageSource and make sure its valid
        DamageSource damageSource = player.getRecentDamageSource();
        if (damageSource == null) return;

        // PRINT!! IGNORE
//        System.out.println(damageSource.getType().msgId());

        // thorns / cactus / berry bush
        if (damageSource.getType().msgId().equals("cactus") ||
                damageSource.getType().msgId().equals("thorns") ||
                damageSource.getType().msgId().equals("sweetBerryBush")) {
            Identifier texture = new Identifier("textures/gui/sprites/hud/heart/thorns_full.png");
            if (half) texture = new Identifier("textures/gui/sprites/hud/heart/thorns_half.png");
            if (container) {
                texture = new Identifier("textures/gui/sprites/hud/heart/thorns_container.png");
                if (blinking) texture = new Identifier("textures/gui/sprites/hud/heart/thorns_container_blinking.png");
            }
            context.drawTexture(texture, x, y,0,0, 9, 9,9,9);
            ci.cancel();
        }

        // suffocation
        if (damageSource.getType().msgId().equals("inWall")) {
            Identifier texture = new Identifier("textures/gui/sprites/hud/heart/suffocate_full.png");
            if (half) texture = new Identifier("textures/gui/sprites/hud/heart/suffocate_half.png");
            if (container) return;
            context.drawTexture(texture, x, y,0,0, 9, 9,9,9);
            ci.cancel();
        }

        // drowning
        if (damageSource.getType().msgId().equals("drown")) {
            Identifier texture = new Identifier("textures/gui/sprites/hud/heart/drown_full.png");
            if (half) texture = new Identifier("textures/gui/sprites/hud/heart/drown_half.png");
            if (container) return;
            context.drawTexture(texture, x, y,0,0, 9, 9,9,9);
            ci.cancel();
        }

        // void
        if (damageSource.getType().msgId().equals("outOfWorld")) {
            Identifier texture = new Identifier("textures/gui/sprites/hud/heart/void_full.png");
            if (half) texture = new Identifier("textures/gui/sprites/hud/heart/void_half.png");
            if (container) return;
            context.drawTexture(texture, x, y,0,0, 9, 9,9,9);
            ci.cancel();
        }

        // fire / lava / campfire / magma
        if (damageSource.getType().msgId().equals("lava") ||
                damageSource.getType().msgId().equals("onFire") ||
                damageSource.getType().msgId().equals("inFire") ||
                damageSource.getType().msgId().equals("hotFloor")) {
            Identifier texture = new Identifier("textures/gui/sprites/hud/heart/fire_full.png");
            if (half) texture = new Identifier("textures/gui/sprites/hud/heart/fire_half.png");
            if (container) return;
            // not animated for some reason,,,
            context.drawTexture(texture, x, y,0,0, 9, 9, 9, 72);
            ci.cancel();
        }
    }
}
