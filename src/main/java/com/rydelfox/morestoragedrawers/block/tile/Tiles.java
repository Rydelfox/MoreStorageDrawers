package com.rydelfox.morestoragedrawers.block.tile;

import com.rydelfox.morestoragedrawers.MoreStorageDrawers;
import com.rydelfox.morestoragedrawers.block.BlockMoreDrawers;
import com.rydelfox.morestoragedrawers.block.DrawerMaterial;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class Tiles {
    public static BlockEntityType<TileEntityDrawersMore.Slot1> TILE_DRAWERS_1 = null;
    public static BlockEntityType<TileEntityDrawersMore.Slot2> TILE_DRAWERS_2 = null;
    public static BlockEntityType<TileEntityDrawersMore.Slot4> TILE_DRAWERS_4 = null;

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MoreStorageDrawers.MOD_ID);

    public static RegistryObject<BlockEntityType<? extends TileEntityDrawersMore>> MORE_DRAWERS_1 = BLOCK_ENTITY_TYPES.register("more_drawers_1", () -> TILE_DRAWERS_1);
    public static RegistryObject<BlockEntityType<? extends TileEntityDrawersMore>> MORE_DRAWERS_2 = BLOCK_ENTITY_TYPES.register("more_drawers_2", () -> TILE_DRAWERS_2);
    public static RegistryObject<BlockEntityType<? extends TileEntityDrawersMore>> MORE_DRAWERS_4 = BLOCK_ENTITY_TYPES.register("more_drawers_4", () -> TILE_DRAWERS_4);

    public static void initializeTiles() {
        MoreStorageDrawers.logInfo("MoreStorageDrawers: Registering Tile Entities");
        buildDrawers();
        if (TILE_DRAWERS_1 == null)
            MoreStorageDrawers.logInfo("Failed to build 1 slot tiles!");
        if (TILE_DRAWERS_2 == null)
            MoreStorageDrawers.logInfo("Failed to build 2 slot tiles!");
        if (TILE_DRAWERS_4 == null)
            MoreStorageDrawers.logInfo("Failed to build 4 slot tiles!");
    }

    public static void buildDrawers() {
        MoreStorageDrawers.logInfo("In buildDrawers");
        List<BlockMoreDrawers> oneDrawers = new ArrayList<>();
        List<BlockMoreDrawers> twoDrawers = new ArrayList<>();
        List<BlockMoreDrawers> fourDrawers = new ArrayList<>();
        for (DrawerMaterial material : DrawerMaterial.values()) {
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
        TILE_DRAWERS_1 = (BlockEntityType.Builder.of((TileEntityDrawersMore.Slot1::new), oneDrawers.stream().toArray(BlockMoreDrawers[]::new)).build(null));
        TILE_DRAWERS_2 = (BlockEntityType.Builder.of((TileEntityDrawersMore.Slot2::new), twoDrawers.stream().toArray(BlockMoreDrawers[]::new)).build(null));
        TILE_DRAWERS_4 = (BlockEntityType.Builder.of((TileEntityDrawersMore.Slot4::new), fourDrawers.stream().toArray(BlockMoreDrawers[]::new)).build(null));
    }
}