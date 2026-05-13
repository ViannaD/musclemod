package com.musclemod;

import com.musclemod.command.MuscleCommand;
import com.musclemod.network.ModNetwork;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib.GeckoLib;

@Mod(MuscleMod.MOD_ID)
public class MuscleMod {

    public static final String MOD_ID = "musclemod";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public MuscleMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        GeckoLib.initialize();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModNetwork::register);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        MuscleCommand.register(event.getDispatcher());
    }
}
