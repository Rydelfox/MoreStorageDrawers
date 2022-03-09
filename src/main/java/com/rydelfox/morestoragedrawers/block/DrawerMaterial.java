package com.rydelfox.morestoragedrawers.block;

import com.jaquadro.minecraft.storagedrawers.block.BlockTrim;
import com.rydelfox.morestoragedrawers.MoreCreative;
import com.rydelfox.morestoragedrawers.MoreStorageDrawers;
import com.rydelfox.morestoragedrawers.block.tile.TileEntityDrawersMore;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum DrawerMaterial implements IStringSerializable {
    DEFAULT(ID.DEFAULT, "default", "NO MATERIAL", 0, null),

    ARSNOUVEAU_ARCHWOOD(ID.ARSNOUVEAU, "archwood", "Archwood", 1, "archwood_planks", "archwood_slab"),
    ASTRALSORCERY_INFUSED(ID.ASTRALSORCERY, "infused", "Infused", 2, "infused_wood_planks", "infused_wood_slab", 6, 11, 0),
    BOTANIA_LIVINGWOOD(ID.BOTANIA, "livingwood", "Livingwood", 3, "livingwood_planks", "livingwood_planks_slab"),
    BOTANIA_MOSSY_LIVINGWOOD(ID.BOTANIA, "mossy_livingwood", "Mossy Livingwood", 4, "mossy_livingwood_planks"),
    BOTANIA_DREAMWOOD(ID.BOTANIA, "dreamwood", "Dreamwood", 5, "dreamwood_planks", "dreamwood_planks_slab"),
    BOTANIA_MOSSY_DREAMWOOD(ID.BOTANIA, "mossy_dreamwood", "Mossy Dreamwood", 6, "mossy_dreamwood_planks"),
    BOTANIA_SHIMMERWOOD(ID.BOTANIA, "shimmerwood", "Shimmerwood", 7, "shimmerwood_planks", "shimmerwood_planks_slab"),
    EIDOLON_POLISHED(ID.EIDOLON, "polished", "Polished", 8, "polished_planks", "polished_planks_slab", 4, 5, 0),
    HEXBLADES_DARK_POLISHED(ID.HEXBLADES, "dark_polished", "Dark Polished", 9, "dark_polished_planks", "dark_polished_planks_slab", 4, 5, 0),
    NATURESAURA_ANCIENT(ID.NATURESAURA, "ancient", "Ancient", 10, "ancient_planks", "ancient_slab"),
    TWILIGHTFOREST_TOWER(ID.TWILIGHTFOREST, "tower", "Towerwood", 11, "tower_wood", null, 25, 20, 0),
    TWILIGHTFOREST_TWILIGHT(ID.TWILIGHTFOREST, "twilight", "Twilight Oak", 12, "twilight_oak_planks", "twilight_oak_slab"),
    TWILIGHTFOREST_CANOPY(ID.TWILIGHTFOREST, "canopy", "Canopy", 13, "canopy_planks", "canopy_slab"),
    TWILIGHTFOREST_MANGROVE(ID.TWILIGHTFOREST, "mangrove", "Mangrove", 14, "mangrove_planks", "mangrove_slab"),
    TWILIGHTFOREST_DARK(ID.TWILIGHTFOREST, "dark", "Darkwood", 15, "dark_planks", "dark_slab"),
    TWILIGHTFOREST_TIME(ID.TWILIGHTFOREST, "time", "Timewood", 16, "time_planks", "time_slab"),
    TWILIGHTFOREST_TRANS(ID.TWILIGHTFOREST, "trans", "Transwood", 17, "trans_planks", "trans_slab"),
    TWILIGHTFOREST_MINE(ID.TWILIGHTFOREST, "mine", "Minewood", 18, "mine_planks", "mine_slab"),
    TWILIGHTFOREST_SORT(ID.TWILIGHTFOREST, "sort", "Sortingwood", 19, "sort_planks", "sort_slab"),
    BIOMESOPLENTY_FIR(ID.BIOMESOPLENTY, "fir", "Fir", 20, "fir_planks", "fir_slab"),
    BIOMESOPLENTY_REDWOOD(ID.BIOMESOPLENTY, "redwood", "Redwood", 21, "redwood_planks", "redwood_slab"),
    BIOMESOPLENTY_CHERRY(ID.BIOMESOPLENTY, "cherry", "Cherry", 22, "cherry_planks", "cherry_slab"),
    BIOMESOPLENTY_MAHOGANY(ID.BIOMESOPLENTY, "mahogany", "Mahogany", 23, "mahogany_planks", "mahogany_slab"),
    BIOMESOPLENTY_JACARANDA(ID.BIOMESOPLENTY, "jacaranda", "Jacaranda", 24, "jacaranda_planks", "jacaranda_slab"),
    BIOMESOPLENTY_PALM(ID.BIOMESOPLENTY, "palm", "Palm", 25, "palm_planks", "palm_slab"),
    BIOMESOPLENTY_WILLOW(ID.BIOMESOPLENTY, "willow", "Willow", 26, "willow_planks", "willow_slab"),
    BIOMESOPLENTY_DEAD(ID.BIOMESOPLENTY, "dead", "Deadwood", 27, "dead_planks", "dead_slab"),
    BIOMESOPLENTY_MAGIC(ID.BIOMESOPLENTY, "magic", "Magic Wood", 28, "magic_planks", "magic_slab"),
    BIOMESOPLENTY_UMBRAN(ID.BIOMESOPLENTY, "umbran", "Umbran", 29, "umbran_planks", "umbran_slab"),
    BIOMESOPLENTY_HELLBARK(ID.BIOMESOPLENTY, "hellbark", "Hellbark", 30, "hellbark_planks", "hellbark_slab");

    private static final Map<ResourceLocation, DrawerMaterial> RESOURCE_LOOKUP;
    private static final Map<Integer, DrawerMaterial> INDEX_LOOKUP;

    private final String namespace;
    private final String name;
    private final String englishName;
    private final ResourceLocation resource;
    private final ResourceLocation plankResource;
    private final ResourceLocation slabResource;
    private final int index;
    private final int hardness;
    private final int blastResistance;
    private final int light;

    private RegistryObject<Block> regBlockTrim = null;
    private RegistryObject<Block> regFullOne = null;
    private RegistryObject<Block> regFullTwo = null;
    private RegistryObject<Block> regFullFour = null;
    private RegistryObject<Block> regHalfOne = null;
    private RegistryObject<Block> regHalfTwo = null;
    private RegistryObject<Block> regHalfFour = null;

    private Block blockTrim = null;
    private Block blockFullOne = null;
    private Block blockFullTwo = null;
    private Block blockFullFour = null;
    private Block blockHalfOne = null;
    private Block blockHalfTwo = null;
    private Block blockHalfFour = null;

    private Item itemTrim = null;
    private Item itemFullOne = null;
    private Item itemFullTwo = null;
    private Item itemFullFour = null;
    private Item itemHalfOne = null;
    private Item itemHalfTwo = null;
    private Item itemHalfFour = null;

    private TileEntityDrawersMore tileFullOne = null;
    private TileEntityDrawersMore tileFullTwo = null;
    private TileEntityDrawersMore tileFullFour = null;
    private TileEntityDrawersMore tileHalfOne = null;
    private TileEntityDrawersMore tileHalfTwo = null;
    private TileEntityDrawersMore tileHalfFour = null;

    DrawerMaterial(String namespace, String name, String englishName, int index, String blockId) {
        this(namespace, name, englishName, index, blockId, null);
    }

    DrawerMaterial(String namespace, String name, String englishName, int index, String plankId, String slabId) {
        this(namespace, name, englishName, index, plankId, slabId, 5, 5, 0);
    }

    DrawerMaterial(String namespace, String name, String englishName, int index, String plankId, String slabId, int hardness, int blastResistance, int light) {
        this.namespace = namespace;
        this.name = name;
        this.englishName = englishName;
        this.plankResource = plankId != null ? new ResourceLocation(namespace, plankId) : null;
        this.slabResource = slabId != null ? new ResourceLocation(namespace, slabId) : null;
        this.resource = new ResourceLocation(namespace, name);
        this.index = index;
        this.hardness = hardness;
        this.blastResistance = blastResistance;
        this.light = light;
    }

    @Nonnull
    public String getNamespace() {
        return resource.getNamespace();
    }

    @Nonnull
    public String getPath() {
        return resource.getPath();
    }

    @Override
    @Nonnull
    public String getSerializedName() {
        return resource.toString();
    }

    public EnumMod getMod() {
        return EnumMod.byId(namespace);
    }

    @Nonnull
    public ResourceLocation getResource() {
        return resource;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public String prefix() {
        return namespace + "_" + name;
    }

    public ResourceLocation getPlankResource() {
        return plankResource;
    }

    public ResourceLocation getSlabResource() {
        return slabResource;
    }

    public int getIndex() {
        return index;
    }

    public int getGroupIndex() {
        return index / 16;
    }

    public int getGroupMeta() {
        return index % 16;
    }

    public int getHardness() {
        return hardness;
    }

    public int getBlastResistance() {
        return blastResistance;
    }

    public int getLight() {
        return light;
    }

    public String getEnglishName() {
        return englishName;
    }

    public BlockMoreDrawers getDrawer(int slots, boolean halfDepth) {
        if(halfDepth) {
            switch (slots) {
                case 4:
                    return (BlockMoreDrawers) blockHalfFour;
                case 2:
                    return (BlockMoreDrawers) blockHalfTwo;
                default:
                    return (BlockMoreDrawers) blockHalfOne;
            }
        } else {
            switch (slots) {
                case 4:
                    return (BlockMoreDrawers) blockFullFour;
                case 2:
                    return (BlockMoreDrawers) blockFullTwo;
                default:
                    return (BlockMoreDrawers) blockFullOne;
            }
        }
    }

    public BlockTrim getTrim() {
        return (BlockTrim) blockTrim;
    }

    public List<Block> getBlocks() {
        return getBlocks(true);
    }

    public List<Block> getBlocks(boolean includeTrim) {
        List<Block> blocks = new ArrayList<>();
        blocks.add(blockFullOne);
        blocks.add(blockFullTwo);
        blocks.add(blockFullFour);
        blocks.add(blockHalfOne);
        blocks.add(blockHalfTwo);
        blocks.add(blockHalfFour);
        if (includeTrim)
            blocks.add(blockTrim);
        return blocks;
    }

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        items.add(itemFullOne);
        items.add(itemFullTwo);
        items.add(itemFullFour);
        items.add(itemHalfOne);
        items.add(itemHalfTwo);
        items.add(itemHalfFour);
        items.add(itemTrim);
        return items;
    }

    public void registerBlocks(IForgeRegistry<Block> registry) {
        if (this == DEFAULT)
            return;
        if (this.blockTrim != null)
            throw new IllegalStateException(this.getEnglishName()+" blocks have already been registered!");
        AbstractBlock.Properties properties = AbstractBlock.Properties
                .of(Material.WOOD)
                .harvestTool(ToolType.AXE)
                .strength(hardness, blastResistance)
                .lightLevel((p1) -> light)
                .isSuffocating((p1, p2, p3) -> false)
                .isRedstoneConductor((p1, p2, p3) -> false);
        MoreStorageDrawers.logInfo("Registering blocks for "+englishName);
        this.blockTrim = new BlockTrim(properties).setRegistryName(namespace+"_"+name+"_trim");
        this.blockFullOne = new BlockMoreDrawers(1, false, properties).setRegistryName(namespace+"_"+name+"_full_1");
        this.blockFullTwo = new BlockMoreDrawers(2, false, properties).setRegistryName(namespace+"_"+name+"_full_2");
        this.blockFullFour = new BlockMoreDrawers(4, false, properties).setRegistryName(namespace+"_"+name+"_full_4");
        this.blockHalfOne = new BlockMoreDrawers(1, true, properties).setRegistryName(namespace+"_"+name+"_half_1");
        this.blockHalfTwo = new BlockMoreDrawers(2, true, properties).setRegistryName(namespace+"_"+name+"_half_2");
        this.blockHalfFour = new BlockMoreDrawers(4, true, properties).setRegistryName(namespace+"_"+name+"_half_4");

        registry.register(blockTrim);
        registry.register(blockFullOne);
        registry.register(blockFullTwo);
        registry.register(blockFullFour);
        registry.register(blockHalfOne);
        registry.register(blockHalfTwo);
        registry.register(blockHalfFour);
    }

    public void registerItems(IForgeRegistry<Item> registry) {
        if (this == DEFAULT)
            return;
        if(this.itemTrim != null)
            throw new IllegalStateException(this.getEnglishName()+" items have already been registered!");
        if(this.blockTrim == null)
            throw new IllegalStateException("Blocks must be registered before registering items!");

        this.itemTrim = new BlockItem(this.blockTrim, new Item.Properties().tab(MoreCreative.TAB)).setRegistryName(this.blockTrim.getRegistryName());
        this.itemFullOne = new BlockItem(this.blockFullOne, new Item.Properties().tab(MoreCreative.TAB)).setRegistryName(this.blockFullOne.getRegistryName());
        this.itemFullTwo = new BlockItem(this.blockFullTwo, new Item.Properties().tab(MoreCreative.TAB)).setRegistryName(this.blockFullTwo.getRegistryName());
        this.itemFullFour = new BlockItem(this.blockFullFour, new Item.Properties().tab(MoreCreative.TAB)).setRegistryName(this.blockFullFour.getRegistryName());
        this.itemHalfOne = new BlockItem(this.blockHalfOne, new Item.Properties().tab(MoreCreative.TAB)).setRegistryName(this.blockHalfOne.getRegistryName());
        this.itemHalfTwo = new BlockItem(this.blockHalfTwo, new Item.Properties().tab(MoreCreative.TAB)).setRegistryName(this.blockHalfTwo.getRegistryName());
        this.itemHalfFour = new BlockItem(this.blockHalfFour, new Item.Properties().tab(MoreCreative.TAB)).setRegistryName(this.blockHalfFour.getRegistryName());

        registry.register(itemTrim);
        registry.register(itemFullOne);
        registry.register(itemFullTwo);
        registry.register(itemFullFour);
        registry.register(itemHalfOne);
        registry.register(itemHalfTwo);
        registry.register(itemHalfFour);
    }

    @Nonnull
    public static DrawerMaterial byResource(String resource) {
        DrawerMaterial variant = RESOURCE_LOOKUP.get(new ResourceLocation(resource));
        return variant != null ? variant : DEFAULT;
    }

    @Nonnull
    public static DrawerMaterial byGroupMeta(int group, int meta) {
        DrawerMaterial varient = INDEX_LOOKUP.get(group * 16 + meta);
        return varient != null ? varient : DEFAULT;
    }

    static {
        RESOURCE_LOOKUP = new HashMap<>();
        INDEX_LOOKUP = new HashMap<>();

        for (DrawerMaterial varient : values()) {
            RESOURCE_LOOKUP.put(varient.getResource(), varient);
            INDEX_LOOKUP.put(varient.getIndex(), varient);
        }
    }

    private static class ID {
        public static final String DEFAULT = "none";
        public static final String ARSNOUVEAU = "ars_nouveau";
        public static final String ASTRALSORCERY = "astralsorcery";
        public static final String BOTANIA = "botania";
        public static final String EIDOLON = "eidolon";
        public static final String HEXBLADES = "hexblades";
        public static final String NATURESAURA = "naturesaura";
        public static final String TWILIGHTFOREST = "twilightforest";
        public static final String BIOMESOPLENTY = "biomesoplenty";
    }


}
