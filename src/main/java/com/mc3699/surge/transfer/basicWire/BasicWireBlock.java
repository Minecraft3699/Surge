package com.mc3699.surge.transfer.basicWire;

import com.mc3699.surge.world.util.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BasicWireBlock extends Block implements EntityBlock {
    public BasicWireBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.METAL));
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BasicWireBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return TickableBlockEntity.getTickerHelper(pLevel);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide())
        {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof BasicWireBlockEntity wireBlockEntity)
            {
                wireBlockEntity.electricalLogic.setVoltage(wireBlockEntity.electricalLogic.getVoltage() + 100);
                wireBlockEntity.electricalLogic.setCurrent(wireBlockEntity.electricalLogic.getCurrent() + 10);
            }

            return InteractionResult.SUCCESS;
        }


        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }
}
