package dev.fastestclient;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Default options.txt values, applied once on first launch.
 * Edit nilai di sini untuk mengubah default Fastest Client.
 */
public final class DefaultSettings {
    public static final Map<String, String> DEFAULTS = new LinkedHashMap<String, String>();

    static {
        DEFAULTS.put("graphicsMode", "0");        // 0 = Fast
        DEFAULTS.put("renderDistance", "8");
        DEFAULTS.put("maxFps", "260");            // 260 = unlimited
        DEFAULTS.put("enableVsync", "false");
        DEFAULTS.put("particles", "1");           // 1 = decreased (0 all, 2 minimal)
        DEFAULTS.put("entityShadows", "false");
        DEFAULTS.put("biomeBlendRadius", "0");
        DEFAULTS.put("renderClouds", "false");
        DEFAULTS.put("ao", "0");                  // smooth lighting off
    }

    private DefaultSettings() {
    }
}
