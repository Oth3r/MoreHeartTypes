package one.oth3r.more_heart_types.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import one.oth3r.more_heart_types.MoreHeartTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Gui.class)
public abstract class RenderHeartMixin {
    @Shadow @Nullable protected abstract Player getCameraPlayer();

    @Inject(method = "renderHeart", at = @At(value = "HEAD"), cancellable = true)
    private void renderHeart(GuiGraphics pGuiGraphics, Gui.HeartType type, int x, int y, boolean pHardcore, boolean blinking, boolean half, CallbackInfo ci) {
        // ^^ BLINKING AND HALF ARE FLIPPED FROM THE SOURCE BECAUSE THEY ARE MISNAMED THERE
        // only run if normal heart or a container heart
        if (!(type.equals(Gui.HeartType.NORMAL) || type.equals(Gui.HeartType.CONTAINER))) return;
        // add a container boolean for when the heart type is a container
        boolean container = type.equals(Gui.HeartType.CONTAINER);

        Player player = this.getCameraPlayer();
        if (player == null) return;

        if (player.hasEffect(MobEffects.HUNGER)) {
            render(ci,pGuiGraphics,x,y,half,blinking,container,"starve",container);
        }

        // get the damageSource and make sure its valid
        DamageSource damageSource = player.getLastDamageSource();
        if (damageSource == null) return;


        // thorns / cactus / berry bush
        if (checkDamage(player, 20, damageSource,"cactus","thorns","sweetBerryBush")) {
            render(ci,pGuiGraphics,x,y,half,blinking,container,"thorns",true);
        }

        // suffocation
        if (damageSource.getMsgId().equals("inWall")) {
            render(ci,pGuiGraphics,x,y,half,blinking,container,"suffocate",false);
        }

        // drowning
        if (checkDamage(player, 20, damageSource,"drown")) {
            render(ci,pGuiGraphics,x,y,half,blinking,container,"drown",false);
        }

        // void
        if (checkDamage(player, 20, damageSource,"outOfWorld")) {
            render(ci,pGuiGraphics,x,y,half,blinking,container,"void",false);
        }

        // fire / lava / campfire / magma
        if (checkDamage(player, 20, damageSource,"lava","onFire","inFire","hotFloor","campfire")) {
            render(ci,pGuiGraphics,x,y,half,blinking,container,"fire",false);
        }
    }

    /**
     * checks if the heart can be rendered, by checking if the time since last damage is good and the damage source is correct
     * @param player the player
     * @param time max time since last damage
     * @param damageSource the last damage source
     * @param strings the correct damage types for the heart
     * @return if the heart can be rendered or not
     */
    @Unique
    private static boolean checkDamage(Player player, int time, DamageSource damageSource, String... strings) {
        // check if the time is under the max
        if (player.level().getGameTime() - MoreHeartTypes.lastDamageTicks > time) return false;
        // check if the damage type matches the criteria
        for (String string : strings) {
            if (damageSource.getMsgId().equals(string)) return true;
        }
        return false;
    }

    /**
     * renders the heart
     * @param name the name for the heart texture
     * @param renderContainer if there is a custom container for the heart
     */
    @Unique
    private static void render(CallbackInfo ci, GuiGraphics pGuiGraphics, int x, int y, boolean half, boolean blinking, boolean container, String name, boolean renderContainer) {
        // get the textures
        ResourceLocation texture = new ResourceLocation("hud/heart/"+name+"_full");
        if (half) texture = new ResourceLocation("hud/heart/"+name+"_half");

        // if container texture
        if (container) {
            // quit if theres no custom container texture
            if (!renderContainer) return;
            // get the container textures
            texture = new ResourceLocation("hud/heart/"+name+"_container");
            if (blinking) texture = new ResourceLocation("hud/heart/"+name+"_container_blinking");
        }

        // draw the texture
        RenderSystem.enableBlend();
        pGuiGraphics.blitSprite(texture,x,y,9,9);
        RenderSystem.disableBlend();

        // cancel the drawing of the other texture
        ci.cancel();
    }
}
