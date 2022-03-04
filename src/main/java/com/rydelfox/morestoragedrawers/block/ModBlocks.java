package com.rydelfox.morestoragedrawers.block;

import com.jaquadro.minecraft.storagedrawers.block.BlockDrawers;
import com.jaquadro.minecraft.storagedrawers.block.BlockStandardDrawers;
import com.jaquadro.minecraft.storagedrawers.block.BlockTrim;
//import com.jaquadro.minecraft.storagedrawers.client.renderer.TileEntityDrawersRenderer;
import com.jaquadro.minecraft.storagedrawers.item.ItemDrawers;
import com.rydelfox.morestoragedrawers.block.tile.TileEntityDrawersMore;
import com.rydelfox.morestoragedrawers.client.renderer.TileEntityDrawersRenderer;
import com.rydelfox.morestoragedrawers.MoreCreative;
import com.rydelfox.morestoragedrawers.MoreStorageDrawers;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.*;
import java.util.function.Supplier;

public class ModBlocks {

    public static Map<EnumVariant, BlockTrim> moreTrims;
    public static Map<EnumVariant, BlockStandardDrawers> fullOne;
    public static Map<EnumVariant, BlockStandardDrawers> fullTwo;
    public static Map<EnumVariant, BlockStandardDrawers> fullFour;
    public static Map<EnumVariant, BlockStandardDrawers> halfOne;
    public static Map<EnumVariant, BlockStandardDrawers> halfTwo;
    public static Map<EnumVariant, BlockStandardDrawers> halfFour;

    @ObjectHolder(MoreStorageDrawers.MOD_ID)
    public static final class Tile {
        public static final TileEntityType<TileEntityDrawersMore> STANDARD_DRAWERS_1 = null;
        public static final TileEntityType<TileEntityDrawersMore> STANDARD_DRAWERS_2 = null;
        public static final TileEntityType<TileEntityDrawersMore> STANDARD_DRAWERS_4 = null;
    }

