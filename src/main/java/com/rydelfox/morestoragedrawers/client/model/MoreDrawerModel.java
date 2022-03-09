package com.rydelfox.morestoragedrawers.client.model;

import com.google.common.collect.Lists;
import com.jaquadro.minecraft.storagedrawers.StorageDrawers;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerAttributes;
import com.jaquadro.minecraft.storagedrawers.api.storage.attribute.LockAttribute;
import com.jaquadro.minecraft.storagedrawers.block.BlockCompDrawers;
//import com.jaquadro.minecraft.storagedrawers.block.BlockDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import com.mojang.datafixers.util.Either;
import com.rydelfox.morestoragedrawers.MoreStorageDrawers;
import com.rydelfox.morestoragedrawers.block.BlockDrawersExtended;
import com.rydelfox.morestoragedrawers.block.BlockMoreDrawers;
import com.rydelfox.morestoragedrawers.block.DrawerMaterial;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.IResource;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;


public class MoreDrawerModel {

    private static final Map<Direction, IBakedModel> lockOverlaysFull = new HashMap<>();
    private static final Map<Direction, IBakedModel> lockOverlaysHalf = new HashMap<>();
    private static final Map<Direction, IBakedModel> voidOverlaysFull = new HashMap<>();
    private static final Map<Direction, IBakedModel> voidOverlaysHalf = new HashMap<>();
    private static final Map<Direction, IBakedModel> shroudOverlaysFull = new HashMap<>();
    private static final Map<Direction, IBakedModel> shroudOverlaysHalf = new HashMap<>();
    private static final Map<Direction, IBakedModel> indicator1Full = new HashMap<>();
    private static final Map<Direction, IBakedModel> indicator1Half = new HashMap<>();
    private static final Map<Direction, IBakedModel> indicator2Full = new HashMap<>();
    private static final Map<Direction, IBakedModel> indicator2Half = new HashMap<>();
    private static final Map<Direction, IBakedModel> indicator4Full = new HashMap<>();
    private static final Map<Direction, IBakedModel> indicator4Half = new HashMap<>();
    private static final Map<Direction, IBakedModel> indicatorComp = new HashMap<>();

    private static boolean geometryDataLoaded = false;

