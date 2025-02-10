package com.mc3699.surge.base;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;

public class ElectricalCapability implements ICapabilityProvider {

    private final ElectricalLogic electricalLogic;

    public ElectricalCapability(int maxVoltage, int maxCurrent, int resistance)
    {
        this.electricalLogic = new ElectricalLogic(maxVoltage, maxCurrent, resistance);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        if(capability == ModCapabilities.ELECTRICAL_CAPABILITY)
        {
            return LazyOptional.of(() -> electricalLogic).cast();
        }
        return LazyOptional.empty();
    }
}
