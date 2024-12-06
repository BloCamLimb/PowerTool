/*
 * Parts of this Java source file are from GlowCase project, maintained by ModFest team,
 * licensed under CC0-1.0 per its repository.
 * You may find the original code at https://github.com/ModFest/glowcase
 */
package org.teacon.powertool.client.renders.holo_sign;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.joml.Matrix4f;
import org.teacon.powertool.block.entity.BaseHolographicSignBlockEntity;
import org.teacon.powertool.block.entity.CommonHolographicSignBlockEntity;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class HolographicSignBlockEntityRenderer implements BlockEntityRenderer<CommonHolographicSignBlockEntity> {

    private final BlockEntityRenderDispatcher dispatcher;
    private final Font font;

    public HolographicSignBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.font = context.getFont();
        this.dispatcher = context.getBlockEntityRenderDispatcher();
    }

    @Override
    public void render(CommonHolographicSignBlockEntity theSign, float partialTick, PoseStack transform, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        renderInternal(theSign,transform,bufferSource,packedLight,theSign.rotate);
        if(theSign.bidirectional){
            renderInternal(theSign,transform,bufferSource,packedLight,(theSign.rotate+180)%360);
        }
    }
    
    public void renderInternal(CommonHolographicSignBlockEntity theSign, PoseStack transform, MultiBufferSource bufferSource, int packedLight, int rotatedDegree){
        transform.pushPose();
        beforeRender(theSign,transform,dispatcher,rotatedDegree);
        Matrix4f matrix4f = transform.last().pose();
        int bgColor = theSign.bgColorInARGB;
        int yOffset = -theSign.renderedContents.size() / 2 * this.font.lineHeight;
        int fontColor = theSign.colorInARGB;
        int maxWidth = 0;
        for (var text : theSign.contents) {
            int w = this.font.width(text);
            if (w > maxWidth) {
                maxWidth = w;
            }
        }
        var align = theSign.align;
        for (var text : theSign.renderedContents) {
            if (text != null && !text.isEmpty()) {
                int xOffset = switch (align) {
                    case LEFT -> -maxWidth / 2;
                    case CENTER -> -this.font.width(text) / 2;
                    case RIGHT -> maxWidth / 2 - this.font.width(text);
                };
                // FIXME Implement all 3 different shadow types
                this.font.drawInBatch(text, xOffset, yOffset, fontColor, false, matrix4f, bufferSource, Font.DisplayMode.NORMAL, bgColor, packedLight, false);
            }
            yOffset += this.font.lineHeight + 2;
        }
        transform.popPose();
    }
    
    public static void beforeRender(BaseHolographicSignBlockEntity theSign, PoseStack transform, BlockEntityRenderDispatcher dispatcher,int rotatedDegree){
        transform.translate(0.5, 0.5, 0.5);
        if(theSign.lock){
            transform.mulPose(Axis.YP.rotationDegrees(rotatedDegree));
        }
        else {
            transform.mulPose(dispatcher.camera.rotation());
            transform.mulPose(Axis.YP.rotationDegrees(180));
        }
        transform.scale(-0.025F, -0.025F, 0.025F);
        // FIXME Scaling does not work as expected
        transform.scale(theSign.scale, theSign.scale, 1);
        switch (theSign.arrange) {
            case FRONT -> transform.translate(0.0, 0.0, 0.4D);
            case BACK -> transform.translate(0.0, 0.0, -0.4D);
        }
    }
}
