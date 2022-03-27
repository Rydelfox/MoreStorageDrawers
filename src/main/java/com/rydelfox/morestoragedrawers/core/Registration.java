package com.rydelfox.morestoragedrawers.core;

import com.rydelfox.morestoragedrawers.MoreStorageDrawers;
import com.rydelfox.morestoragedrawers.block.BlockDrawersExtended;
import com.rydelfox.morestoragedrawers.block.DrawerMaterial;
import com.rydelfox.morestoragedrawers.block.tile.Tiles;
import com.rydelfox.morestoragedrawers.datagen.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.rydelfox.morestoragedrawers.MoreStorageDrawers.MOD_ID;
import static com.rydelfox.morestoragedrawers.MoreStorageDrawers.logInfo;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registration {
    @SubscribeEvent
    public static void onBlockRegistry(RegistryEvent.Register<Block> event) {
        logInfo("MoreStorageDrawers: Registering Blocks");
        for (DrawerMaterial material : DrawerMaterial.values()) {
            if (material.getMod().isLoaded())
                material.registerBlocks(event.getRegistry());
        }
    }

    @SubscribeEvent
    public static void onItemRegistry(RegistryEvent.Register<Item> event) {
        logInfo("MoreStorageDrawers: Registering Items");
        for (DrawerMaterial material : DrawerMaterial.values()) {
            if (material.getMod().isLoaded())
                material.registerItems(event.getRegistry());
        }
    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        logInfo("MoreStorageDrawers: Running Datagen");
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        if (event.includeServer()) {
            generator.addProvider(new DrawerRecipeProvider(generator));
            generator.addProvider(new DrawerLootTableProvider(generator));
            //generator.addProvider(new DrawerTagsProvider(generator,helper));
        }
        if (event.includeClient()) {
            generator.addProvider(new DrawerBlockStateProvider(generator, helper));
            generator.addProvider(new DrawerItemModelProvider(generator, helper));
        }
        try {
            generator.run();
        } catch (IOException e) {
            logInfo("DataGenerator#run threw an exception");
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<BlockEntityType<?>> event)
    {
        MoreStorageDrawers.logInfo("MoreStorageDrawers: Registering Tile Entities");
        IForgeRegistry<BlockEntityType<?>> r = event.getRegistry();
        /*
        List<BlockMoreDrawers> oneDrawers = new ArrayList<>();
        List<BlockMoreDrawers> twoDrawers = new ArrayList<>();
        List<BlockMoreDrawers> fourDrawers = new ArrayList<>();
        for (DrawerMaterial material : DrawerMaterial.values()) {
            if(material.getDrawer(1, false) == null) {
                MoreStorageDrawers.logInfo("Drawer 1 full empty for "+material.getNamespace());
            }
            oneDrawers.add(material.getDrawer(1, true));
            oneDrawers.add(material.getDrawer(1, false));
            twoDrawers.add(material.getDrawer(2, true));
            twoDrawers.add(material.getDrawer(2, false));
            fourDrawers.add(material.getDrawer(4, true));
            fourDrawers.add(material.getDrawer(4, false));
        }
        MoreStorageDrawers.logInfo("MoreStorageDrawers: Tile Entity Lists created");
        MoreStorageDrawers.logInfo("MoreStorageDrawers: oneDrawers contains:");
        for(BlockMoreDrawers drawer : oneDrawers) {
            MoreStorageDrawers.logInfo(drawer.getDescriptionId());
        }
        registerTileEntity(event, "standard_drawers_1", TileEntityDrawersMore.Slot1::new, oneDrawers.toArray(new Block[0]));
        registerTileEntity(event, "standard_drawers_2", TileEntityDrawersMore.Slot2::new, twoDrawers.toArray(new Block[0]));
        registerTileEntity(event, "standard_drawers_4", TileEntityDrawersMore.Slot4::new, fourDrawers.toArray(new Block[0]));

         */
        Tiles.registerTiles(r);
    }

    @OnlyIn(Dist.CLIENT)
    public static void bindRenderTypes() {
        List<Block> alldrawers = new ArrayList<>();
        for (DrawerMaterial material : DrawerMaterial.values()) {
            alldrawers.addAll(material.getBlocks(false));
        }
        for (Block block : alldrawers) {
            if (block instanceof BlockDrawersExtended) {
                ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutoutMipped());
            }
        }
    }

    private static <T extends BlockEntity> void registerTileEntity(RegistryEvent.Register<BlockEntityType<?>> event, String name, BlockEntityType.BlockEntitySupplier<? extends T> factory, Block... blocks) {
        event.getRegistry().register(BlockEntityType.Builder.of(factory, blocks)
                .build(null).setRegistryName(new ResourceLocation(MoreStorageDrawers.MOD_ID, name)));
    }
}
