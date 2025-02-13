package com.mc3699.surge.generation.creativeGenerator;

import com.mc3699.surge.ModBlockEntities;
import com.mc3699.surge.base.ElectricalBlockEntity;
import com.mc3699.surge.base.ElectricalPolarity;
import com.mc3699.surge.world.util.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CreativeGeneratorBlockEntity extends ElectricalBlockEntity implements TickableBlockEntity {
    public CreativeGeneratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CREATIVE_GENERATOR_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.setPolarity(Direction.NORTH, ElectricalPolarity.POSITIVE);
        this.setPolarity(Direction.SOUTH, ElectricalPolarity.NEGATIVE);
    }


    @Override
    public void tick() {

    }
}