    @Mod.EventBusSubscriber(modid = MoreStorageDrawers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class Register
    {
        @SubscribeEvent
        public static void registerTextures(TextureStitchEvent.Pre event) {
            MoreStorageDrawers.logInfo("MoreStorageDrawers: Registering Textures");
            loadUnbakedModel(event, new ResourceLocation(StorageDrawers.MOD_ID, "models/block/full_drawers_lock.json"));
            loadUnbakedModel(event, new ResourceLocation(StorageDrawers.MOD_ID, "models/block/full_drawers_void.json"));
            loadUnbakedModel(event, new ResourceLocation(StorageDrawers.MOD_ID, "models/block/full_drawers_shroud.json"));
            loadUnbakedModel(event, new ResourceLocation(StorageDrawers.MOD_ID, "models/block/compdrawers_indicator.json"));
            loadUnbakedModel(event, new ResourceLocation(StorageDrawers.MOD_ID, "models/block/full_drawers_indicator_1.json"));
            loadUnbakedModel(event, new ResourceLocation(StorageDrawers.MOD_ID, "models/block/full_drawers_indicator_2.json"));
            loadUnbakedModel(event, new ResourceLocation(StorageDrawers.MOD_ID, "models/block/full_drawers_indicator_4.json"));

            loadGeometryData();
        }

        private static void loadGeometryData() {
            if(geometryDataLoaded)
                return;
            geometryDataLoaded = true;

            List<BlockMoreDrawers> fullDrawers1 = new ArrayList<>();
            List<BlockMoreDrawers> fullDrawers2 = new ArrayList<>();
            List<BlockMoreDrawers> fullDrawers4 = new ArrayList<>();
            List<BlockMoreDrawers> halfDrawers1 = new ArrayList<>();
            List<BlockMoreDrawers> halfDrawers2 = new ArrayList<>();
            List<BlockMoreDrawers> halfDrawers4 = new ArrayList<>();
            for(DrawerMaterial material : DrawerMaterial.values()) {
                fullDrawers1.add(material.getDrawer(1, false));
                fullDrawers1.add(material.getDrawer(1, true));
                fullDrawers2.add(material.getDrawer(2, false));
                fullDrawers2.add(material.getDrawer(2, true));
                fullDrawers4.add(material.getDrawer(4, false));
                fullDrawers4.add(material.getDrawer(4, true));
            }

            MoreStorageDrawers.logInfo("MoreStorageDrawers: Populating Geometry");
            populateGeometryData(new ResourceLocation(StorageDrawers.MOD_ID, "models/block/geometry/full_drawers_icon_area_1.json"),
                    new ResourceLocation(StorageDrawers.MOD_ID, "models/block/geometry/full_drawers_count_area_1.json"),
                    new ResourceLocation(StorageDrawers.MOD_ID, "models/block/geometry/full_drawers_ind_area_1.json"),
                    new ResourceLocation(StorageDrawers.MOD_ID, "models/block/geometry/full_drawers_indbase_area_1.json"),
                    fullDrawers1.stream().toArray(BlockMoreDrawers[]::new));
            populateGeometryData(new ResourceLocation(StorageDrawers.MOD_ID, "models/block/geometry/full_drawers_icon_area_2.json"),
                    new ResourceLocation(StorageDrawers.MOD_ID, "models/block/geometry/full_drawers_count_area_2.json"),
                    new ResourceLocation(StorageDrawers.MOD_ID, "models/block/geometry/full_drawers_ind_area_2.json"),
                    new ResourceLocation(StorageDrawers.MOD_ID, "models/block/geometry/full_drawers_indbase_area_2.json"),
                    fullDrawers2.stream().toArray(BlockDrawersExtended[]::new));
            populateGeometryData(new ResourceLocation(StorageDrawers.MOD_ID, "models/block/geometry/full_drawers_icon_area_4.json"),
                    new ResourceLocation(StorageDrawers.MOD_ID, "models/block/geometry/full_drawers_count_area_4.json"),
                    new ResourceLocation(StorageDrawers.MOD_ID, "models/block/geometry/full_drawers_ind_area_4.json"),
                    new ResourceLocation(StorageDrawers.MOD_ID, "models/block/geometry/full_drawers_indbase_area_4.json"),
                    fullDrawers4.stream().toArray(BlockDrawersExtended[]::new));
            populateGeometryData(new ResourceLocation(StorageDrawers.MOD_ID, "models/block/geometry/half_drawers_icon_area_1.json"),
                    new ResourceLocation(StorageDrawers.MOD_ID, "models/block/geometry/half_drawers_count_area_1.json"),
                    new ResourceLocation(StorageDrawers.MOD_ID, "models/block/geometry/half_drawers_ind_area_1.json"),
                    new ResourceLocation(StorageDrawers.MOD_ID, "models/block/geometry/half_drawers_indbase_area_1.json"),
                    halfDrawers1.stream().toArray(BlockDrawersExtended[]::new));
            populateGeometryData(new ResourceLocation(StorageDrawers.MOD_ID, "models/block/geometry/half_drawers_icon_area_2.json"),
                    new ResourceLocation(StorageDrawers.MOD_ID, "models/block/geometry/half_drawers_count_area_2.json"),
                    new ResourceLocation(StorageDrawers.MOD_ID, "models/block/geometry/half_drawers_ind_area_2.json"),
                    new ResourceLocation(StorageDrawers.MOD_ID, "models/block/geometry/half_drawers_indbase_area_2.json"),
                    halfDrawers2.stream().toArray(BlockDrawersExtended[]::new));
            populateGeometryData(new ResourceLocation(StorageDrawers.MOD_ID, "models/block/geometry/half_drawers_icon_area_4.json"),
                    new ResourceLocation(StorageDrawers.MOD_ID, "models/block/geometry/half_drawers_count_area_4.json"),
                    new ResourceLocation(StorageDrawers.MOD_ID, "models/block/geometry/half_drawers_ind_area_4.json"),
                    new ResourceLocation(StorageDrawers.MOD_ID, "models/block/geometry/half_drawers_indbase_area_4.json"),
                    halfDrawers4.stream().toArray(BlockDrawersExtended[]::new));
        }

        private static void populateGeometryData(ResourceLocation locationIcon,
                                                 ResourceLocation locationCount,
                                                 ResourceLocation locationInd,
                                                 ResourceLocation locationIndBase,
                                                 BlockDrawersExtended... blocks) {
            BlockModel slotInfo = getBlockModel(locationIcon);
            BlockModel countInfo = getBlockModel(locationCount);
            BlockModel indInfo = getBlockModel(locationInd);
            BlockModel indBaseInfo = getBlockModel(locationIndBase);
            for (BlockDrawersExtended block : blocks) {
                if (block == null)
                    continue;

                for (int i = 0; i < block.getDrawerCount(); i++) {
                    Vector3f from = slotInfo.getElements().get(i).from;
                    Vector3f to = slotInfo.getElements().get(i).to;
                    block.labelGeometry[i] = new AxisAlignedBB(from.x(), from.y(), from.z(), to.x(), to.y(), to.z());
                }
                for (int i = 0; i < block.getDrawerCount(); i++) {
                    Vector3f from = countInfo.getElements().get(i).from;
                    Vector3f to = countInfo.getElements().get(i).to;
                    block.countGeometry[i] = new AxisAlignedBB(from.x(), from.y(), from.z(), to.x(), to.y(), to.z());
                }
                for (int i = 0; i < block.getDrawerCount(); i++) {
                    Vector3f from = indInfo.getElements().get(i).from;
                    Vector3f to = indInfo.getElements().get(i).to;
                    block.indGeometry[i] = new AxisAlignedBB(from.x(), from.y(), from.z(), to.x(), to.y(), to.z());
                }
                for (int i = 0; i < block.getDrawerCount(); i++) {
                    Vector3f from = indBaseInfo.getElements().get(i).from;
                    Vector3f to = indBaseInfo.getElements().get(i).to;
                    block.indBaseGeometry[i] = new AxisAlignedBB(from.x(), from.y(), from.x(), to.z(), to.y(), to.z());
                }
            }
        }

        private static void loadUnbakedModel(TextureStitchEvent.Pre event, ResourceLocation resource) {
            BlockModel unbakedModel = getBlockModel(resource);

            for (Either<RenderMaterial, String> x : unbakedModel.textureMap.values()) {
                x.ifLeft((value) -> {
                    if (value.atlasLocation().equals(event.getMap().location()))
                        event.addSprite(value.texture());
                });
            }
        }

        private static BlockModel getBlockModel (ResourceLocation location) {
            try (IResource iresource = Minecraft.getInstance().getResourceManager().getResource(location)){
                try (Reader reader = new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8)){
                    return BlockModel.fromStream(reader);
                }
            } catch (IOException e) {
                return null;
            }
        }

        @SubscribeEvent
        public static void registerModels (ModelBakeEvent event) {
            MoreStorageDrawers.logInfo("MoreStorageDrawers: Registering Models");
            for (int i = 0; i < 4; i++) {
                Direction dir = Direction.from2DDataValue(i);
                ModelRotation rot = ModelRotation.by(0, (int)dir.toYRot() + 180);
                Function<RenderMaterial, TextureAtlasSprite> texGet = ModelLoader.defaultTextureGetter();

                lockOverlaysFull.put(dir, event.getModelLoader().getBakedModel(new ResourceLocation(StorageDrawers.MOD_ID, "block/full_drawers_lock"), rot, texGet));
                lockOverlaysHalf.put(dir, event.getModelLoader().getBakedModel(new ResourceLocation(StorageDrawers.MOD_ID, "block/half_drawers_lock"), rot, texGet));
                voidOverlaysFull.put(dir, event.getModelLoader().getBakedModel(new ResourceLocation(StorageDrawers.MOD_ID, "block/full_drawers_void"), rot, texGet));
                voidOverlaysHalf.put(dir, event.getModelLoader().getBakedModel(new ResourceLocation(StorageDrawers.MOD_ID, "block/half_drawers_void"), rot, texGet));
                shroudOverlaysFull.put(dir, event.getModelLoader().getBakedModel(new ResourceLocation(StorageDrawers.MOD_ID, "block/full_drawers_shroud"), rot, texGet));
                shroudOverlaysHalf.put(dir, event.getModelLoader().getBakedModel(new ResourceLocation(StorageDrawers.MOD_ID, "block/half_drawers_shroud"), rot, texGet));
                indicator1Full.put(dir, event.getModelLoader().getBakedModel(new ResourceLocation(StorageDrawers.MOD_ID, "block/full_drawers_indicator_1"), rot, texGet));
                indicator1Half.put(dir, event.getModelLoader().getBakedModel(new ResourceLocation(StorageDrawers.MOD_ID, "block/half_drawers_indicator_1"), rot, texGet));
                indicator2Full.put(dir, event.getModelLoader().getBakedModel(new ResourceLocation(StorageDrawers.MOD_ID, "block/full_drawers_indicator_2"), rot, texGet));
                indicator2Half.put(dir, event.getModelLoader().getBakedModel(new ResourceLocation(StorageDrawers.MOD_ID, "block/half_drawers_indicator_2"), rot, texGet));
                indicator4Full.put(dir, event.getModelLoader().getBakedModel(new ResourceLocation(StorageDrawers.MOD_ID, "block/full_drawers_indicator_4"), rot, texGet));
                indicator4Half.put(dir, event.getModelLoader().getBakedModel(new ResourceLocation(StorageDrawers.MOD_ID, "block/half_drawers_indicator_4"), rot, texGet));
                indicatorComp.put(dir, event.getModelLoader().getBakedModel(new ResourceLocation(StorageDrawers.MOD_ID, "block/compdrawers_indicator"), rot, texGet));
            }

            for(DrawerMaterial material : DrawerMaterial.values()) {
                if (material.getMod() != null && material.getMod().isLoaded()) {
                    replaceBlock(event, material.getDrawer(1, false));
                    replaceBlock(event, material.getDrawer(2, false));
                    replaceBlock(event, material.getDrawer(3, false));
                    replaceBlock(event, material.getDrawer(1, true));
                    replaceBlock(event, material.getDrawer(2, true));
                    replaceBlock(event, material.getDrawer(3, true));
                }
            }
        }

        public static void replaceBlock(ModelBakeEvent event, BlockDrawersExtended block) {
            for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                ModelResourceLocation modelResource = BlockModelShapes.stateToModelLocation(state);
                IBakedModel parentModel = event.getModelManager().getModel(modelResource);
                if (parentModel == null) {
                    continue;
                } else if (parentModel == event.getModelManager().getMissingModel()) {
                    continue;
                }

                if (block.isHalfDepth())
                    event.getModelRegistry().put(modelResource, new Model2.HalfModel(parentModel));
                else
                    event.getModelRegistry().put(modelResource, new Model2.FullModel(parentModel));
            }
        }

