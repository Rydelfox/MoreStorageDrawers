package com.rydelfox.morestoragedrawers.datagen;

import com.rydelfox.morestoragedrawers.MoreStorageDrawers;
import com.rydelfox.morestoragedrawers.block.DrawerMaterial;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

public class DrawerRecipeProvider extends RecipeProvider {

    public DrawerRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
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

    private void build_full_1(DrawerMaterial material, Consumer<IFinishedRecipe> consumer) {
        //MoreStorageDrawers.logInfo("Generating Recipe for "+material.getName()+"_full_1");
        if (material.getPlankResource() == null) {
            MoreStorageDrawers.logInfo("Could not generate recipe for "+material.getEnglishName()+"! Could not load plank!");
            return;
        }
        //RegistryObject<Item> plank = RegistryObject.of(material.getPlankResource(), ForgeRegistries.ITEMS);
        Item plank = ForgeRegistries.ITEMS.getValue(material.getPlankResource());
        ConditionalRecipe.builder().addCondition( new ModLoadedCondition(material.getMod().getSerializedName()) )
                .addRecipe(consumer1 ->
                    ShapedRecipeBuilder.shaped(material.getDrawer(1, false))
                        .pattern("///")
                        .pattern(" x ")
                        .pattern("///")
                        .define('/', plank)
                        .define('x', Tags.Items.CHESTS_WOODEN)
                        .group("morestoragedrawers")
                        .unlockedBy("has_item", has(Tags.Items.CHESTS_WOODEN))
                        .save(consumer));
    }

    private void build_full_2(DrawerMaterial material, Consumer<IFinishedRecipe> consumer) {
        //MoreStorageDrawers.logInfo("Generating Recipe for "+material.getName()+"_full_2");
        if (material.getPlankResource() == null) {
            MoreStorageDrawers.logInfo("Could not generate recipe for "+material.getEnglishName()+"! Could not load plank!");
            return;
        }
        //RegistryObject<Item> plank = RegistryObject.of(material.getPlankResource(), ForgeRegistries.ITEMS);
        Item plank = ForgeRegistries.ITEMS.getValue(material.getPlankResource());
        ConditionalRecipe.builder().addCondition( new ModLoadedCondition(material.getMod().getSerializedName()) )
                .addRecipe(consumer1 ->
                        ShapedRecipeBuilder.shaped(material.getDrawer(2, false),2)
                                .pattern("/x/")
                                .pattern("///")
                                .pattern("/x/")
                                .define('/', plank)
                                .define('x', Tags.Items.CHESTS_WOODEN)
                                .group("morestoragedrawers")
                                .unlockedBy("has_item", has(Tags.Items.CHESTS_WOODEN))
                                .save(consumer));
    }

    private void build_full_4(DrawerMaterial material, Consumer<IFinishedRecipe> consumer) {
        //MoreStorageDrawers.logInfo("Generating Recipe for "+material.getName()+"_full_4");
        if (material.getPlankResource() == null) {
            MoreStorageDrawers.logInfo("Could not generate recipe for "+material.getEnglishName()+"! Could not load plank!");
            return;
        }
        //RegistryObject<Item> plank = RegistryObject.of(material.getPlankResource(), ForgeRegistries.ITEMS);
        Item plank = ForgeRegistries.ITEMS.getValue(material.getPlankResource());
        ConditionalRecipe.builder().addCondition( new ModLoadedCondition(material.getMod().getSerializedName()) )
                .addRecipe(consumer1 ->
                        ShapedRecipeBuilder.shaped(material.getDrawer(4, false),4)
                                .pattern("x/x")
                                .pattern("///")
                                .pattern("x/x")
                                .define('/', plank)
                                .define('x', Tags.Items.CHESTS_WOODEN)
                                .group("morestoragedrawers")
                                .unlockedBy("has_item", has(Tags.Items.CHESTS_WOODEN))
                                .save(consumer));
    }