    @Mod.EventBusSubscriber(modid = MoreStorageDrawers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {

        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            //IForgeRegistry<Block> r = event.getRegistry();
            AbstractBlock.Properties properties = AbstractBlock.Properties
                    .of(Material.WOOD)
                    .harvestTool(ToolType.AXE)
                    .strength(5f)
                    .isSuffocating(Registration::predFalse)
                    .isRedstoneConductor(Registration::predFalse);

            moreTrims = new HashMap<>();
            fullOne = new HashMap<>();
            fullTwo = new HashMap<>();
            fullFour = new HashMap<>();
            halfOne = new HashMap<>();
            halfTwo = new HashMap<>();
            halfFour = new HashMap<>();


            BlockTrim blockTrim;
            BlockStandardDrawers newFullOne, newFullTwo, newFullFour, newHalfOne, newHalfTwo, newHalfFour;

            for (EnumVariant variant : EnumVariant.values()) {
                MoreStorageDrawers.logInfo("MoreStorageDrawers: Attempting to load " + variant.getNamespace() + " Drawers");
                // Loop over each drawer variant and create each drawer type
                if (variant.getMod() != null && variant.getMod().isLoaded()) {
                    MoreStorageDrawers.logInfo("MoreStorageDrawers: "+variant.getNamespace() + " loaded");
                    /*
                    blockTrim = new BlockTrim(properties);
                    newFullOne = new BlockStandardDrawers(1, false, properties);
                    newFullTwo = new BlockStandardDrawers(2, false, properties);
                    newFullFour = new BlockStandardDrawers(4, false, properties);
                    newHalfOne = new BlockStandardDrawers(1, true, properties);
                    newHalfTwo = new BlockStandardDrawers(2, true, properties);
                    newHalfFour = new BlockStandardDrawers(4, true, properties);

                    moreTrims.put(variant, blockTrim);
                    moreTrims.get(variant).setRegistryName(MoreStorageDrawers.MOD_ID, variant.prefix() + "_trim");
                    r.register(moreTrims.get(variant));

                    fullOne.put(variant, newFullOne);
                    fullOne.get(variant).setRegistryName(MoreStorageDrawers.MOD_ID, variant.prefix() + "_full_1");
                    r.register(fullOne.get(variant));

                    fullTwo.put(variant, newFullTwo);
                    fullTwo.get(variant).setRegistryName(MoreStorageDrawers.MOD_ID, variant.prefix() + "_full_2");
                    r.register(fullTwo.get(variant));

                    fullFour.put(variant, newFullFour);
                    fullFour.get(variant).setRegistryName(MoreStorageDrawers.MOD_ID, variant.prefix() + "_full_4");
                    r.register(fullFour.get(variant));

                    halfOne.put(variant, newHalfOne);
                    halfOne.get(variant).setRegistryName(MoreStorageDrawers.MOD_ID, variant.prefix() + "_half_1");
                    r.register(halfOne.get(variant));

                    halfTwo.put(variant, newHalfTwo);
                    halfTwo.get(variant).setRegistryName(MoreStorageDrawers.MOD_ID, variant.prefix() + "_half_2");
                    r.register(halfTwo.get(variant));

                    halfFour.put(variant, newHalfFour);
                    halfFour.get(variant).setRegistryName(MoreStorageDrawers.MOD_ID, variant.prefix() + "_half_4");
                    r.register(halfFour.get(variant));
                    */
                    registerTrimBlock(event, variant, variant.prefix() + "_trim", properties);
                    registerDrawerBlock(event, variant, variant.prefix()+"_full_1", 1, false, properties);
                    registerDrawerBlock(event, variant, variant.prefix()+"_full_2", 2, false, properties);
                    registerDrawerBlock(event, variant, variant.prefix()+"_full_4", 4, false, properties);
                    registerDrawerBlock(event, variant, variant.prefix()+"_half_1", 1, true, properties);
                    registerDrawerBlock(event, variant, variant.prefix()+"_half_2", 2, true, properties);
                    registerDrawerBlock(event, variant, variant.prefix()+"_half_4", 4, true, properties);
                }
            }
            MoreStorageDrawers.logInfo("MoreStorageDrawers: All Drawer blocks loaded");
        }

        @SubscribeEvent
        public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
            MoreStorageDrawers.logInfo("MoreStorageDrawers: Registering Tile Entities");
            List<BlockStandardDrawers> addDrawers = new ArrayList<>(fullOne.values());
            addDrawers.addAll(halfOne.values());
            registerTileEntity(event, "standard_drawers_1", TileEntityDrawersMore.Slot1::new, addDrawers.toArray(new Block[0]));
            for (Block block : addDrawers) {
                MoreStorageDrawers.logInfo("standard_drawers_1 Added "+block.getDescriptionId());
            }
            addDrawers = new ArrayList<>(fullTwo.values());
            addDrawers.addAll(halfTwo.values());
            registerTileEntity(event, "standard_drawers_2", TileEntityDrawersMore.Slot2::new, addDrawers.toArray(new Block[0]));
            for (Block block : addDrawers) {
                MoreStorageDrawers.logInfo("Added "+block.getDescriptionId());
            }
            addDrawers = new ArrayList<>(fullFour.values());
            addDrawers.addAll(halfFour.values());
            registerTileEntity(event, "standard_drawers_4", TileEntityDrawersMore.Slot4::new, addDrawers.toArray(new Block[0]));
            for (Block block : addDrawers) {
                MoreStorageDrawers.logInfo("Added "+block.getDescriptionId());
            }
        }

        private static <T extends TileEntity> void registerTileEntity(RegistryEvent.Register<TileEntityType<?>> event, String name, Supplier<? extends T> factory, Block... blocks) {
            event.getRegistry().register(TileEntityType.Builder.of(factory, blocks)
                    .build(null).setRegistryName(new ResourceLocation(MoreStorageDrawers.MOD_ID, name)));
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            IForgeRegistry<Item> r = event.getRegistry();

            Item.Properties properties = new Item.Properties().tab(MoreCreative.TAB);

            // Loop over each drawer variant and create each drawer type as an item
            for (EnumVariant variant : EnumVariant.values()) {
                if (variant.getMod() != null && variant.getMod().isLoaded()) {
                    r.register(new ItemDrawers(moreTrims.get(variant), properties)
                            .setRegistryName(Objects.requireNonNull(moreTrims.get(variant).getRegistryName())));
                    r.register(new ItemDrawers(fullOne.get(variant), properties)
                            .setRegistryName(Objects.requireNonNull(fullOne.get(variant).getRegistryName())));
                    r.register(new ItemDrawers(fullTwo.get(variant), properties)
                            .setRegistryName(Objects.requireNonNull(fullTwo.get(variant).getRegistryName())));
                    r.register(new ItemDrawers(fullFour.get(variant), properties)
                            .setRegistryName(Objects.requireNonNull(fullFour.get(variant).getRegistryName())));
                    if (variant.getSlabResource() != null) {
                        r.register(new ItemDrawers(halfOne.get(variant), properties)
                                .setRegistryName(Objects.requireNonNull(halfOne.get(variant).getRegistryName())));
                        r.register(new ItemDrawers(halfTwo.get(variant), properties)
                                .setRegistryName(Objects.requireNonNull(halfTwo.get(variant).getRegistryName())));
                        r.register(new ItemDrawers(halfFour.get(variant), properties)
                                .setRegistryName(Objects.requireNonNull(halfFour.get(variant).getRegistryName())));
                    }
                }
            }
            MoreStorageDrawers.logInfo("MoreStorageDrawers: All Drawer items loaded");
        }

