package com.rydelfox.morestoragedrawers.block;

import com.rydelfox.morestoragedrawers.MoreStorageDrawers;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public enum EnumVariant implements IStringSerializable {
    DEFAULT(null, "default", 0, null),

    ARSNOUVEAU_ARCHWOOD(ID.ARSNOUVEAU, "archwood", 1, "archwood_planks", "archwood_slab"),
    ASTRALSORCERY_INFUSED(ID.ASTRALSORCERY, "infused", 2, "infused_wood_planks", "infused_wood_slab", 6, 11, 0),
    BOTANIA_LIVINGWOOD(ID.BOTANIA, "livingwood", 3, "livingwood_planks", "livingwood_planks_slab"),
    BOTANIA_MOSSY_LIVINGWOOD(ID.BOTANIA, "mossy_livingwood", 4, "mossy_livingwood_planks"),
    BOTANIA_DREAMWOOD(ID.BOTANIA, "dreamwood", 5, "dreamwood_planks", "dreamwood_planks_slab"),
    BOTANIA_MOSSY_DREAMWOOD(ID.BOTANIA, "mossy_dreamwood", 6, "mossy_dreamwood_planks"),
    BOTANIA_SHIMMERWOOD(ID.BOTANIA, "shimmerwood", 7, "shimmerwood_planks", "shimmerwood_planks_slab"),
    EIDOLON_POLISHED(ID.EIDOLON, "polished", 8, "polished_planks", "polished_planks_slab", 4, 5, 0),
    HEXBLADES_DARK_POLISHED(ID.HEXBLADES, "dark_polished", 8, "dark_polished_planks", "dark_polished_planks_slab", 4, 5, 0);

    private static final Map<ResourceLocation, EnumVariant> RESOURCE_LOOKUP;
    private static final Map<Integer, EnumVariant> INDEX_LOOKUP;

    private final String namespace;
    private final String name;
    private final ResourceLocation resource;
    private final ResourceLocation plankResource;
    private final ResourceLocation slabResource;
    private final int index;
    private final int hardness;
    private final int blastResistance;
    private final int light;

    EnumVariant (String namespace, String name, int index, String blockId) {
        this(namespace, name, index, blockId, null);
    }

    EnumVariant(String namespace, String name, int index, String plankId, String slabId) {
        this(namespace, name,index, plankId, slabId, 5, 5, 0);
    }

    EnumVariant(String namespace, String name, int index, String plankId, String slabId, int hardness, int blastResistance, int light) {
        this.namespace = namespace;
        this.name = name;
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

    @Nonnull
    public static EnumVariant byResource(String resource) {
        EnumVariant variant = RESOURCE_LOOKUP.get(new ResourceLocation(resource));
        return variant != null ? variant : DEFAULT;
    }

    @Nonnull
    public static EnumVariant byGroupMeta(int group, int meta) {
        EnumVariant varient = INDEX_LOOKUP.get(group * 16 + meta);
        return varient != null ? varient : DEFAULT;
    }

    static {
        RESOURCE_LOOKUP = new HashMap<>();
        INDEX_LOOKUP = new HashMap<>();

        for (EnumVariant varient : values()) {
            RESOURCE_LOOKUP.put(varient.getResource(), varient);
            INDEX_LOOKUP.put(varient.getIndex(), varient);
        }
    }

    private static class ID {
        public static final String ARSNOUVEAU = "ars_nouveau";
        public static final String ASTRALSORCERY = "astralsorcery";
        public static final String BOTANIA = "botania";
        public static final String EIDOLON = "eidolon";
        public static final String HEXBLADES = "hexblades";
    }


}
