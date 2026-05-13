package com.musclemod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.musclemod.common.MuscleCapabilityProvider;
import com.musclemod.MuscleMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

/**
 * Render layer added to the PlayerRenderer.
 * When active it draws the muscle model using the player's own skin texture,
 * so it works with any custom skin.
 */
@OnlyIn(Dist.CLIENT)
public class MuscleRenderLayer extends RenderLayer<Player, net.minecraft.client.model.PlayerModel<Player>> {

    private final MuscleModelRenderer muscleRenderer = new MuscleModelRenderer();

    public MuscleRenderLayer(PlayerRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                       Player player, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {

        if (!MuscleCapabilityProvider.isMuscle(player)) return;

        // Get the player's actual skin texture (works with any skin/cape)
        ResourceLocation skinTexture = Minecraft.getInstance()
                .getSkinManager()
                .getInsecureSkin(player.getGameProfile())
                .texture();

        muscleRenderer.render(poseStack, buffer, packedLight, player,
                limbSwing, limbSwingAmount, partialTick, ageInTicks,
                netHeadYaw, headPitch, skinTexture);
    }
}
