package one.oth3r.more_heart_types;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import one.oth3r.more_heart_types.heart.HeartRegistry;
import one.oth3r.more_heart_types.heart.HeartTypes;
import one.oth3r.otterlib.registry.CustomFileReg;

public class MoreHeartTypes implements ClientModInitializer {
    /**
     * returns the version of the mod.
     * @return the version as a String.
     */
    public static String getVersion() {
        return FabricLoader.getInstance().getModContainer(ModData.ID).orElseThrow().getMetadata().getVersion().getFriendlyString();
    }

    /**
     * returns the directory where the config file is stored.
     * @return the config directory path as a String.
     */
    public static String getConfigDir() {
        return FabricLoader.getInstance().getConfigDir().toFile()+"/more_heart_types/";
    }

    @Override
    public void onInitializeClient() {
        // register all heart types
        HeartTypes.getHeartTypes().forEach(HeartRegistry::register);
        // register the config file to OtterLib, for handling
        CustomFileReg.registerFile(ModData.ID,new CustomFileReg.FileEntry(Config.ID,new Config(),true,true));

        ModData.LOGGER.info("Successfully loaded More Heart Types %s!", ModData.VERSION);

        //todo config screen
    }
}
