package dev.fastestclient;

import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FastestCore implements ClientModInitializer {
    public static final String MOD_ID = "fastestcore";
    public static final Logger LOGGER = LogManager.getLogger("FastestCore");

    @Override
    public void onInitializeClient() {
        LOGGER.info("FastestCore loaded.");
    }
}
