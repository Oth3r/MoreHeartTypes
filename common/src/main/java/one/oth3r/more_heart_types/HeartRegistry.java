package one.oth3r.more_heart_types;

import java.util.ArrayList;

public class HeartRegistry {
    private static final ArrayList<HeartSetting> heartRegistry = new ArrayList<>();

    /**
     * registers a HeartSetting to the registry.
     * @param heartSetting the HeartSetting to register
     */
    public static void register(HeartSetting heartSetting) {
        if (heartSetting == null || heartSetting.getId() == null || heartSetting.getId().isEmpty()) {
            throw new IllegalArgumentException("HeartSetting and its ID must not be null or empty");
        }
        if (getHeartSetting(heartSetting.getId()) != null) {
            throw new IllegalArgumentException("HeartSetting with ID '" + heartSetting.getId() + "' is already registered");
        }
        heartRegistry.add(heartSetting);
    }

    /**
     * Retrieves a HeartSetting by its ID.
     * @param id The ID of the heart setting to retrieve.
     * @return The HeartSetting with the specified ID, or null if not found.
     */
    public static HeartSetting getHeartSetting(String id) {
        return heartRegistry.stream().filter(heartSetting -> heartSetting.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * Returns a list of all registered HeartSettings.
     * @return The list of registered HeartSettings.
     */
    public static ArrayList<HeartSetting> getHeartSettings() {
        return new ArrayList<>(heartRegistry);
    }
}
