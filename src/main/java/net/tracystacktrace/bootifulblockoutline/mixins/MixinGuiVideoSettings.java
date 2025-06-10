package net.tracystacktrace.bootifulblockoutline.mixins;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiVideoSettings;
import net.tracystacktrace.bootifulblockoutline.BootifulBlockOutline;
import net.tracystacktrace.bootifulblockoutline.gui.GuiOutlineEditor;
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
            this.mc.displayGuiScreen(new GuiOutlineEditor(this, "gui.editBlockOutline", BootifulBlockOutline.CONFIG.selectionBoxColor, null));
            ci.cancel();
        }
    }

}
