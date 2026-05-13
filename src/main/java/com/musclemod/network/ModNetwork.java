package com.musclemod.network;

import com.musclemod.MuscleMod;
import com.musclemod.common.MuscleCapabilityProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class ModNetwork {

    private static final String PROTOCOL = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MuscleMod.MOD_ID, "main"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals
    );

    private static int id = 0;

    public static void register() {
        // Server -> Client: sync muscle state
        CHANNEL.registerMessage(id++, SyncMusclePacket.class,
                SyncMusclePacket::encode,
                SyncMusclePacket::decode,
                SyncMusclePacket::handle,
                java.util.Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    public static void sendToAllTracking(ServerPlayer player) {
        boolean isMuscle = MuscleCapabilityProvider.isMuscle(player);
        SyncMusclePacket packet = new SyncMusclePacket(player.getId(), isMuscle);
        // Send to the player themselves
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
        // Send to everyone tracking this player
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> player), packet);
    }

    // ---- Inner packet class ----

    public static class SyncMusclePacket {
        private final int entityId;
        private final boolean isMuscle;

        public SyncMusclePacket(int entityId, boolean isMuscle) {
            this.entityId = entityId;
            this.isMuscle = isMuscle;
        }

        public static void encode(SyncMusclePacket packet, FriendlyByteBuf buf) {
            buf.writeInt(packet.entityId);
            buf.writeBoolean(packet.isMuscle);
        }

        public static SyncMusclePacket decode(FriendlyByteBuf buf) {
            return new SyncMusclePacket(buf.readInt(), buf.readBoolean());
        }

        public static void handle(SyncMusclePacket packet, Supplier<NetworkEvent.Context> ctxSupplier) {
            NetworkEvent.Context ctx = ctxSupplier.get();
            ctx.enqueueWork(() -> ClientPacketHandler.handleSync(packet.entityId, packet.isMuscle));
            ctx.setPacketHandled(true);
        }
    }
}
