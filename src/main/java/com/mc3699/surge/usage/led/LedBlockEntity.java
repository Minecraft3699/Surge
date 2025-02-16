package com.mc3699.surge.usage.led;

import com.mc3699.surge.ModBlockEntities;
import com.mc3699.surge.base.CircuitPart;
import com.mc3699.surge.base.ElectricalConsumerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LedBlockEntity extends ElectricalConsumerBlockEntity implements CircuitPart {
    public LedBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.LED_BLOCK_ENTITY.get(), pPos, pBlockState);
        // TODO: polarities
    }

    @Override
    public double getResistance() {
        return 1;
    }

    public void power(double voltage, double amperage) {
        if (voltage > 3.5) {
            // TODO: fucking explode
        } else if (voltage > 0.7) {
            // TODO: check if this works properly
            BlockState state = level.getBlockState(getBlockPos());
            level.setBlockAndUpdate(
                    getBlockPos(),
                    state.setValue(LedBlock.LIGHT_EMISSION,
                            Math.max(
                                    0, Math.min((int) Math.round(15*amperage), 1)
                            )
                    )
            );
        }
    }
}
