package com.rydelfox.morestoragedrawers.datagen;

import com.rydelfox.morestoragedrawers.MoreStorageDrawers;
import com.rydelfox.morestoragedrawers.block.DrawerMaterial;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.rydelfox.morestoragedrawers.PortUtil.*;

public class DrawerItemModelProvider extends ItemModelProvider {

    public DrawerItemModelProvider(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, MoreStorageDrawers.MOD_ID, helper);
    }

    @Override
    protected void registerModels() {
        for (DrawerMaterial material : DrawerMaterial.values()) {
            if (material.getMod() != null && material.getMod().isLoaded()) {
                // Trim blocks already created by DrawerBlockStateProvider

                withExistingParent(getRegistryName(material.getDrawer(1, false)).getPath(), modLoc( "block/" + material.prefix() + "_full_1"));
                withExistingParent(getRegistryName(material.getDrawer(2, false)).getPath(), modLoc( "block/" + material.prefix() + "_full_2"));
                withExistingParent(getRegistryName(material.getDrawer(4, false)).getPath(), modLoc( "block/" + material.prefix() + "_full_4"));
                if (material.getSlabResource() != null) {
                    withExistingParent(getRegistryName(material.getDrawer(1, true)).getPath(), modLoc( "block/" + material.prefix() + "_half_1"));
                    withExistingParent(getRegistryName(material.getDrawer(2, true)).getPath(), modLoc( "block/" + material.prefix() + "_half_2"));
                    withExistingParent(getRegistryName(material.getDrawer(4, true)).getPath(), modLoc( "block/" + material.prefix() + "_half_4"));
                }
            }
        }
    }
}