        @SubscribeEvent
        @OnlyIn(Dist.CLIENT)
        public static void registerModels (ModelBakeEvent event) {
            /*
            MoreStorageDrawers.logInfo("MoreStorageDrawers: Registering Models, binding Tile Entity Renderer");
            MoreStorageDrawers.logInfo("STANDARD_DRAWERS_1 is "+Tile.STANDARD_DRAWERS_1.getRegistryName().getNamespace()+":"+Tile.STANDARD_DRAWERS_1.getRegistryName().getPath());
            ClientRegistry.bindTileEntityRenderer(Tile.STANDARD_DRAWERS_1, TileEntityDrawersRenderer::new);
            ClientRegistry.bindTileEntityRenderer(Tile.STANDARD_DRAWERS_2, TileEntityDrawersRenderer::new);
            ClientRegistry.bindTileEntityRenderer(Tile.STANDARD_DRAWERS_4, TileEntityDrawersRenderer::new);

             */
            // Check and confirm these are registered
        }

        @OnlyIn(Dist.CLIENT)
        public static void bindRenderTypes() {
            List<BlockStandardDrawers> alldrawers = new ArrayList<>(fullOne.values());
            alldrawers.addAll(fullTwo.values());
            alldrawers.addAll(fullFour.values());
            alldrawers.addAll(halfOne.values());
            alldrawers.addAll(halfTwo.values());
            alldrawers.addAll(halfFour.values());
            for (Block block : alldrawers) {
                if (block instanceof BlockDrawers) {
                    RenderTypeLookup.setRenderLayer(block, RenderType.cutoutMipped());
                }
            }
        }

        private static Block registerDrawerBlock(RegistryEvent.Register<Block> event, EnumVariant variant, String name, int drawerCount, boolean halfDepth, AbstractBlock.Properties properties) {
            BlockStandardDrawers newBlock = new BlockStandardDrawers(drawerCount, halfDepth, properties);
            if (halfDepth) {
                switch(drawerCount) {
                    case 4:
                        halfFour.put(variant, newBlock); break;
                    case 2:
                        halfTwo.put(variant, newBlock); break;
                    case 1:
                    default:
                        halfOne.put(variant, newBlock);
                }
            } else {
                switch(drawerCount) {
                    case 4:
                        fullFour.put(variant, newBlock); break;
                    case 2:
                        fullTwo.put(variant, newBlock); break;
                    case 1:
                    default:
                        fullOne.put(variant, newBlock);
                }
            }
            return registerBlock(event, name, newBlock);
        }

        private static Block registerTrimBlock(RegistryEvent.Register<Block> event, EnumVariant variant, String name, AbstractBlock.Properties properties) {
            BlockTrim newBlock = new BlockTrim(properties);
            moreTrims.put(variant, newBlock);
            return registerBlock(event, name, newBlock);
        }

        private static Block registerBlock(RegistryEvent.Register<Block> event, String name, Block block) {
            block.setRegistryName(MoreStorageDrawers.MOD_ID, name);
            event.getRegistry().register(block);
            MoreStorageDrawers.logInfo("MoreStorageDrawers: Registered "+name);
            return block;
        }

        private static boolean predFalse(BlockState blockState, IBlockReader reader, BlockPos pos) {
            return false;
        }
    }


}
