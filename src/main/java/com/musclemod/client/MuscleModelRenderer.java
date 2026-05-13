package com.musclemod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

/**
 * Hand-built renderer for the muscle model described in muscle_geo.json.
 * All UV coordinates and cube sizes come directly from the JSON.
 * Texture size: 64x64.
 *
 * The model is rendered on top of the player with the player's own skin,
 * replacing the visual shape while keeping the original skin texture.
 */
@OnlyIn(Dist.CLIENT)
public class MuscleModelRenderer {

    private final ModelPart root;

    public MuscleModelRenderer() {
        root = buildModel().bakeRoot();
    }

    private static LayerDefinition buildModel() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // ---- Waist (no visual, just pivot anchor) ----
        PartDefinition waist = root.addOrReplaceChild("waist",
                CubeListBuilder.create(),
                PartPose.offset(0, 12, 0));

        // ---- Head (pivot at 0,24,0 relative to waist => offset from waist = 0,12,0) ----
        PartDefinition head = waist.addOrReplaceChild("head",
                CubeListBuilder.create()
                        // inner cube  origin[-4,24,-4] relative to world -> relative to pivot(0,24,0): -4,0,-4
                        .texOffs(8, 8).addBox(-4, 0, -4, 8, 8, 8)
                        // outer (hat) inflate 0.5
                        .texOffs(40, 8).addBox(-4.5f, -0.5f, -4.5f, 9, 9, 9),
                PartPose.offset(0, 12, 0)); // pivot relative to waist

        // ---- Body ----
        PartDefinition body = waist.addOrReplaceChild("body",
                CubeListBuilder.create()
                        // lower torso  origin[-4,12,-2] -> relative to pivot(0,24): -4,-12,-2
                        .texOffs(20, 26).addBox(-4, -12, -2, 8, 6, 4)
                        // upper torso  origin[-4.5,18,-2.5] -> -4.5,-6,-2.5
                        .texOffs(20, 20).addBox(-4.5f, -6, -2.5f, 9, 6, 5)
                        // lower torso overlay inflate 0.25
                        .texOffs(20, 42).addBox(-4.25f, -12.25f, -2.25f, 8, 6, 4)
                        // upper torso overlay inflate 0.25
                        .texOffs(20, 36).addBox(-4.75f, -5.75f, -2.75f, 9, 6, 5),
                PartPose.offset(0, 12, 0)); // pivot relative to waist (same as head)

        // ---- Right Arm  pivot[-5,22,0] relative to waist, rotation z=+5° ----
        PartDefinition rightArm = waist.addOrReplaceChild("rightArm",
                CubeListBuilder.create()
                        // forearm  origin[-8,12,-2] -> relative to pivot[-5,22,0]: -3,-10,-2
                        .texOffs(44, 25).addBox(-3, -10, -2, 4, 7, 4)
                        // shoulder  origin[-8.5,18.9,-2.6] -> -3.5,-3.1,-2.6
                        .texOffs(44, 20).addBox(-3.5f, -3.1f, -2.6f, 5, 5, 5)
                        // forearm overlay
                        .texOffs(44, 41).addBox(-3.25f, -10.25f, -2.25f, 4, 7, 4)
                        // shoulder overlay
                        .texOffs(44, 36).addBox(-3.75f, -2.85f, -2.85f, 5, 5, 5),
                PartPose.offsetAndRotation(-5, 10, 0, 0, 0, (float) Math.toRadians(5)));

        // ---- Left Arm  pivot[5,22,0] relative to waist, rotation z=-5° ----
        PartDefinition leftArm = waist.addOrReplaceChild("leftArm",
                CubeListBuilder.create()
                        .texOffs(40, 57).addBox(-1, -10, -2, 4, 7, 4)
                        .texOffs(40, 52).addBox(-1.5f, -3.1f, -2.6f, 5, 5, 5)
                        .texOffs(56, 57).addBox(-1.25f, -10.25f, -2.25f, 4, 7, 4)
                        .texOffs(56, 52).addBox(-1.75f, -2.85f, -2.85f, 5, 5, 5),
                PartPose.offsetAndRotation(5, 10, 0, 0, 0, (float) Math.toRadians(-5)));

        // ---- Right Leg  pivot[-1.9,12,0] absolute -> offset from root ----
        PartDefinition rightLeg = root.addOrReplaceChild("rightLeg",
                CubeListBuilder.create()
                        // origin[-3.4,0,-1.75] relative to pivot[-1.9,12,0]: -1.5,-12,-1.75
                        .texOffs(4, 20).addBox(-1.5f, -12, -1.75f, 3, 12, 3)
                        // overlay inflate 0.25
                        .texOffs(4, 36).addBox(-1.75f, -12.25f, -2f, 3, 12, 3),
                PartPose.offsetAndRotation(-1.9f, 12, 0, 0, 0, (float) Math.toRadians(2.5)));

        // ---- Left Leg  pivot[1.9,12,0] ----
        PartDefinition leftLeg = root.addOrReplaceChild("leftLeg",
                CubeListBuilder.create()
                        // origin[0.4,0,-1.75] relative to pivot[1.9,12]: -1.5,-12,-1.75
                        .texOffs(20, 52).addBox(-1.5f, -12, -1.75f, 3, 12, 3)
                        // overlay
                        .texOffs(4, 52).addBox(-1.75f, -12.25f, -2f, 3, 12, 3),
                PartPose.offsetAndRotation(1.9f, 12, 0, 0, 0, (float) Math.toRadians(-2.5)));

        return LayerDefinition.create(mesh, 64, 64);
    }

    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                       Player player, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch,
                       ResourceLocation skinTexture) {

        VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucent(skinTexture));

        poseStack.pushPose();

        // Standard player Y offset so feet touch the ground
        poseStack.translate(0, 1.501f, 0);
        poseStack.scale(-1, -1, 1);

        // --- Animate limbs mirroring vanilla player ---
        ModelPart waistPart   = root.getChild("waist");
        ModelPart headPart    = waistPart.getChild("head");
        ModelPart bodyPart    = waistPart.getChild("body");
        ModelPart rightArmPart = waistPart.getChild("rightArm");
        ModelPart leftArmPart  = waistPart.getChild("leftArm");
        ModelPart rightLegPart = root.getChild("rightLeg");
        ModelPart leftLegPart  = root.getChild("leftLeg");

        // Head rotation
        headPart.xRot = headPitch * Mth.DEG_TO_RAD;
        headPart.yRot = netHeadYaw * Mth.DEG_TO_RAD;

        // Leg swing
        rightLegPart.xRot = Mth.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount;
        leftLegPart.xRot  = Mth.cos(limbSwing * 0.6662f + (float) Math.PI) * 1.4f * limbSwingAmount;

        // Arm swing
        rightArmPart.xRot = Mth.cos(limbSwing * 0.6662f + (float) Math.PI) * 1.4f * limbSwingAmount;
        leftArmPart.xRot  = Mth.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount;

        root.render(poseStack, vc, packedLight, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
    }
}
