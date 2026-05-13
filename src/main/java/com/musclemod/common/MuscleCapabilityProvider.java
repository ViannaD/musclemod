package com.musclemod.common;

import com.musclemod.MuscleMod;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = MuscleMod.MOD_ID)
public class MuscleCapabilityProvider implements ICapabilitySerializable<CompoundTag> {

    public static final Capability<MuscleCapability> MUSCLE_CAP = CapabilityManager.get(new CapabilityToken<>() {});
    public static final ResourceLocation CAP_ID = new ResourceLocation(MuscleMod.MOD_ID, "muscle_cap");

    private final MuscleCapability instance = new MuscleCapability();
    private final LazyOptional<MuscleCapability> optional = LazyOptional.of(() -> instance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == MUSCLE_CAP ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return instance.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        instance.deserializeNBT(nbt);
    }

    public void invalidate() {
        optional.invalidate();
    }

    // ---- Static helpers ----

    public static boolean isMuscle(Player player) {
        return player.getCapability(MUSCLE_CAP)
                .map(MuscleCapability::isMuscle)
                .orElse(false);
    }

    public static void setMuscle(Player player, boolean value) {
        player.getCapability(MUSCLE_CAP).ifPresent(cap -> cap.setMuscle(value));
    }

    // ---- Events ----

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            MuscleCapabilityProvider provider = new MuscleCapabilityProvider();
            event.addCapability(CAP_ID, provider);
            event.addListener(provider::invalidate);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();
        boolean wasMuscle = isMuscle(event.getOriginal());
        event.getOriginal().invalidateCaps();
        setMuscle(event.getEntity(), wasMuscle);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        // state already cloned above
    }
}
