package dev.fastestclient.core.mixin;

import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Inject(method = "getWindowTitle", at = @At("HEAD"), cancellable = true)
	private void fastestcore$title(CallbackInfoReturnable<String> cir) {
		cir.setReturnValue("Fastest Client " + SharedConstants.getGameVersion().getName());
	}
}
