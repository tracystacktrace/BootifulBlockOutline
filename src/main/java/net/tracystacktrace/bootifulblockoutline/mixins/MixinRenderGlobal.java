package net.tracystacktrace.bootifulblockoutline.mixins;

import net.minecraft.client.renderer.world.RenderGlobal;
import net.minecraft.client.renderer.world.Tessellator;
import net.minecraft.common.entity.player.EntityPlayer;
import net.minecraft.common.util.math.AxisAlignedBB;
import net.minecraft.common.util.physics.MovingObjectPosition;
import net.minecraft.common.world.EnumMovingObjectType;
import net.tracystacktrace.bootifulblockoutline.BootifulBlockOutline;
import net.tracystacktrace.bootifulblockoutline.inject.CustomOutlineRenderer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderGlobal.class)
public class MixinRenderGlobal {

    @Inject(method = "drawSelectionBox", at = @At(value = "HEAD"), cancellable = true)
    private void bootifulblockoutline$injectInitialChecks(EntityPlayer player, MovingObjectPosition hitResult, float deltaTicks, CallbackInfo ci) {
        if (BootifulBlockOutline.CONFIG.blockOutlineMode == 0 && hitResult.typeOfHit == EnumMovingObjectType.TILE) {
            ci.cancel();
            return;
        }

        if (BootifulBlockOutline.CONFIG.entityOutlineMode == 0 && hitResult.typeOfHit == EnumMovingObjectType.ENTITY) {
            ci.cancel();
            return;
        }

        if (BootifulBlockOutline.CONFIG.entityOutlineMode != 0 && hitResult.typeOfHit == EnumMovingObjectType.ENTITY) {
            CustomOutlineRenderer.renderEntityOutline(player, hitResult, deltaTicks);
            ci.cancel();
        }
    }

    @Redirect(method = "drawSelectionBox", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glLineWidth(F)V"))
    private void bootifulblockoutline$injectWidth(float width) {
        GL11.glLineWidth(BootifulBlockOutline.CONFIG.blockOutlineWidth);
    }

    @Redirect(method = "drawOutlinedBoundingBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/world/Tessellator;setColorOpaque_I(I)V"))
    private void bootifulblockoutline$redirectToBlockColor(Tessellator instance, int color) {
        final int argb = BootifulBlockOutline.CONFIG.blockOutlineMode == 1 ? BootifulBlockOutline.CONFIG.blockOutlineColor : BootifulBlockOutline.getSilverARGB();
        final int alpha = (short) ((argb >> 24) & 0xFF);
        final int red = (short) ((argb >> 16) & 0xFF);
        final int green = (short) ((argb >> 8) & 0xFF);
        final int blue = (short) ((argb) & 0xFF);
        instance.setColorRGBA(red, green, blue, alpha);
    }

    @ModifyVariable(method = "drawOutlinedBoundingBox", at = @At("STORE"), ordinal = 0, argsOnly = true)
    private AxisAlignedBB bootifulblockoutline$modifyAABBOffset(AxisAlignedBB aabb) {
        float offset = (BootifulBlockOutline.CONFIG.blockOutlineWidth / 2.0f) * 0.002f;
        return aabb.expand(offset, offset, offset);
    }

}
