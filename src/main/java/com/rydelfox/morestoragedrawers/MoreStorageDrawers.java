package com.rydelfox.morestoragedrawers;

import com.jaquadro.minecraft.storagedrawers.config.ClientConfig;
import com.jaquadro.minecraft.storagedrawers.config.CommonConfig;
import com.jaquadro.minecraft.storagedrawers.config.CompTierRegistry;
import com.jaquadro.minecraft.storagedrawers.core.ClientProxy;
import com.jaquadro.minecraft.storagedrawers.core.CommonProxy;
import com.rydelfox.morestoragedrawers.block.ModBlocks;
import com.rydelfox.morestoragedrawers.client.renderer.TileEntityDrawersRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MoreStorageDrawers.MOD_ID)
public class MoreStorageDrawers {
    // Directly reference a log4j logger.
    public static final String MOD_ID = "morestoragedrawers";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final boolean DEBUG = true;

    public static CommonProxy proxy;
    public static CompTierRegistry compRegistry;

    public MoreStorageDrawers() {
        logInfo("Loading MoreStorageDrawers");
        proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
        //IEventBus eb = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.spec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.spec);
        //eb.addGenericListener(Block.class, ModBlocks::registerBlocks);
        //eb.addGenericListener(Item.class, ModBlocks::registerItems);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onModQueueEvent);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onModConfigEvent);
        // todo: automatic Recipes

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup (final FMLCommonSetupEvent event) {
        //MessageHandler.init()
        compRegistry = new CompTierRegistry();
        compRegistry.initialize();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ModBlocks.Registration.bindRenderTypes();
        logInfo("Register renderers in clientSetup");
        MoreStorageDrawers.logInfo("STANDARD_DRAWERS_1 is "+ ModBlocks.Tile.STANDARD_DRAWERS_1.getRegistryName().getNamespace()+":"+ ModBlocks.Tile.STANDARD_DRAWERS_1.getRegistryName().getPath());
        ClientRegistry.bindTileEntityRenderer(ModBlocks.Tile.STANDARD_DRAWERS_1, TileEntityDrawersRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModBlocks.Tile.STANDARD_DRAWERS_2, TileEntityDrawersRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModBlocks.Tile.STANDARD_DRAWERS_4, TileEntityDrawersRenderer::new);
    }

    @SuppressWarnings("Convert2MethodRef")
    private void onModQueueEvent(final InterModEnqueueEvent event) {
        // Nothing for now
    }

    private void onModConfigEvent(final ModConfig.ModConfigEvent event) {
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
}
