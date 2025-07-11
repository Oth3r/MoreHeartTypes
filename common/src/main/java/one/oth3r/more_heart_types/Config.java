package one.oth3r.more_heart_types;

import com.google.gson.annotations.SerializedName;
import one.oth3r.otterlib.base.OtterLogger;
import one.oth3r.otterlib.file.CustomFile;
import one.oth3r.otterlib.file.FileSettings;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class Config implements CustomFile<Config> {
    public static final String ID = "config";

    @SerializedName("version")
    private double version = 1.0;
    @SerializedName("heart_settings")
    private ArrayList<HeartSetting> heartSettings = HeartRegistry.getHeartSettings();

    public Config() {}

    public Config(Config config) {
        copyFileData(config);
    }

    /**
     * gets all heart settings from the config.
     * @return An ArrayList of HeartSetting objects.
     */
    public ArrayList<HeartSetting> getHeartSettings() {
        return heartSettings;
    }

    /**
     * Retrieves a HeartSetting by its ID.
     * @param id The ID of the heart setting to retrieve.
     * @return The HeartSetting with the specified ID, or null if not found.
     */
    public HeartSetting getHeartSetting(String id) {
        return heartSettings.stream().filter(hs -> hs.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public FileSettings getFileSettings() {
        return new FileSettings(new OtterLogger("more_heart_types"));
    }

    @Override
    public Path getFilePath() {
        return Paths.get(ModData.CONFIG_DIR,"config.json");
    }

    @Override
    public void reset() {
        copyFileData(new Config());
    }

    @Override
    public Class<Config> getFileClass() {
        return Config.class;
    }

    @Override
    public void copyFileData(Config config) {
        this.version = config.version;
        this.heartSettings = new ArrayList<>(config.heartSettings);
    }

    @Override
    public void updateInstance() {
        // make sure every heart type that is registered is also in the config, and if not, add it
        for (HeartSetting heartSetting : HeartTypes.getHeartTypes()) {
            // null check
            if (heartSetting.getId() == null || heartSetting.getId().isEmpty()) continue;
            // add if not already present
            if (heartSettings.stream().noneMatch(hs -> hs.getId().equals(heartSetting.getId()))) {
                heartSettings.add(heartSetting);
            }
        }
    }

    @Override
    public Config clone() {
        return new Config(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Config config = (Config) o;
        return Double.compare(version, config.version) == 0 && Objects.equals(heartSettings, config.heartSettings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, heartSettings);
    }
}
