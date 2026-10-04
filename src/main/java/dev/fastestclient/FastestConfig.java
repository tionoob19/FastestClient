package dev.fastestclient;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Config sederhana: config/fastestcore.properties
 * Dibuat otomatis kalau belum ada. Ubah file itu, tidak perlu compile ulang.
 */
public final class FastestConfig {
    private static final String[][] DEFAULTS = {
            {"credit.text", "Fastest Client"},
            {"window.title", "Fastest Client 1.16.5"},
            {"discord.enabled", "true"},
            {"discord.client_id", ""},
            {"discord.details", "Playing Fastest Client"},
            {"discord.state", "Built for speed"},
            {"discord.large_image", "icon"},
    };

    private static final Properties PROPS = new Properties();
    private static boolean loaded;

    private FastestConfig() {
    }

    public static synchronized void load() {
        if (loaded) {
            return;
        }
        loaded = true;

        for (String[] d : DEFAULTS) {
            PROPS.setProperty(d[0], d[1]);
        }

        Path file = FabricLoader.getInstance().getConfigDir().resolve("fastestcore.properties");
        try {
            if (Files.exists(file)) {
                Properties user = new Properties();
                InputStream in = Files.newInputStream(file);
                try {
                    user.load(in);
                } finally {
                    in.close();
                }
                for (String name : user.stringPropertyNames()) {
                    PROPS.setProperty(name, user.getProperty(name));
                }
            } else {
                List<String> lines = new ArrayList<String>();
                lines.add("# Fastest Client config");
                lines.add("# discord.client_id: isi dengan Application ID dari discord.com/developers");
                for (String[] d : DEFAULTS) {
                    lines.add(d[0] + "=" + d[1]);
                }
                Files.createDirectories(file.getParent());
                Files.write(file, lines, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            System.err.println("[FastestCore] Config error: " + e);
        }
    }

    public static String get(String key) {
        load();
        String v = PROPS.getProperty(key);
        return v == null ? "" : v;
    }

    public static boolean getBoolean(String key) {
        return "true".equalsIgnoreCase(get(key).trim());
    }
}