    private void build_half_1(DrawerMaterial material, Consumer<IFinishedRecipe> consumer) {
        if (material.getSlabResource() == null) {
            return;
        }
        //RegistryObject<Item> slab = RegistryObject.of(material.getSlabResource(), ForgeRegistries.ITEMS);
        Item slab = ForgeRegistries.ITEMS.getValue(material.getSlabResource());
        ConditionalRecipe.builder().addCondition( new ModLoadedCondition(material.getMod().getSerializedName()) )
                .addRecipe(consumer1 ->
                        ShapedRecipeBuilder.shaped(material.getDrawer(1, true))
                                .pattern("///")
                                .pattern(" x ")
                                .pattern("///")
                                .define('/', slab)
                                .define('x', Tags.Items.CHESTS_WOODEN)
                                .group("morestoragedrawers")
                                .unlockedBy("has_item", has(Tags.Items.CHESTS_WOODEN))
                                .save(consumer));
    }

    private void build_half_2(DrawerMaterial material, Consumer<IFinishedRecipe> consumer) {
        if (material.getSlabResource() == null) {
            return;
        }
        //RegistryObject<Item> slab = RegistryObject.of(material.getSlabResource(), ForgeRegistries.ITEMS);
        Item slab = ForgeRegistries.ITEMS.getValue(material.getSlabResource());
        ConditionalRecipe.builder().addCondition( new ModLoadedCondition(material.getMod().getSerializedName()) )
                .addRecipe(consumer1 ->
                        ShapedRecipeBuilder.shaped(material.getDrawer(2, true),2)
                                .pattern("/x/")
                                .pattern("///")
                                .pattern("/x/")
                                .define('/', slab)
                                .define('x', Tags.Items.CHESTS_WOODEN)
                                .group("morestoragedrawers")
                                .unlockedBy("has_item", has(Tags.Items.CHESTS_WOODEN))
                                .save(consumer));
    }

    private void build_half_4(DrawerMaterial material, Consumer<IFinishedRecipe> consumer) {
        if (material.getSlabResource() == null) {
            return;
        }
        //RegistryObject<Item> slab = RegistryObject.of(material.getSlabResource(), ForgeRegistries.ITEMS);
        Item slab = ForgeRegistries.ITEMS.getValue(material.getSlabResource());

        ConditionalRecipe.builder().addCondition( new ModLoadedCondition(material.getMod().getSerializedName()) )
                .addRecipe(consumer1 ->
                        ShapedRecipeBuilder.shaped(material.getDrawer(4, true),4)
                                .pattern("x/x")
                                .pattern("///")
                                .pattern("x/x")
                                .define('/', slab)
                                .define('x', Tags.Items.CHESTS_WOODEN)
                                .group("morestoragedrawers")
                                .unlockedBy("has_item", has(Tags.Items.CHESTS_WOODEN))
                                .save(consumer));
    }

    private void build_trim(DrawerMaterial material, Consumer<IFinishedRecipe> consumer) {
        if (material.getPlankResource() == null) {
            MoreStorageDrawers.logInfo("Could not generate recipe for "+material.getEnglishName()+"! Could not load plank!");
            return;
        }
        //RegistryObject<Item> plank = RegistryObject.of(material.getPlankResource(), ForgeRegistries.ITEMS);
        Item plank = ForgeRegistries.ITEMS.getValue(material.getPlankResource());
        ConditionalRecipe.builder().addCondition( new ModLoadedCondition(material.getMod().getSerializedName()) )
                .addRecipe(consumer1 ->
                        ShapedRecipeBuilder.shaped(material.getTrim(),4)
                                .pattern("///")
                                .pattern(" x ")
                                .pattern("///")
                                .define('/', plank)
                                .define('x', Tags.Items.CHESTS_WOODEN)
                                .group("morestoragedrawers")
                                .unlockedBy("has_item", has(Tags.Items.CHESTS_WOODEN))
                                .save(consumer));
    }
}
