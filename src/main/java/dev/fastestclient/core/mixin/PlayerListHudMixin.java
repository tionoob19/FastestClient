package dev.fastestclient.core.mixin;

import dev.fastestclient.core.client.FastestIcon;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {
	@Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
	private void fastestcore$icon(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if (mc.player != null && entry.getProfile().getId().equals(mc.player.getUuid())) {
			cir.setReturnValue(FastestIcon.prefix(cir.getReturnValue()));
		}
	}
}
