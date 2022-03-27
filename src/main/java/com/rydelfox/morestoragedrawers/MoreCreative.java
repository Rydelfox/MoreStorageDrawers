package com.rydelfox.morestoragedrawers;

import com.rydelfox.morestoragedrawers.block.DrawerMaterial;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class MoreCreative {

    public static final CreativeModeTab TAB = new CreativeModeTab(MoreStorageDrawers.MOD_ID) {
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
