package com.rydelfox.morestoragedrawers.datagen;

import com.rydelfox.morestoragedrawers.MoreStorageDrawers;
import com.rydelfox.morestoragedrawers.block.DrawerMaterial;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class DrawerItemModelProvider extends ItemModelProvider {

    public DrawerItemModelProvider(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, MoreStorageDrawers.MOD_ID, helper);
    }

    @Override
    protected void registerModels() {
        for (DrawerMaterial material : DrawerMaterial.values()) {
            if (material.getMod() != null && material.getMod().isLoaded()) {
                // Trim blocks already created by DrawerBlockStateProvider

                withExistingParent(material.getDrawer(1, false).getRegistryName().getPath(), modLoc( "block/" + material.prefix() + "_full_1"));
                withExistingParent(material.getDrawer(2, false).getRegistryName().getPath(), modLoc( "block/" + material.prefix() + "_full_2"));
                withExistingParent(material.getDrawer(4, false).getRegistryName().getPath(), modLoc( "block/" + material.prefix() + "_full_4"));
                if (material.getSlabResource() != null) {
                    withExistingParent(material.getDrawer(1, true).getRegistryName().getPath(), modLoc( "block/" + material.prefix() + "_half_1"));
                    withExistingParent(material.getDrawer(2, true).getRegistryName().getPath(), modLoc( "block/" + material.prefix() + "_half_2"));
                    withExistingParent(material.getDrawer(4, true).getRegistryName().getPath(), modLoc( "block/" + material.prefix() + "_half_4"));
                }
            }
        }
    }
}
