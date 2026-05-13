package com.musclemod.common;

import com.musclemod.MuscleMod;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MuscleMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CapabilityRegistration {

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(MuscleCapability.class);
    }
}
