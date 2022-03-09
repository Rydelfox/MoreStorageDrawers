package com.rydelfox.morestoragedrawers.block.tile;

import com.google.common.collect.ImmutableSet;
import com.rydelfox.morestoragedrawers.MoreStorageDrawers;
import com.rydelfox.morestoragedrawers.block.BlockMoreDrawers;
import com.rydelfox.morestoragedrawers.block.DrawerMaterial;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tiles {
    @ObjectHolder(MoreStorageDrawers.MOD_ID)
    public static final class Tile {
        public static final TileEntityType<TileEntityDrawersMore> MORE_DRAWERS_1 = null;
        public static final TileEntityType<TileEntityDrawersMore> MORE_DRAWERS_2 = null;
        public static final TileEntityType<TileEntityDrawersMore> MORE_DRAWERS_4 = null;
    }

    public static TileEntityType<TileEntityDrawersMore.Slot1> TILE_DRAWERS_1 = null;
    public static TileEntityType<TileEntityDrawersMore.Slot2> TILE_DRAWERS_2 = null;
    public static TileEntityType<TileEntityDrawersMore.Slot4> TILE_DRAWERS_4 = null;

    public static void buildDrawers() {
        MoreStorageDrawers.logInfo("In buildDrawers");
        List<BlockMoreDrawers> oneDrawers = new ArrayList<>();
        List<BlockMoreDrawers> twoDrawers = new ArrayList<>();
        List<BlockMoreDrawers> fourDrawers = new ArrayList<>();
        for(DrawerMaterial material : DrawerMaterial.values()) {
            if (material.getDrawer(1, false) != null)
                oneDrawers.add(material.getDrawer(1, false));
            if (material.getDrawer(1, true) != null)
                oneDrawers.add(material.getDrawer(1, true));
            if (material.getDrawer(2, false) != null)
                twoDrawers.add(material.getDrawer(2, false));
            if (material.getDrawer(2, true) != null)
                twoDrawers.add(material.getDrawer(2, true));
            if (material.getDrawer(4, false) != null)
                fourDrawers.add(material.getDrawer(4, false));
            if (material.getDrawer(4, true) != null)
                fourDrawers.add(material.getDrawer(4, true));
        }
        TILE_DRAWERS_1 = (TileEntityType.Builder.of((TileEntityDrawersMore.Slot1::new), oneDrawers.stream().toArray(BlockMoreDrawers[]::new)).build(null));
        TILE_DRAWERS_2 = (TileEntityType.Builder.of((TileEntityDrawersMore.Slot2::new), twoDrawers.stream().toArray(BlockMoreDrawers[]::new)).build(null));
        TILE_DRAWERS_4 = (TileEntityType.Builder.of((TileEntityDrawersMore.Slot4::new), fourDrawers.stream().toArray(BlockMoreDrawers[]::new)).build(null));
        TILE_DRAWERS_1.setRegistryName(new ResourceLocation(MoreStorageDrawers.MOD_ID, "more_drawers_1"));
        TILE_DRAWERS_2.setRegistryName(new ResourceLocation(MoreStorageDrawers.MOD_ID, "more_drawers_2"));
        TILE_DRAWERS_4.setRegistryName(new ResourceLocation(MoreStorageDrawers.MOD_ID, "more_drawers_4"));
    }

    public static void registerTiles(IForgeRegistry<TileEntityType<?>> registry) {
        buildDrawers();
        if (TILE_DRAWERS_1 == null)
            MoreStorageDrawers.logInfo("Failed to build 1 slot tiles!");
        if (TILE_DRAWERS_2 == null)
            MoreStorageDrawers.logInfo("Failed to build 2 slot tiles!");
        if (TILE_DRAWERS_4 == null)
            MoreStorageDrawers.logInfo("Failed to build 4 slot tiles!");
        registry.register(TILE_DRAWERS_1);
        registry.register(TILE_DRAWERS_2);
        registry.register(TILE_DRAWERS_4);
    }
}
