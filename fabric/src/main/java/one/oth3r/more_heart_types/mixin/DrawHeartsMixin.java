package one.oth3r.more_heart_types.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
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

        // make sure the player is valid
        PlayerEntity player = this.client.player;
        if (player == null) return;

        // starving, apply when hunger effect is active
        if (player.hasStatusEffect(StatusEffects.HUNGER)) {
            render(ci,context,x,y,half,blinking,container,"starve",true);
        }

        // get the damageSource and make sure its valid
        DamageSource damageSource = player.getRecentDamageSource();
        if (damageSource == null) return;


        // thorns / cactus / berry bush
        if (checkDamage(player, 20, damageSource,"cactus","thorns","sweetBerryBush")) {
            render(ci,context,x,y,half,blinking,container,"thorns",true);
        }

        // suffocation
        if (checkDamage(player, 20, damageSource,"inWall")) {
            render(ci,context,x,y,half,blinking,container,"suffocate",false);
        }

        // drowning
        if (checkDamage(player, 20, damageSource,"drown")) {
            render(ci,context,x,y,half,blinking,container,"drown",false);
        }

        // void
        if (checkDamage(player, 20, damageSource,"outOfWorld")) {
            render(ci,context,x,y,half,blinking,container,"void",false);
        }

        // fire / lava / campfire / magma
        if (checkDamage(player, 20, damageSource,"lava","onFire","inFire","hotFloor","campfire",
                customFire[0], customFire[1])) {
            render(ci,context,x,y,half,blinking,container,"fire",false);
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
     * @param name the name for the heart texture
     * @param renderContainer if there is a custom container for the heart
     */
    @Unique
    private static void render(CallbackInfo ci, DrawContext context, int x, int y, boolean half, boolean blinking, boolean container, String name, boolean renderContainer) {
        // get the textures
        Identifier texture = Identifier.of("hud/heart/"+name+"_full");
        if (half) texture = Identifier.of("hud/heart/"+name+"_half");

        // if container texture
        if (container) {
            // quit if theres no custom container texture
            if (!renderContainer) return;
            // get the container textures
            texture = Identifier.of("hud/heart/"+name+"_container");
            if (blinking) texture = Identifier.of("hud/heart/"+name+"_container_blinking");
        }

        // draw the texture
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, 9, 9);
        // cancel the drawing of the other texture
        ci.cancel();
    }
}
