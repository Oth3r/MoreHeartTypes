package one.oth3r.more_heart_types;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MoreHeartTypes implements ModInitializer {
    // track the time since last damaged
    public static long lastDamageTicks = 0;

    public static final String MOD_ID = "more_heart_types";
    public static final Version VERSION = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().getMetadata().getVersion();
    public static final Logger LOGGER = LogManager.getLogger("More Heart Types");

    @Override
    public void onInitialize() {
        LOGGER.info("Successfully loaded More Heart Types v{}!", VERSION);
    }
}
