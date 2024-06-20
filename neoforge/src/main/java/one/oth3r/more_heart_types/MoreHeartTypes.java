package one.oth3r.more_heart_types;

import com.mojang.logging.LogUtils;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.entity.living.LivingAttackEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(MoreHeartTypes.MOD_ID)
public class MoreHeartTypes {
    // track the time since last damaged
    public static long lastDamageTicks = 0;

    public static final String MOD_ID = "more_heart_types";
    private static final Logger LOGGER = LogUtils.getLogger();

    public MoreHeartTypes(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Started More Heart Types!");
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onClientSetup(LivingAttackEvent event)
        {
            if (event.getEntity() instanceof LocalPlayer) {
                try {
                    lastDamageTicks = event.getEntity().level().getGameTime();
                } catch (Exception ignored) {}
            }
        }
    }
}
