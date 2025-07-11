package one.oth3r.more_heart_types.heart;

import net.minecraft.util.Identifier;
import one.oth3r.more_heart_types.ModData;

public class HeartSetting {
    private final String id;
    private Boolean enabled;
    private Boolean normal;
    private Boolean hardcore;
    private Boolean container;

    private HeartSetting(Builder builder) {
        this.id = builder.id;
        this.enabled = builder.enabled;
        this.normal = builder.normal;
        this.hardcore = builder.hardcore;
        this.container = builder.container;
    }

    public static class Builder {
        private final String id;
        private boolean enabled = true;
        private Boolean normal = true;
        private Boolean hardcore = null;
        private Boolean container = null;

        public Builder(String id) {
            this.id = id;
        }

        public Builder enabled(boolean val) { this.enabled = val; return this; }
        public Builder normal(Boolean val) { this.normal = val; return this; }
        public Builder hardcore(Boolean val) { this.hardcore = val; return this; }
        public Builder container(Boolean val) { this.container = val; return this; }

        public HeartSetting build() {
            return new HeartSetting(this);
        }
    }

    public String getId() {
        return id;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public boolean setEnabled(Boolean enabled) {
        if (this.enabled == null) return false;
        this.enabled = enabled;
        return true;
    }

    public Boolean getNormal() {
        return normal;
    }

    public void setNormal(Boolean normal) {
        this.normal = normal;
    }

    public Boolean getHardcore() {
        return hardcore;
    }

    public boolean setHardcore(Boolean hardcore) {
        if (this.hardcore == null) return false;
        this.hardcore = hardcore;
        return true;
    }

    public Boolean getContainer() {
        return container;
    }

    public boolean setContainer(Boolean container) {
        if (this.container == null) return false;
        this.container = container;
        return true;
    }

    public Identifier getIdentifier(boolean blinking, boolean half, boolean hardcore, boolean container) {
        // can't display if not enabled
        if (!this.enabled) return null;
        String heartPath = "hud/heart/" + id;
        if (container) {
            if (this.container == null || !this.container) return null;
            return Identifier.of(ModData.ID, heartPath+"_container"+ (blinking ? "_blinking" : ""));
        }
        // add half or full based on the heart
        heartPath += (half ? "_half" : "_full");

        // display hardcore if enabled
        if (hardcore && this.hardcore != null && this.hardcore) return Identifier.of(ModData.ID, heartPath+"_hardcore");
        // display normal hearts if enabled
        if (this.normal != null && this.normal) return Identifier.of(ModData.ID, heartPath);
        // nothing can be displayed
        else return null;
    }
}
