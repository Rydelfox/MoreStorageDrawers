package com.rydelfox.morestoragedrawers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class PortUtil {

    public static ResourceLocation getRegistryName(Block block) {
        return block.builtInRegistryHolder().key().location();
    }

    public static ResourceLocation getRegistryName(Item item) {
        return item.builtInRegistryHolder().key().location();
    }
}