package one.oth3r.more_heart_types.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.Identifier;
import one.oth3r.more_heart_types.*;
import one.oth3r.more_heart_types.heart.HeartSetting;
import one.oth3r.more_heart_types.heart.HeartTypes;
import one.oth3r.otterlib.registry.CustomFileReg;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public class DrawHeartsMixin {

    @Unique private final String[] customFire = {"create.fan_lava", "create.fan_fire"};

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "extractHeart", at = @At(value = "HEAD"), cancellable = true)
    private void drawHeart(GuiGraphicsExtractor graphics, Hud.HeartType type, int x, int y, boolean hardcore, boolean blinking, boolean half, CallbackInfo ci) {
        // only run if normal heart or a container heart
        if (!(type.equals(Hud.HeartType.NORMAL) || type.equals(Hud.HeartType.CONTAINER))) return;
        // add a container boolean for when the heart type is a container
        boolean container = type.equals(Hud.HeartType.CONTAINER);
        // get the config
        Config config = (Config) CustomFileReg.getFile(ModData.ID, "config");

        // make sure the player is valid
        Player player = this.minecraft.player;
        if (player == null) return;

        // starving, apply when hunger effect is active
        if (player.hasEffect(MobEffects.HUNGER)) {
            render(ci,graphics,x,y,half,blinking,container,hardcore,config.getHeartSetting(HeartTypes.STARVING_ID));
        }

        // get the damageSource and make sure its valid
        DamageSource damageSource = player.getLastDamageSource();
        if (damageSource == null) return;


        // thorns / cactus / berry bush
        if (checkDamage(player, 20, damageSource,"cactus","thorns","sweetBerryBush")) {
            render(ci,graphics,x,y,half,blinking,container,hardcore,config.getHeartSetting(HeartTypes.PRICKLY_ID));
        }

        // suffocation
        if (checkDamage(player, 20, damageSource,"inWall")) {
            render(ci,graphics,x,y,half,blinking,container,hardcore,config.getHeartSetting(HeartTypes.SUFFOCATING_ID));
        }

        // drowning
        if (checkDamage(player, 20, damageSource,"drown")) {
            render(ci,graphics,x,y,half,blinking,container,hardcore, config.getHeartSetting(HeartTypes.DROWNING_ID));
        }

        // void
        if (checkDamage(player, 20, damageSource,"outOfWorld")) {
            render(ci,graphics,x,y,half,blinking,container,hardcore,config.getHeartSetting(HeartTypes.VOID_ID));
        }

        // fire / lava / campfire / magma
        if (checkDamage(player, 20, damageSource,"lava","onFire","inFire","hotFloor","campfire",
                customFire[0], customFire[1])) {
            render(ci,graphics,x,y,half,blinking,container,hardcore, config.getHeartSetting(HeartTypes.FIRE_ID));
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
        if (player.level().getGameTime() - ModData.ticksSinceLastDamage > time) return false;
        // check if the damage type matches the criteria
        for (String string : strings) {
            if (damageSource.type().msgId().equals(string)) return true;
        }
        return false;
    }

    /**
     * renders the heart
     * @param heartSetting the heart setting to use for rendering
     */
    @Unique
    private static void render(CallbackInfo ci, GuiGraphicsExtractor graphics, int x, int y, boolean half, boolean blinking, boolean container, boolean hardcore, HeartSetting heartSetting) {
        // get the textures
        Identifier texture = heartSetting.getIdentifier(blinking,half,hardcore,container);
        if (texture == null) return; // if the texture is null, do not render

        // draw the texture
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, texture, x, y, 9, 9);
        // cancel the drawing of the other texture
        ci.cancel();
    }
}
