package one.oth3r.more_heart_types.heart;

import java.util.ArrayList;

public class HeartTypes {
    public static final String DROWNING_ID = "drown";
    public static final String FIRE_ID = "fire";
    public static final String STARVING_ID = "starving";
    public static final String SUFFOCATING_ID = "suffocate";
    public static final String PRICKLY_ID = "prickly";
    public static final String VOID_ID = "void";

    /**
     * Returns a list of all heart types.
     * @return The list of heart types.
     */
    public static ArrayList<HeartSetting> getHeartTypes() {
        ArrayList<HeartSetting> heartSettings = new ArrayList<>();
        heartSettings.add(new HeartSetting.Builder(DROWNING_ID).hardcore().container().blinking().build());
        heartSettings.add(new HeartSetting.Builder(FIRE_ID).hardcore().container().build());
        heartSettings.add(new HeartSetting.Builder(STARVING_ID).hardcore().container().blinking().build());
        heartSettings.add(new HeartSetting.Builder(SUFFOCATING_ID).hardcore().blinking().build());
        heartSettings.add(new HeartSetting.Builder(PRICKLY_ID).hardcore().container().blinking().build());
        heartSettings.add(new HeartSetting.Builder(VOID_ID).hardcore().container().blinking().build());

        return heartSettings;
    }

}
