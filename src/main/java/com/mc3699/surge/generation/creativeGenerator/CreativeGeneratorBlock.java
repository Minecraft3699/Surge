package com.mc3699.surge.generation.creativeGenerator;

import com.mc3699.surge.base.ElectricalBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class CreativeGeneratorBlock extends Block implements EntityBlock {
    public CreativeGeneratorBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.METAL));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CreativeGeneratorBlockEntity(blockPos, blockState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        if(!pLevel.isClientSide())
        {
            CreativeGeneratorBlockEntity blockEntity = (CreativeGeneratorBlockEntity) pLevel.getBlockEntity(pPos);
            blockEntity.setAmperage(blockEntity.getAmperage() + 0.5f);
            blockEntity.setVoltage(blockEntity.getVoltage() + 0.5f);
            blockEntity.setResistance(blockEntity.getResistance() + 0.5f);
            return InteractionResult.SUCCESS;
        }


        return InteractionResult.FAIL;
    }
}
