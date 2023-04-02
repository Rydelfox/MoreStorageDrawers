package com.rydelfox.morestoragedrawers.core;

import com.rydelfox.morestoragedrawers.block.BlockDrawersExtended;
import com.rydelfox.morestoragedrawers.block.DrawerMaterial;
import com.rydelfox.morestoragedrawers.block.tile.Tiles;
import com.rydelfox.morestoragedrawers.datagen.DrawerBlockStateProvider;
import com.rydelfox.morestoragedrawers.datagen.DrawerItemModelProvider;
import com.rydelfox.morestoragedrawers.datagen.DrawerLootTableProvider;
import com.rydelfox.morestoragedrawers.datagen.DrawerRecipeProvider;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.rydelfox.morestoragedrawers.MoreStorageDrawers.*;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registration {
    @SubscribeEvent
    public static void onBlockRegistry(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.BLOCKS, (helper) -> {
            logInfo("MoreStorageDrawers: Registering Blocks");
            for (DrawerMaterial material : DrawerMaterial.values()) {
                if (material.getMod().isLoaded())
                    material.registerBlocks(event.getForgeRegistry());
            }
            Tiles.initializeTiles();
        });
    }

    @SubscribeEvent
    public static void onItemRegistry(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.ITEMS, (helper) -> {
            logInfo("MoreStorageDrawers: Registering Items");
            for (DrawerMaterial material : DrawerMaterial.values()) {
                if (material.getMod().isLoaded())
                    material.registerItems(event.getForgeRegistry());
            }
        });
    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        logInfo("MoreStorageDrawers: Running Datagen");
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        if (event.includeServer()) {
            generator.addProvider(true, new DrawerRecipeProvider(generator));
            generator.addProvider(true, new DrawerLootTableProvider(generator));
            //generator.addProvider(new DrawerTagsProvider(generator,helper));
        }
        if (event.includeClient()) {
            generator.addProvider(true, new DrawerBlockStateProvider(generator, helper));
            generator.addProvider(true, new DrawerItemModelProvider(generator, helper));
        }
        try {
            generator.run();
        } catch (IOException e) {
            logInfo("DataGenerator#run threw an exception");
            e.printStackTrace();
        }
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
}