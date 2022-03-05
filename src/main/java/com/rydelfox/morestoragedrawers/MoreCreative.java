package com.rydelfox.morestoragedrawers;

import com.rydelfox.morestoragedrawers.block.EnumVariant;
import com.rydelfox.morestoragedrawers.block.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class MoreCreative {

    public static final ItemGroup TAB = new ItemGroup(MoreStorageDrawers.MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            for (EnumVariant variant : EnumVariant.values()) {
                if(variant.getMod() != null && variant.getMod().isLoaded()) {
                    return new ItemStack(ModBlocks.fullOne.get(variant));
                }
            }
            return new ItemStack(Blocks.ACACIA_LOG);
        }
    };
}
