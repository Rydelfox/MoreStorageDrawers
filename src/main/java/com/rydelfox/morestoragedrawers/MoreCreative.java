package com.rydelfox.morestoragedrawers;

import com.rydelfox.morestoragedrawers.block.DrawerMaterial;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class MoreCreative {

    public static final ItemGroup TAB = new ItemGroup(MoreStorageDrawers.MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            for (DrawerMaterial material : DrawerMaterial.values()) {
                if(material.getMod() != null && material.getMod().isLoaded()) {
                    return new ItemStack(material.getDrawer(4, false));
                }
            }
            return new ItemStack(Blocks.ACACIA_LOG);
        }
    };
}
