package dev.fastestclient;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Menulis default options.txt SEKALI sebelum Minecraft membacanya.
 * Kalau flag file sudah ada, tidak menimpa settingan player.
 */
public class FastestPreLaunch implements PreLaunchEntrypoint {

    @Override
    public void onPreLaunch() {
        try {
            Path flagDir = FabricLoader.getInstance().getConfigDir().resolve("fastestcore");
            Path flag = flagDir.resolve("default-settings-applied");
            if (Files.exists(flag)) {
                return;
            }

            Path options = FabricLoader.getInstance().getGameDir().resolve("options.txt");
            Map<String, String> values = new LinkedHashMap<String, String>();

            if (Files.exists(options)) {
                List<String> lines = Files.readAllLines(options, StandardCharsets.UTF_8);
                for (String line : lines) {
                    int idx = line.indexOf(':');
                    if (idx > 0) {
                        values.put(line.substring(0, idx), line.substring(idx + 1));
                    }
                }
            }

            values.putAll(DefaultSettings.DEFAULTS);

            List<String> out = new ArrayList<String>();
            for (Map.Entry<String, String> e : values.entrySet()) {
                out.add(e.getKey() + ":" + e.getValue());
            }
            Files.write(options, out, StandardCharsets.UTF_8);

            Files.createDirectories(flagDir);
            Files.write(flag, "applied".getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("[FastestCore] Failed to apply default settings: " + e);
        }
    }
}
