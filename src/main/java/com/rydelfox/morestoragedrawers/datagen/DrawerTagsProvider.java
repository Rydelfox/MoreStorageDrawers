package com.rydelfox.morestoragedrawers.datagen;

import com.rydelfox.morestoragedrawers.MoreStorageDrawers;
import com.rydelfox.morestoragedrawers.block.DrawerMaterial;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.HashSet;
import java.util.Set;

public class DrawerTagsProvider extends ItemTagsProvider {

    public DrawerTagsProvider(DataGenerator dataGeneratorIn, ExistingFileHelper helper) {
        super(dataGeneratorIn, new BlockTagsProvider(dataGeneratorIn, MoreStorageDrawers.MOD_ID, helper), MoreStorageDrawers.MOD_ID, helper);
    }

    @Override
    protected void addTags() {
        Set<Item> drawersItems = new HashSet<>();
        for (DrawerMaterial material : DrawerMaterial.values()) {
            drawersItems.addAll(material.getItems());
        }
        TagsProvider.Builder<Item> itemBuilder = this.tag(ItemTags.bind("storagedrawers:drawers"));
        drawersItems.forEach(itemBuilder::add);
    }
}
