package com.mc3699.surge.base;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class ModCapabilities {

    public static Capability<ElectricalCapability> ELECTRICAL_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

}
