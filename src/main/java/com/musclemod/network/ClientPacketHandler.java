package com.musclemod.network;

import com.musclemod.common.MuscleCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientPacketHandler {

    public static void handleSync(int entityId, boolean isMuscle) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        Entity entity = mc.level.getEntity(entityId);
        if (entity instanceof Player player) {
            MuscleCapabilityProvider.setMuscle(player, isMuscle);
        }
    }
}
