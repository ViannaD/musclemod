package com.musclemod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.musclemod.MuscleMod;
import com.musclemod.common.MuscleCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Intercepts player rendering:
 * - Cancels the vanilla render when muscle mode is active
 * - Draws the muscle model with the player's own skin texture
 */
@Mod.EventBusSubscriber(modid = MuscleMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class PlayerRenderEvents {

    private static final MuscleModelRenderer MUSCLE_RENDERER = new MuscleModelRenderer();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPreRenderPlayer(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();
        if (!MuscleCapabilityProvider.isMuscle(player)) return;

        // Cancel the vanilla player render
        event.setCanceled(true);

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource buffer = event.getMultiBufferSource();
        int packedLight = event.getPackedLight();

        Minecraft mc = Minecraft.getInstance();
        float partialTick = mc.getFrameTime();
        float limbSwing = player.walkAnimation.position(partialTick);
        float limbSwingAmount = player.walkAnimation.speed(partialTick);
        float ageInTicks = player.tickCount + partialTick;
        float netHeadYaw = Mth.rotLerp(partialTick, player.yHeadRotO, player.yHeadRot)
                - Mth.rotLerp(partialTick, player.yBodyRotO, player.yBodyRot);
        float headPitch = Mth.lerp(partialTick, player.xRotO, player.getXRot());

        // Player's own skin (works with any custom skin)
        ResourceLocation skinTexture = mc.getSkinManager()
                .getInsecureSkin(player.getGameProfile())
                .texture();

        MUSCLE_RENDERER.render(poseStack, buffer, packedLight, player,
                limbSwing, limbSwingAmount, partialTick, ageInTicks,
                netHeadYaw, headPitch, skinTexture);
    }
}
