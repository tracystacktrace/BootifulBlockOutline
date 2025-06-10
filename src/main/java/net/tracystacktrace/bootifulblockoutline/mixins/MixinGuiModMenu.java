package net.tracystacktrace.bootifulblockoutline.mixins;

import com.fox2code.foxloader.client.gui.GuiModMenu;
import com.fox2code.foxloader.client.gui.GuiModMenuContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = GuiModMenu.class, remap = false)
public class MixinGuiModMenu {

    @Shadow private GuiModMenuContainer modListContainer;

    @ModifyVariable(method = "updateActionButtons", at = @At("STORE"), ordinal = 0)
    private Object bootifulblockoutline$disableConfigButton(Object value) {
        if(this.modListContainer.getSelectedModContainer().getModId().equals("bootifulblockoutline")) {
            return null;
        }
        return value;
    }

}
