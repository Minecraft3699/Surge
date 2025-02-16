package com.mc3699.surge.usage.led;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class LedBlock extends Block implements EntityBlock {
    public static final IntegerProperty LIGHT_EMISSION = IntegerProperty.create(
            "light_emission",
            0,
            15
    );

    public LedBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.GLASS));
        this.registerDefaultState(this.stateDefinition.any().setValue(LIGHT_EMISSION, 0));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new LedBlockEntity(blockPos, blockState);
    }
}
