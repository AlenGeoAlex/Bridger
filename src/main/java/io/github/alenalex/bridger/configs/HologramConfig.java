package io.github.alenalex.bridger.configs;

import io.github.alenalex.bridger.abstracts.AbstractFileSettings;
import io.github.alenalex.bridger.handler.ConfigurationHandler;

import java.util.List;

public class HologramConfig extends AbstractFileSettings {

    private boolean hologramEnabled;

    private HoloConfigData spawnPointHoloConfigData, endPointHoloConfigData;

    public HologramConfig(ConfigurationHandler handler) {
        super(handler);
    }

    @Override
    public void loadFile() {
        this.hologramEnabled = this.file.getBoolean("hologram-enabled");
        this.spawnPointHoloConfigData = new HoloConfigData(
                this.file.getStringList("spawn-point-hologram.lines"),
                this.file.getInt("spawn-point-hologram.offset-x"),
                this.file.getInt("spawn-point-hologram.offset-y"),
                this.file.getInt("spawn-point-hologram.offset-z"),
                this.file.getBoolean("spawn-point-hologram.enabled")
        );

        this.endPointHoloConfigData = new HoloConfigData(
                this.file.getStringList("end-point-hologram.lines"),
                this.file.getInt("end-point-hologram.offset-x"),
                this.file.getInt("end-point-hologram.offset-y"),
                this.file.getInt("end-point-hologram.offset-z"),
                this.file.getBoolean("end-point-hologram.enabled")
        );
    }

    @Override
    public void prepareReload() {

    }

    public boolean isHologramEnabled() {
        return hologramEnabled;
    }

    public HoloConfigData getSpawnPointHoloData() {
        return spawnPointHoloConfigData;
    }

    public HoloConfigData getEndPointHoloData() {
        return endPointHoloConfigData;
    }

    public class HoloConfigData {
        private final List<String> lines;
        private final int offSetX;
        private final int offSetY;
        private final int offSetZ;
        private final boolean enabled;

        public HoloConfigData(List<String> lines, int offSetX, int offSetY, int offSetZ, boolean enabled) {
            this.lines = lines;
            this.offSetX = offSetX;
            this.offSetY = offSetY;
            this.offSetZ = offSetZ;
            this.enabled = enabled;
        }

        public List<String> getLines() {
            return lines;
        }

        public int getOffSetX() {
            return offSetX;
        }

        public int getOffSetY() {
            return offSetY;
        }

        public int getOffSetZ() {
            return offSetZ;
        }

        public boolean isEnabled() {
            return enabled;
        }
    }
}
