package com.mc3699.surge.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumMap;
import java.util.Map;

// class for all electrical parts BUT wires
public class ElectricalBlockEntity extends BlockEntity {
    public ElectricalBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        initializePolarity();
    }

    private final Map<Direction, ElectricalPolarity> polaritySides = new EnumMap<>(Direction.class);
    public final ElectricalLogic electricalLogic = new ElectricalLogic(120, 40, 8);


    private void initializePolarity()
    {
        polaritySides.put(Direction.NORTH, ElectricalPolarity.NEUTRAL);
        polaritySides.put(Direction.SOUTH, ElectricalPolarity.NEUTRAL);
        polaritySides.put(Direction.EAST, ElectricalPolarity.NEUTRAL);
        polaritySides.put(Direction.WEST, ElectricalPolarity.NEUTRAL);
        polaritySides.put(Direction.UP, ElectricalPolarity.NEUTRAL);
        polaritySides.put(Direction.DOWN, ElectricalPolarity.NEUTRAL);
    }

    public void setPolarity(Direction direction, ElectricalPolarity polarity)
    {
        polaritySides.put(direction, polarity);
        setChanged();
    }

    public ElectricalPolarity getPolarity(Direction direction)
    {
        return polaritySides.getOrDefault(direction, ElectricalPolarity.NEUTRAL);
    }

    public boolean isCompletelyNeutral() {
        for (Direction dir : Direction.values())
        {
            if (polaritySides.get(dir) != ElectricalPolarity.NEUTRAL) return false;
        }
        return true;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        // Polarity
        CompoundTag polarityTag = new CompoundTag();
        for (Direction direction : Direction.values())
        {
            polarityTag.putString(direction.getName(), polaritySides.getOrDefault(
                    direction,
                    ElectricalPolarity.NEUTRAL
            ).name());
        }

        pTag.put("polarity", polarityTag);

        // V/A/R

        pTag.putFloat("voltage", electricalLogic.getVoltage());
        pTag.putFloat("amperage", electricalLogic.getAmperage());
        pTag.putFloat("resistance", electricalLogic.getResistance());

    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if(pTag.contains("polarity", Tag.TAG_COMPOUND))
        {
            CompoundTag polarityTag = pTag.getCompound("polarity");
            for (Direction dir : Direction.values())
            {
                polaritySides.put(dir, ElectricalPolarity.valueOf(polarityTag.getString(dir.getName())));
            }
        }

        if(pTag.contains("voltage")) {
            electricalLogic.setVoltage(pTag.getFloat("voltage"));
        }

        if(pTag.contains("amperage")) {
            electricalLogic.setAmperage(pTag.getFloat("amperage"));
        }

        if(pTag.contains("resistance")) {
            electricalLogic.setResistance(pTag.getFloat("resistance"));
        }

    }

    public static ElectricalPolarity oppositeof(ElectricalPolarity polarity) {
        if (polarity == ElectricalPolarity.POSITIVE) {
            return ElectricalPolarity.NEGATIVE;
        } else if (polarity == ElectricalPolarity.NEGATIVE) {
            return ElectricalPolarity.POSITIVE;
        } else {
            return ElectricalPolarity.NEUTRAL;
        }
    }
}
