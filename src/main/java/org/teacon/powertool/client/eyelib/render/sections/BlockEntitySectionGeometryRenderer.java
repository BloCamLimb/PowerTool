package org.teacon.powertool.client.eyelib.render.sections;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.client.event.AddSectionGeometryEvent;

/**
 * @author Argon4W
 */
public interface BlockEntitySectionGeometryRenderer<T extends BlockEntity> {
    void renderSectionGeometry(T blockEntity, AddSectionGeometryEvent.SectionRenderingContext context, PoseStack poseStack, BlockPos pos, BlockPos regionOrigin, int packedLight, MultiBufferSource bufferSource);
}
