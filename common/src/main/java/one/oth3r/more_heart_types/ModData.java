package one.oth3r.more_heart_types;

import one.oth3r.otterlib.base.OtterLogger;

public class ModData {
    /**
     * The mod ID for More Heart Types.
     */
    public static final String ID = "more_heart_types";
    /**
     * Logger for the mod.
     */
    public static final OtterLogger LOGGER = new OtterLogger(ID);
    /**
     * returns the directory where the config file is stored.
     */
    public static final String CONFIG_DIR = MoreHeartTypes.getConfigDir();
    /**
     * returns the version of the mod.
     */
    public static final String VERSION = MoreHeartTypes.getVersion();


    /**
     * tracks the time since last damaged
     */
    public static long ticksSinceLastDamage = 0;
}
