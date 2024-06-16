package one.oth3r.more_heart_types.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import one.oth3r.more_heart_types.MoreHeartTypes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class DrawHeartsMixin extends DrawableHelper {

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "drawHeart", at = @At(value = "HEAD"), cancellable = true)
    private void drawHeart(MatrixStack matrices, InGameHud.HeartType type, int x, int y, int v, boolean blinking, boolean half, CallbackInfo ci) {
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
            draw(texture,ci,matrices,x,y);
        }

        // only if the player has taken damage in the last 20 ticks
        if (player.getWorld().getTime() - MoreHeartTypes.lastDamageTicks > 20) return;

        // get the damageSource and make sure its valid
        DamageSource damageSource = player.getRecentDamageSource();
        if (damageSource == null) return;

        // PRINT!! IGNORE
//        System.out.println(damageSource.getName());

        // thorns / cactus / berry bush
        if (damageSource.getName().equals("cactus") ||
                damageSource.getName().equals("thorns") ||
                damageSource.getName().equals("sweetBerryBush")) {
            Identifier texture = new Identifier("textures/gui/sprites/hud/heart/thorns_full.png");
            if (half) texture = new Identifier("textures/gui/sprites/hud/heart/thorns_half.png");
            if (container) {
                texture = new Identifier("textures/gui/sprites/hud/heart/thorns_container.png");
                if (blinking) texture = new Identifier("textures/gui/sprites/hud/heart/thorns_container_blinking.png");
            }
            draw(texture,ci,matrices,x,y);
        }

        // suffocation
        if (damageSource.getName().equals("inWall")) {
            Identifier texture = new Identifier("textures/gui/sprites/hud/heart/suffocate_full.png");
            if (half) texture = new Identifier("textures/gui/sprites/hud/heart/suffocate_half.png");
            if (container) return;
            draw(texture,ci,matrices,x,y);
        }

        // drowning
        if (damageSource.getName().equals("drown")) {
            Identifier texture = new Identifier("textures/gui/sprites/hud/heart/drown_full.png");
            if (half) texture = new Identifier("textures/gui/sprites/hud/heart/drown_half.png");
            if (container) return;
            draw(texture,ci,matrices,x,y);
        }

        // void
        if (damageSource.getName().equals("outOfWorld")) {
            Identifier texture = new Identifier("textures/gui/sprites/hud/heart/void_full.png");
            if (half) texture = new Identifier("textures/gui/sprites/hud/heart/void_half.png");
            if (container) return;
            draw(texture,ci,matrices,x,y);
        }

        // fire / lava / campfire / magma
        if (damageSource.getName().equals("lava") ||
                damageSource.getName().equals("onFire") ||
                damageSource.getName().equals("inFire") ||
                damageSource.getName().equals("hotFloor")) {
            Identifier texture = new Identifier("textures/gui/sprites/hud/heart/fire_full.png");
            if (half) texture = new Identifier("textures/gui/sprites/hud/heart/fire_half.png");
            if (container) return;
            // not animated for some reason,,,
            draw(texture,ci,matrices,x,y,9,72);
        }
    }

    @Unique private static void draw(Identifier texture, CallbackInfo ci, MatrixStack matrices, int x, int y) {
        draw(texture,ci,matrices,x,y,9,9);
    }

    @Unique private static void draw(Identifier texture, CallbackInfo ci, MatrixStack matrices, int x, int y, int textureX, int textureY) {
        RenderSystem.setShaderTexture(0, texture);
        drawTexture(matrices, x, y, 0, 0, 9, 9,9,textureX,textureY);
        RenderSystem.setShaderTexture(0, DrawableHelper.GUI_ICONS_TEXTURE);
        ci.cancel();
    }
}
