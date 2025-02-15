package com.mc3699.surge.generation.creativeGenerator;

import com.mc3699.surge.ModBlockEntities;
import com.mc3699.surge.base.ElectricalPolarity;
import com.mc3699.surge.base.ElectricalSourceBlockEntity;
import com.mc3699.surge.world.util.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumMap;

public class CreativeGeneratorBlockEntity extends ElectricalSourceBlockEntity implements TickableBlockEntity {
    public CreativeGeneratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(
                ModBlockEntities.CREATIVE_GENERATOR_BLOCK_ENTITY.get(),
                pPos,
                pBlockState
        );

        EnumMap<Direction, ElectricalPolarity> polaritySides = new EnumMap<>(Direction.class);
        polaritySides.put(Direction.NORTH, ElectricalPolarity.POSITIVE);
        polaritySides.put(Direction.SOUTH, ElectricalPolarity.NEGATIVE);

        this.initializePolaritySides(polaritySides);
    }


    @Override
    public void tick() {

    }
}
