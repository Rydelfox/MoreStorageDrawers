package com.rydelfox.morestoragedrawers;

import com.jaquadro.minecraft.storagedrawers.client.renderer.BlockEntityDrawersRenderer;
import com.jaquadro.minecraft.storagedrawers.config.ClientConfig;
import com.jaquadro.minecraft.storagedrawers.config.CommonConfig;
import com.jaquadro.minecraft.storagedrawers.config.CompTierRegistry;
import com.rydelfox.morestoragedrawers.block.EnumMod;
import com.rydelfox.morestoragedrawers.block.tile.Tiles;
import com.rydelfox.morestoragedrawers.core.Registration;
import com.rydelfox.morestoragedrawers.network.MoreStorageDrawersPacketHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MoreStorageDrawers.MOD_ID)
@Mod.EventBusSubscriber(modid = MoreStorageDrawers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MoreStorageDrawers {
    // Directly reference a log4j logger.
    public static final String MOD_ID = "morestoragedrawers";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final boolean DEBUG = true;

    public static CompTierRegistry compRegistry;

    public MoreStorageDrawers() {
        logInfo("Loading MoreStorageDrawers");
        //IEventBus eb = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.spec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.spec);
        logLoadedMods();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onModQueueEvent);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onModConfigEvent);
        Tiles.BLOCK_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        MoreStorageDrawersPacketHandler.init();
        compRegistry = new CompTierRegistry();
        compRegistry.initialize();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        Registration.bindRenderTypes();
    }

    @SubscribeEvent
    public static void registerEntityRenderers(final EntityRenderersEvent.RegisterRenderers evt) {
        evt.registerBlockEntityRenderer(Tiles.MORE_DRAWERS_1.get(), BlockEntityDrawersRenderer::new);
        evt.registerBlockEntityRenderer(Tiles.MORE_DRAWERS_2.get(), BlockEntityDrawersRenderer::new);
        evt.registerBlockEntityRenderer(Tiles.MORE_DRAWERS_4.get(), BlockEntityDrawersRenderer::new);
    }

    @SuppressWarnings("Convert2MethodRef")
    private void onModQueueEvent(final InterModEnqueueEvent event) {
        // Nothing for now
    }

    private void onModConfigEvent(final ModConfigEvent event) {
        if (event.getConfig().getType() == ModConfig.Type.COMMON) {
            // NYI: CommonConfig.setLoaded();
        }
        if (event.getConfig().getType() == ModConfig.Type.CLIENT) {
            // NYI: ClientConfig.setLoaded();
        }
    }

    public static void logInfo(String info) {
        if (DEBUG)
            LOGGER.info(info);
    }

    public static void logLoadedMods() {
        for (EnumMod mod : EnumMod.values()) {
            if (mod.isLoaded()) {
                logInfo("MoreStorageDrawers: " + mod.getSerializedName() + " mod loaded");
            }
        }
    }
}