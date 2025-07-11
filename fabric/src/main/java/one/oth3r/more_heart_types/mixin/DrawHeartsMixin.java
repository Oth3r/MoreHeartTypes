package one.oth3r.more_heart_types.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import one.oth3r.more_heart_types.Config;
import one.oth3r.more_heart_types.HeartSetting;
import one.oth3r.more_heart_types.HeartTypes;
import one.oth3r.more_heart_types.MoreHeartTypes;
import one.oth3r.otterlib.registry.CustomFileReg;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class DrawHeartsMixin {

    @Unique private final String[] customFire = {"create.fan_lava", "create.fan_fire"};

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "drawHeart", at = @At(value = "HEAD"), cancellable = true)
    private void drawHeart(DrawContext context, InGameHud.HeartType type, int x, int y, boolean hardcore, boolean blinking, boolean half, CallbackInfo ci) {
        // only run if normal heart or a container heart
        if (!(type.equals(InGameHud.HeartType.NORMAL) || type.equals(InGameHud.HeartType.CONTAINER))) return;
        // add a container boolean for when the heart type is a container
        boolean container = type.equals(InGameHud.HeartType.CONTAINER);
        // get the config
        Config config = (Config) CustomFileReg.getFile(MoreHeartTypes.MOD_ID, "config");

        // make sure the player is valid
        PlayerEntity player = this.client.player;
        if (player == null) return;

        // starving, apply when hunger effect is active
        if (player.hasStatusEffect(StatusEffects.HUNGER)) {
            render(ci,context,x,y,half,blinking,container,config.getHeartSetting(HeartTypes.STARVING_ID));
        }

        // get the damageSource and make sure its valid
        DamageSource damageSource = player.getRecentDamageSource();
        if (damageSource == null) return;


        // thorns / cactus / berry bush
        if (checkDamage(player, 20, damageSource,"cactus","thorns","sweetBerryBush")) {
            render(ci,context,x,y,half,blinking,container,config.getHeartSetting(HeartTypes.PRICKLY_ID));
        }

        // suffocation
        if (checkDamage(player, 20, damageSource,"inWall")) {
            render(ci,context,x,y,half,blinking,container,config.getHeartSetting(HeartTypes.SUFFOCATING_ID));
        }

        // drowning
        if (checkDamage(player, 20, damageSource,"drown")) {
            render(ci,context,x,y,half,blinking,container, config.getHeartSetting(HeartTypes.DROWNING_ID));
        }

        // void
        if (checkDamage(player, 20, damageSource,"outOfWorld")) {
            render(ci,context,x,y,half,blinking,container,config.getHeartSetting(HeartTypes.VOID_ID));
        }

        // fire / lava / campfire / magma
        if (checkDamage(player, 20, damageSource,"lava","onFire","inFire","hotFloor","campfire",
                customFire[0], customFire[1])) {
            render(ci,context,x,y,half,blinking,container, config.getHeartSetting(HeartTypes.FIRE_ID));
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
    private static boolean checkDamage(PlayerEntity player, int time, DamageSource damageSource, String... strings) {
        // check if the time is under the max
        if (player.getWorld().getTime() - MoreHeartTypes.lastDamageTicks > time) return false;
        // check if the damage type matches the criteria
        for (String string : strings) {
            if (damageSource.getType().msgId().equals(string)) return true;
        }
        return false;
    }

    /**
     * renders the heart
     * @param heartSetting the heart setting to use for rendering
     */
    @Unique
    private static void render(CallbackInfo ci, DrawContext context, int x, int y, boolean half, boolean blinking, boolean container, HeartSetting heartSetting) {
        // get the textures
        Identifier texture = heartSetting.getIdentifier(blinking,half,false,container);
        if (texture == null) return; // if the texture is null, do not render

        // draw the texture
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, 9, 9);
        // cancel the drawing of the other texture
        ci.cancel();
    }
}
