package net.tracystacktrace.bootifulblockoutline.mixins;

import net.minecraft.client.renderer.world.RenderGlobal;
import net.tracystacktrace.bootifulblockoutline.BootifulBlockOutline;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderGlobal.class)
public class MixinRenderGlobal {

    @Redirect(method = "drawSelectionBox", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glLineWidth(F)V"))
    private void bootifulblockoutline$injectWidth(float width) {
        GL11.glLineWidth(BootifulBlockOutline.CONFIG.selectionBoxWidth);
    }


}
