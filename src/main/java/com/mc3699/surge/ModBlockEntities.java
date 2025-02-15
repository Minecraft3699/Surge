package com.mc3699.surge;

import com.mc3699.surge.generation.creativeGenerator.CreativeGeneratorBlockEntity;
import com.mc3699.surge.transfer.copperWire.CopperWireBlockEntity;
import com.mc3699.surge.usage.led.LedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Surge.MODID);


    public static final RegistryObject<BlockEntityType<CreativeGeneratorBlockEntity>> CREATIVE_GENERATOR_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("creative_generator_block_entity", () ->
                    BlockEntityType.Builder.of(CreativeGeneratorBlockEntity::new, ModBlocks.CREATIVE_GENERATOR_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<CopperWireBlockEntity>> COPPER_WIRE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("copper_wire_block_entity", () ->
                    BlockEntityType.Builder.of(CopperWireBlockEntity::new, ModBlocks.COPPER_WIRE_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<LedBlockEntity>> LED_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("led_block_entity", () ->
                    BlockEntityType.Builder.of(LedBlockEntity::new, ModBlocks.LED_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus)
    {
        BLOCK_ENTITIES.register(eventBus);
    }
}
