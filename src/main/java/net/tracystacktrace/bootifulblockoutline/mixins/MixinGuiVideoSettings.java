package net.tracystacktrace.bootifulblockoutline.mixins;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.common.util.i18n.StringTranslate;
import net.tracystacktrace.bootifulblockoutline.gui.GuiHomeConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiVideoSettings.class)
public class MixinGuiVideoSettings extends GuiScreen {

    @Inject(method = "actionPerformed", cancellable = true, at = @At(value = "HEAD"))
    private void bootifulblockoutline$injectOpenGUI(GuiButton button, CallbackInfo ci) {
        if (button.enabled && button.id == 107) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiHomeConfig(this));
            ci.cancel();
        }
    }

    @Inject(method = "initGui", at = @At("TAIL"))
    private void bootifulblockoutline$injectGiveNiceName(CallbackInfo ci) {
        ((GuiButton)this.controlList.get(5)).displayString = StringTranslate.getInstance().translateKey("bootifulblockoutline.button.entrypoint");
    }

}
