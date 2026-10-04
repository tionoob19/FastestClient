package dev.fastestclient.mixin;

import dev.fastestclient.FastestConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void fastestcore$renderCredit(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        String text = FastestConfig.get("credit.text");
        if (text.isEmpty()) {
            return;
        }
        int x = this.width - this.textRenderer.getWidth(text) - 2;
        int y = this.height - 20;
        drawStringWithShadow(matrices, this.textRenderer, text, x, y, 0xFFFFFF);
    }
}
