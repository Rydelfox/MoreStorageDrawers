package com.rydelfox.morestoragedrawers.datagen;

import com.rydelfox.morestoragedrawers.MoreStorageDrawers;
import com.rydelfox.morestoragedrawers.block.DrawerMaterial;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

public class DrawerRecipeProvider extends RecipeProvider {

    public DrawerRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        MoreStorageDrawers.logInfo("Generating Recipes");
        for(DrawerMaterial material : DrawerMaterial.values()) {
            if (material.getMod() != null && material.getMod().isLoaded()) {
                build_full_1(material, consumer);
                build_full_2(material, consumer);
                build_full_4(material, consumer);
                build_trim(material, consumer);
                build_half_1(material, consumer);
                build_half_2(material, consumer);
                build_half_4(material, consumer);
            }
        }
    }

    private void build_full_1(DrawerMaterial material, Consumer<FinishedRecipe> consumer) {
        //MoreStorageDrawers.logInfo("Generating Recipe for "+material.getName()+"_full_1");
        if (material.getPlankResource() == null) {
            MoreStorageDrawers.logInfo("Could not generate recipe for "+material.getEnglishName()+"! Could not load plank!");
            return;
        }
        //RegistryObject<Item> plank = RegistryObject.of(material.getPlankResource(), ForgeRegistries.ITEMS);
        Item plank = ForgeRegistries.ITEMS.getValue(material.getPlankResource());
        ShapedRecipeBuilder.shaped(material.getDrawer(1, false))
                .pattern("///")
                .pattern(" x ")
                .pattern("///")
                .define('/', plank)
                .define('x', Tags.Items.CHESTS_WOODEN)
                .group("morestoragedrawers")
                .unlockedBy("has_item", has(Tags.Items.CHESTS_WOODEN))
                .save(consumer);
    }

    private void build_full_2(DrawerMaterial material, Consumer<FinishedRecipe> consumer) {
        //MoreStorageDrawers.logInfo("Generating Recipe for "+material.getName()+"_full_2");
        if (material.getPlankResource() == null) {
            MoreStorageDrawers.logInfo("Could not generate recipe for "+material.getEnglishName()+"! Could not load plank!");
            return;
        }
        //RegistryObject<Item> plank = RegistryObject.of(material.getPlankResource(), ForgeRegistries.ITEMS);
        Item plank = ForgeRegistries.ITEMS.getValue(material.getPlankResource());
        ShapedRecipeBuilder.shaped(material.getDrawer(2, false))
                .pattern("/x/")
                .pattern("///")
                .pattern("/x/")
                .define('/', plank)
                .define('x', Tags.Items.CHESTS_WOODEN)
                .group("morestoragedrawers")
                .unlockedBy("has_item", has(Tags.Items.CHESTS_WOODEN))
                .save(consumer);
    }

    private void build_full_4(DrawerMaterial material, Consumer<FinishedRecipe> consumer) {
        //MoreStorageDrawers.logInfo("Generating Recipe for "+material.getName()+"_full_4");
        if (material.getPlankResource() == null) {
            MoreStorageDrawers.logInfo("Could not generate recipe for "+material.getEnglishName()+"! Could not load plank!");
            return;
        }
        //RegistryObject<Item> plank = RegistryObject.of(material.getPlankResource(), ForgeRegistries.ITEMS);
        Item plank = ForgeRegistries.ITEMS.getValue(material.getPlankResource());
        ShapedRecipeBuilder.shaped(material.getDrawer(4, false))
                .pattern("x/x")
                .pattern("///")
                .pattern("x/x")
                .define('/', plank)
                .define('x', Tags.Items.CHESTS_WOODEN)
                .group("morestoragedrawers")
                .unlockedBy("has_item", has(Tags.Items.CHESTS_WOODEN))
                .save(consumer);
    }

    private void build_half_1(DrawerMaterial material, Consumer<FinishedRecipe> consumer) {
        if (material.getSlabResource() == null) {
            return;
        }
        //RegistryObject<Item> slab = RegistryObject.of(material.getSlabResource(), ForgeRegistries.ITEMS);
        Item slab = ForgeRegistries.ITEMS.getValue(material.getSlabResource());
        ShapedRecipeBuilder.shaped(material.getDrawer(1, true))
                .pattern("///")
                .pattern(" x ")
                .pattern("///")
                .define('/', slab)
                .define('x', Tags.Items.CHESTS_WOODEN)
                .group("morestoragedrawers")
                .unlockedBy("has_item", has(Tags.Items.CHESTS_WOODEN))
                .save(consumer);
    }

    private void build_half_2(DrawerMaterial material, Consumer<FinishedRecipe> consumer) {
        if (material.getSlabResource() == null) {
            return;
        }
        //RegistryObject<Item> slab = RegistryObject.of(material.getSlabResource(), ForgeRegistries.ITEMS);
        Item slab = ForgeRegistries.ITEMS.getValue(material.getSlabResource());
        ShapedRecipeBuilder.shaped(material.getDrawer(2, true))
                .pattern("/x/")
                .pattern("///")
                .pattern("/x/")
                .define('/', slab)
                .define('x', Tags.Items.CHESTS_WOODEN)
                .group("morestoragedrawers")
                .unlockedBy("has_item", has(Tags.Items.CHESTS_WOODEN))
                .save(consumer);
    }

    private void build_half_4(DrawerMaterial material, Consumer<FinishedRecipe> consumer) {
        if (material.getSlabResource() == null) {
            return;
        }
        //RegistryObject<Item> slab = RegistryObject.of(material.getSlabResource(), ForgeRegistries.ITEMS);
        Item slab = ForgeRegistries.ITEMS.getValue(material.getSlabResource());
        ShapedRecipeBuilder.shaped(material.getDrawer(4, true))
                .pattern("x/x")
                .pattern("///")
                .pattern("x/x")
                .define('/', slab)
                .define('x', Tags.Items.CHESTS_WOODEN)
                .group("morestoragedrawers")
                .unlockedBy("has_item", has(Tags.Items.CHESTS_WOODEN))
                .save(consumer);
    }

    private void build_trim(DrawerMaterial material, Consumer<FinishedRecipe> consumer) {
        if (material.getPlankResource() == null) {
            MoreStorageDrawers.logInfo("Could not generate recipe for "+material.getEnglishName()+"! Could not load plank!");
            return;
        }
        //RegistryObject<Item> plank = RegistryObject.of(material.getPlankResource(), ForgeRegistries.ITEMS);
        Item plank = ForgeRegistries.ITEMS.getValue(material.getPlankResource());
        ShapedRecipeBuilder.shaped(material.getTrim())
                .pattern("X/X")
                .pattern("/X/")
                .pattern("X/X")
                .define('X', plank)
                .define('/', Tags.Items.RODS_WOODEN)
                .group("morestoragedrawers")
                .unlockedBy("has_item", has(plank))
                .save(consumer);
    }
}
