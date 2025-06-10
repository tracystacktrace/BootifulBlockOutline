package net.tracystacktrace.bootifulblockoutline.mixins;

import net.minecraft.client.renderer.world.RenderGlobal;
import net.minecraft.common.util.math.AxisAlignedBB;
import net.tracystacktrace.bootifulblockoutline.BootifulBlockOutline;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderGlobal.class)
public class MixinRenderGlobal {

    @Redirect(method = "drawSelectionBox", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glLineWidth(F)V"))
    private void bootifulblockoutline$injectWidth(float width) {
        GL11.glLineWidth(BootifulBlockOutline.CONFIG.blockOutlineWidth);
    }

    @ModifyVariable(method = "drawOutlinedBoundingBox", at = @At("STORE"), ordinal = 0, argsOnly = true)
    private AxisAlignedBB injected(AxisAlignedBB aabb) {
        float offset = (BootifulBlockOutline.CONFIG.blockOutlineWidth / 2.0f) * 0.002f;
        return aabb.expand(offset, offset, offset);
    }

}
