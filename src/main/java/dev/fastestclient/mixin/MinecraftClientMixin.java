package dev.fastestclient.mixin;

import dev.fastestclient.FastestConfig;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    // require = 0: kalau nama method beda, mod tetap jalan (judul jendela saja yang tidak berubah)
    @Inject(method = "getWindowTitle", at = @At("RETURN"), cancellable = true, require = 0)
    private void fastestcore$windowTitle(CallbackInfoReturnable<String> cir) {
        String title = FastestConfig.get("window.title");
        if (!title.isEmpty()) {
            cir.setReturnValue(title);
        }
    }
}
