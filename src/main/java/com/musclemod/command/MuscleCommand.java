package com.musclemod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.musclemod.common.MuscleCapabilityProvider;
import com.musclemod.network.ModNetwork;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class MuscleCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(Commands.literal("muscle")
                .executes(ctx -> {
                    CommandSourceStack source = ctx.getSource();
                    if (!(source.getEntity() instanceof ServerPlayer player)) {
                        source.sendFailure(Component.literal("Este comando só pode ser usado por jogadores."));
                        return 0;
                    }
                    MuscleCapabilityProvider.setMuscle(player, true);
                    ModNetwork.sendToAllTracking(player);
                    player.sendSystemMessage(Component.literal("§aModo Muscle ativado! 💪"));
                    return 1;
                })
        );

        dispatcher.register(Commands.literal("normal")
                .executes(ctx -> {
                    CommandSourceStack source = ctx.getSource();
                    if (!(source.getEntity() instanceof ServerPlayer player)) {
                        source.sendFailure(Component.literal("Este comando só pode ser usado por jogadores."));
                        return 0;
                    }
                    MuscleCapabilityProvider.setMuscle(player, false);
                    ModNetwork.sendToAllTracking(player);
                    player.sendSystemMessage(Component.literal("§eFormato normal restaurado."));
                    return 1;
                })
        );
    }
}
