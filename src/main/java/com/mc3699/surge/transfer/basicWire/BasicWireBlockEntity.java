package com.mc3699.surge.transfer.basicWire;

import com.mc3699.surge.ModBlockEntities;
import com.mc3699.surge.Surge;
import com.mc3699.surge.base.ElectricalCapability;
import com.mc3699.surge.base.ElectricalLogic;
import com.mc3699.surge.base.IElectricalLogic;
import com.mc3699.surge.base.ModCapabilities;
import com.mc3699.surge.world.util.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import oshi.util.platform.unix.solaris.KstatUtil;

public class BasicWireBlockEntity extends BlockEntity implements TickableBlockEntity {

    final ElectricalLogic electricalLogic = new ElectricalLogic(120, 40, 8);
    private final LazyOptional<IElectricalLogic> electricalOptional = LazyOptional.of(() -> electricalLogic);

    public BasicWireBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.BASIC_WIRE_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putFloat("voltage", electricalLogic.getVoltage());
        pTag.putFloat("current", electricalLogic.getCurrent());
        pTag.putFloat("resistance", electricalLogic.getResistance());
    }

    @Override
    public void tick() {

        if(level.isClientSide()) return;

        for(Direction dir : Direction.values())
        {
            BlockEntity neighbor = level.getBlockEntity(worldPosition.relative(dir));
            if(neighbor instanceof BasicWireBlockEntity targetWire)
            {
                IElectricalLogic targetLogic = targetWire.electricalLogic;

                float localVoltage = electricalLogic.getVoltage();
                float localCurrent = electricalLogic.getCurrent();
                float localResistance = electricalLogic.getResistance();

                float targetVoltage = targetLogic.getVoltage();
                float targetCurrent = targetLogic.getCurrent();
                float targetResistance = targetLogic.getResistance();

                float totalResist = localResistance + targetResistance;

                if(totalResist == 0) continue;

                float totalCurrent = (localVoltage - targetVoltage) / totalResist;

                float currentActual = Math.min(electricalLogic.getCurrent(), Math.abs(totalCurrent));

                float voltageAdjust = (currentActual * totalResist);

                if(localVoltage > targetVoltage)
                {
                    targetLogic.setCurrent(targetLogic.getCurrent() + currentActual);
                    targetLogic.setVoltage(targetVoltage + voltageAdjust);

                    electricalLogic.setCurrent(electricalLogic.getCurrent() - currentActual);
                    electricalLogic.setVoltage(electricalLogic.getVoltage() - voltageAdjust);
                } else {
                    targetLogic.setCurrent(targetLogic.getCurrent() - currentActual);
                    targetLogic.setVoltage(targetVoltage - voltageAdjust);

                    electricalLogic.setCurrent(electricalLogic.getCurrent() + currentActual);
                    electricalLogic.setVoltage(electricalLogic.getVoltage() + voltageAdjust);
                }

                if(localVoltage > 0)
                {
                    level.setBlock(worldPosition.above(), Blocks.IRON_BLOCK.defaultBlockState(), 3);
                } else {
                    level.setBlock(worldPosition.above(), Blocks.AIR.defaultBlockState(), 3);
                }

            }
        }

    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if(cap == ModCapabilities.ELECTRICAL_CAPABILITY)
        {
            return electricalOptional.cast();
        } else {
            return LazyOptional.empty();
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        electricalOptional.invalidate();
    }
}
