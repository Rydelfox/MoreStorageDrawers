package com.rydelfox.morestoragedrawers.datagen;

import com.jaquadro.minecraft.storagedrawers.StorageDrawers;
import com.rydelfox.morestoragedrawers.MoreStorageDrawers;
import com.rydelfox.morestoragedrawers.block.BlockMoreDrawers;
import com.rydelfox.morestoragedrawers.block.DrawerMaterial;
import net.minecraft.block.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Function;

public class DrawerBlockStateProvider extends BlockStateProvider {

    public DrawerBlockStateProvider(DataGenerator dataGeneratorIn, ExistingFileHelper helper) {
        super(dataGeneratorIn, MoreStorageDrawers.MOD_ID, helper);
    }

    @Override
    protected void registerStatesAndModels() {


        for (DrawerMaterial material : DrawerMaterial.values()) {
            if (material.getMod() != null && material.getMod().isLoaded()) {
                BlockModelBuilder trimBlock = models().cubeAll("block/" + material.getName() + "_trim", modLoc("blocks/" + material.prefix() + "_side"));
                simpleBlock(material.getTrim(), trimBlock);
                simpleBlockItem(material.getTrim(), trimBlock);

                horizontalDrawer(material.getDrawer(1, false),
                        new ResourceLocation(MoreStorageDrawers.MOD_ID, "blocks/" + material.prefix() + "_side"),
                        new ResourceLocation(MoreStorageDrawers.MOD_ID, "blocks/" + material.prefix() + "_front_1"),
                        new ResourceLocation(MoreStorageDrawers.MOD_ID, "blocks/" + material.prefix() + "_trim"), false);
                horizontalDrawer(material.getDrawer(2, false),
                        new ResourceLocation(MoreStorageDrawers.MOD_ID, "blocks/" + material.prefix() + "_side"),
                        new ResourceLocation(MoreStorageDrawers.MOD_ID, "blocks/" + material.prefix() + "_front_2"),
                        new ResourceLocation(MoreStorageDrawers.MOD_ID, "blocks/" + material.prefix() + "_trim"), false);
                horizontalDrawer(material.getDrawer(4, false),
                        new ResourceLocation(MoreStorageDrawers.MOD_ID, "blocks/" + material.prefix() + "_side"),
                        new ResourceLocation(MoreStorageDrawers.MOD_ID, "blocks/" + material.prefix() + "_front_4"),
                        new ResourceLocation(MoreStorageDrawers.MOD_ID, "blocks/" + material.prefix() + "_trim"), false);

                horizontalDrawer(material.getDrawer(1, true),
                        new ResourceLocation(MoreStorageDrawers.MOD_ID, "blocks/" + material.prefix() + "_side_v"),
                        new ResourceLocation(MoreStorageDrawers.MOD_ID, "blocks/" + material.prefix() + "_front_1"),
                        new ResourceLocation(MoreStorageDrawers.MOD_ID, "blocks/" + material.prefix() + "_side_h"),
                        new ResourceLocation(MoreStorageDrawers.MOD_ID, "blocks/" + material.prefix() + "_side"),
                        new ResourceLocation(MoreStorageDrawers.MOD_ID, "blocks/" + material.prefix() + "_trim"), true);
                horizontalDrawer(material.getDrawer(2, true),
                        new ResourceLocation(MoreStorageDrawers.MOD_ID, "blocks/" + material.prefix() + "_side_v"),
                        new ResourceLocation(MoreStorageDrawers.MOD_ID, "blocks/" + material.prefix() + "_front_2"),
                        new ResourceLocation(MoreStorageDrawers.MOD_ID, "blocks/" + material.prefix() + "_side_h"),
                        new ResourceLocation(MoreStorageDrawers.MOD_ID, "blocks/" + material.prefix() + "_side"),
                        new ResourceLocation(MoreStorageDrawers.MOD_ID, "blocks/" + material.prefix() + "_trim"), true);
                horizontalDrawer(material.getDrawer(4, true),
                        new ResourceLocation(MoreStorageDrawers.MOD_ID, "blocks/" + material.prefix() + "_side_v"),
                        new ResourceLocation(MoreStorageDrawers.MOD_ID, "blocks/" + material.prefix() + "_front_4"),
                        new ResourceLocation(MoreStorageDrawers.MOD_ID, "blocks/" + material.prefix() + "_side_h"),
                        new ResourceLocation(MoreStorageDrawers.MOD_ID, "blocks/" + material.prefix() + "_side"),
                        new ResourceLocation(MoreStorageDrawers.MOD_ID, "blocks/" + material.prefix() + "_trim"), true);

            }
        }
    }

    private void orientedBlock(Block block, Function<BlockState, ModelFile> modelFunc) {
        getVariantBuilder(block)
                .forAllStates(state -> {
                    Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                    return ConfiguredModel.builder()
                            .modelFile(modelFunc.apply(state))
                            .rotationX(dir.getAxis() == Direction.Axis.Y ?  dir.getAxisDirection().getStep() * -90 : 0)
                            .rotationY(dir.getAxis() != Direction.Axis.Y ? ((dir.get2DDataValue() + 2) % 4) * 90 : 0)
                            .build();
                });
    }

    protected void horizontalDrawer(BlockMoreDrawers block, ResourceLocation side, ResourceLocation front, ResourceLocation trim, boolean halfDepth) {
        horizontalDrawer(block, side, front, side, side, trim, halfDepth);
    }

    protected void horizontalDrawer(BlockMoreDrawers block, ResourceLocation side, ResourceLocation front, ResourceLocation top, ResourceLocation back, ResourceLocation trim, boolean halfDepth) {
        String builderPath = "block/builder/" + (halfDepth ? "half" : "full") + "drawermodelbuilder";
        ResourceLocation parent;
        if (!halfDepth)
            //parent = models().withExistingParent("full_drawers", modLoc("block/full_drawers_orientable"));
            parent = modLoc("block/full_drawers");
        else
            parent = modLoc("block/half_drawers");

        ModelFile model = models()  //.getBuilder(block.getRegistryName().getPath())
                .withExistingParent(block.getRegistryName().getPath(),parent)
                .texture("particle", front)
                .texture("east", side)
                .texture("west", side)
                .texture("north", front)
                .texture("up", top)
                .texture("down", top)
                .texture("south", back)
                .texture("trim", trim);

        buildDrawerState(block, model);
        //buildDrawerItem(Item.BY_BLOCK.get(block));
    }

    protected void buildDrawerState(BlockMoreDrawers block, ModelFile model) {
        VariantBlockStateBuilder builder = getVariantBuilder(block);
        builder.partialState().with(BlockMoreDrawers.FACING, Direction.NORTH).addModels(new ConfiguredModel(model));
        builder.partialState().with(BlockMoreDrawers.FACING, Direction.EAST).addModels(new ConfiguredModel(model, 0, 90, false));
        builder.partialState().with(BlockMoreDrawers.FACING, Direction.SOUTH).addModels(new ConfiguredModel(model, 0, 180, false));
        builder.partialState().with(BlockMoreDrawers.FACING, Direction.WEST).addModels(new ConfiguredModel(model, 0, 270, false));
    }

    protected void buildDrawerItem(Item block) {
        models().withExistingParent(block.getRegistryName().getPath(), modLoc("block/"+block.getRegistryName().getPath()));
    }
}
