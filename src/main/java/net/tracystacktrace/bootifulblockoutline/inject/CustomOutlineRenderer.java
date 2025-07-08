package net.tracystacktrace.bootifulblockoutline.inject;

import com.indigo3d.util.RenderSystem;
import net.minecraft.client.renderer.world.Tessellator;
import net.minecraft.common.entity.player.EntityPlayer;
import net.minecraft.common.util.math.MathHelper;
import net.minecraft.common.util.physics.MovingObjectPosition;
import net.tracystacktrace.bootifulblockoutline.BootifulBlockOutline;
import org.lwjgl.opengl.GL11;

/**
 * Slightly (heavily) modified entity outline renderer from game code that
 * respects mod's config values
 * <br>
 * I am not interested in crazy mixining the code so let this be there
 */
public class CustomOutlineRenderer {
    public static void renderEntityOutline(EntityPlayer player, MovingObjectPosition hitResult, float deltaTicks) {
        final double x = MathHelper.lerp_d(deltaTicks, player.lastTickPosX, player.posX);
        final double y = MathHelper.lerp_d(deltaTicks, player.lastTickPosY, player.posY);
        final double z = MathHelper.lerp_d(deltaTicks, player.lastTickPosZ, player.posZ);

        final int color = (BootifulBlockOutline.CONFIG.entityOutlineMode == 1) ? BootifulBlockOutline.CONFIG.entityOutlineColor : BootifulBlockOutline.getSilverARGB();

        final boolean wasFogEnabled = RenderSystem.isFogEnabled();
        if (wasFogEnabled) RenderSystem.disableFog();

        //make it render correctly
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderSystem.color(0.0F, 0.0F, 0.0F, 0.4F);
        GL11.glLineWidth(BootifulBlockOutline.CONFIG.entityOutlineWidth);
        RenderSystem.disableTexture2D();
        RenderSystem.disableDepthMask();

        //render the outline box
        Tessellator.drawOutlinedBoundingBoxStatic(
                hitResult.entityHit.boundingBox
                        .expand(
                                hitResult.entityHit.rotationYaw != 0.0F && hitResult.entityHit.rotationYaw != 180.0F ? 0.006 : 0.002,
                                0.001,
                                hitResult.entityHit.rotationYaw != 90.0F && hitResult.entityHit.rotationYaw != 270.0F ? 0.006 : 0.002
                        ).getOffsetBoundingBox(-x, -y, -z),
                color, true
        );

        //restore render features
        RenderSystem.enableDepthMask();
        RenderSystem.enableTexture2D();
        RenderSystem.disableBlend();
        if (wasFogEnabled) RenderSystem.enableFog();
    }
}