        public static void replaceBlock(ModelBakeEvent event, BlockMoreDrawers block) {
            for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                ModelResourceLocation modelResource = BlockModelShapes.stateToModelLocation(state);
                IBakedModel parentModel = event.getModelManager().getModel(modelResource);
                if (parentModel == null) {
                    continue;
                } else if (parentModel == event.getModelManager().getMissingModel()) {
                    continue;
                }

                if (block.isHalfDepth())
                    event.getModelRegistry().put(modelResource, new Model2.HalfModel(parentModel));
                else
                    event.getModelRegistry().put(modelResource, new Model2.FullModel(parentModel));
            }
        }

        public static abstract class Model2 implements IDynamicBakedModel {
            protected final IBakedModel mainModel;
            protected final Map<Direction, IBakedModel> lockOverlay;
            protected final Map<Direction, IBakedModel> voidOverlay;
            protected final Map<Direction, IBakedModel> shroudOverlay;
            protected final Map<Direction, IBakedModel> indicator1Overlay;
            protected final Map<Direction, IBakedModel> indicator2Overlay;
            protected final Map<Direction, IBakedModel> indicator4Overlay;
            protected final Map<Direction, IBakedModel> indicatorCompOverlay;

            public static class FullModel extends Model2 {
                FullModel(IBakedModel mainModel) {
                    super(mainModel, lockOverlaysFull, voidOverlaysFull, shroudOverlaysFull, indicator1Full, indicator2Full, indicator4Full, indicatorComp);
                }
            }

            public static class HalfModel extends Model2 {
                HalfModel(IBakedModel mainModel) {
                    super(mainModel, lockOverlaysHalf, voidOverlaysHalf, shroudOverlaysHalf, indicator1Half, indicator2Half, indicator4Half, indicatorComp);
                }
            }

            private Model2(IBakedModel mainModel,
                           Map<Direction, IBakedModel> lockOverlay,
                           Map<Direction, IBakedModel> voidOverlay,
                           Map<Direction, IBakedModel> shroudOverlay,
                           Map<Direction, IBakedModel> indicator1Overlay,
                           Map<Direction, IBakedModel> indicator2Overlay,
                           Map<Direction, IBakedModel> indicator4Overlay,
                           Map<Direction, IBakedModel> indicatorComp) {
                this.mainModel = mainModel;
                this.lockOverlay = lockOverlay;
                this.voidOverlay = voidOverlay;
                this.shroudOverlay = shroudOverlay;
                this.indicator1Overlay = indicator1Overlay;
                this.indicator2Overlay = indicator2Overlay;
                this.indicator4Overlay = indicator4Overlay;
                this.indicatorCompOverlay = indicatorComp;
            }

            @Override
            public boolean usesBlockLight () {
                return mainModel.usesBlockLight();
            }

            @Nonnull
            @Override
            public List<BakedQuad> getQuads (@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
                List<BakedQuad> quads = Lists.newArrayList();
                quads.addAll(mainModel.getQuads(state, side, rand, extraData));

                if (state != null && extraData.hasProperty(TileEntityDrawers.ATTRIBUTES)) {
                    IDrawerAttributes attr = extraData.getData(TileEntityDrawers.ATTRIBUTES);
                    Direction dir = state.getValue(BlockDrawersExtended.FACING);

                    if (attr.isItemLocked(LockAttribute.LOCK_EMPTY) || attr.isItemLocked(LockAttribute.LOCK_POPULATED))
                        quads.addAll(lockOverlay.get(dir).getQuads(state, side, rand, extraData));
                    if (attr.isVoid())
                        quads.addAll(voidOverlay.get(dir).getQuads(state, side, rand, extraData));
                    if (attr.isConcealed())
                        quads.addAll(shroudOverlay.get(dir).getQuads(state, side, rand, extraData));
                    if (attr.hasFillLevel()) {
                        Block block = state.getBlock();
                        if (block instanceof BlockCompDrawers)
                            quads.addAll((indicatorCompOverlay.get(dir).getQuads(state, side, rand, extraData)));
                        else if (block instanceof BlockDrawersExtended) {
                            int count = ((BlockDrawersExtended) block).getDrawerCount();
                            if (count == 1)
                                quads.addAll((indicator1Overlay.get(dir).getQuads(state, side, rand, extraData)));
                            else if (count == 2)
                                quads.addAll((indicator2Overlay.get(dir).getQuads(state, side, rand, extraData)));
                            else if (count == 4)
                                quads.addAll((indicator4Overlay.get(dir).getQuads(state, side, rand, extraData)));
                        }
                    }
                }

                return quads;
            }

            @Override
            public boolean useAmbientOcclusion () {
                return mainModel.useAmbientOcclusion();
            }

            @Override
            public boolean isGui3d () {
                return mainModel.isGui3d();
            }

            @Override
            public boolean isCustomRenderer () {
                return mainModel.isCustomRenderer();
            }

            @Override
            public TextureAtlasSprite getParticleIcon () {
                return mainModel.getParticleIcon();
            }

            @Override
            public ItemOverrideList getOverrides () {
                return mainModel.getOverrides();
            }
        }
    }
}
