package dev.fastestclient.core.mixin;

import dev.fastestclient.core.client.FastestIcon;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * The nametag (vanilla and Perspective Nametag) draws entity.getDisplayName().
 * Only the local client player is touched, so server-side code and other players are unaffected.
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
	@Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
	private void fastestcore$icon(CallbackInfoReturnable<Text> cir) {
		if ((Object) this == MinecraftClient.getInstance().player) {
			cir.setReturnValue(FastestIcon.prefix(cir.getReturnValue()));
		}
	}
}
