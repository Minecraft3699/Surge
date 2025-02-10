package com.mc3699.surge.base;

import com.mc3699.surge.Surge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Surge.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CapabilityRegistry {

    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event)
    {
        event.register(IElectricalLogic.class);
    }

}
