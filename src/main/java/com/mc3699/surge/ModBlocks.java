package com.mc3699.surge;

import com.mc3699.surge.base.ElectricalPolarity;
import com.mc3699.surge.generation.creativeGenerator.CreativeGeneratorBlock;
import com.mc3699.surge.generation.creativeGenerator.CreativeGeneratorBlockEntity;
import com.mc3699.surge.transfer.basicWire.BasicWireBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Surge.MODID);


    public static final RegistryObject<Block> CREATIVE_GENERATOR_BLOCK =
            registerBlock("creative_generator", CreativeGeneratorBlock::new);

    public static final RegistryObject<Block> BASIC_WIRE_BLOCK =
            registerBlock("wire", BasicWireBlock::new);




    public static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name,block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }


}
