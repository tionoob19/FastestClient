package dev.fastestclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class FastestCore implements ClientModInitializer {
    public static final String MOD_ID = "fastestcore";
    public static final Logger LOGGER = LogManager.getLogger("FastestCore");

    @Override
    public void onInitializeClient() {
        FastestConfig.load();

        // Resource pack bawaan (selalu aktif)
        Optional<ModContainer> container = FabricLoader.getInstance().getModContainer(MOD_ID);
        if (container.isPresent()) {
            ResourceManagerHelper.registerBuiltinResourcePack(
                    new Identifier(MOD_ID, "fastest-resources"),
                    container.get(),
                    ResourcePackActivationType.ALWAYS_ENABLED);
        }

        FastestDiscord.start();
        LOGGER.info("FastestCore loaded.");
    }
}
