package com.rydelfox.morestoragedrawers.block;

import com.rydelfox.morestoragedrawers.MoreStorageDrawers;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public enum EnumVariant implements IStringSerializable {
    DEFAULT(null, "default", 0, null, 0),

    ARSNOUVEAU_ARCHWOOD(ID.ARSNOUVEAU, "archwood", 1, "archwood_planks", 0, "archwood_slab", 0);

    private static final Map<ResourceLocation, EnumVariant> RESOURCE_LOOKUP;
    private static final Map<Integer, EnumVariant> INDEX_LOOKUP;

    private final String namespace;
    private final String name;
    private final ResourceLocation resource;
    private final ResourceLocation plankResource;
    private final ResourceLocation slabResource;
    private final int index;
    private final int plankMeta;
    private final int slabMeta;

    EnumVariant (String namespace, String name, int index, String blockId, int blockMeta) {
        this(namespace, name, index, blockId, blockMeta, null, 0);
    }

    EnumVariant(String namespace, String name, int index, String plankId, int plankMeta, String slabId, int slabMeta) {
        this.namespace = namespace;
        this.name = name;
        this.plankResource = plankId != null ? new ResourceLocation(namespace, plankId) : null;
        this.slabResource = slabId != null ? new ResourceLocation(namespace, slabId) : null;
        this.plankMeta = plankMeta;
        this.slabMeta = slabMeta;
        this.resource = new ResourceLocation(namespace, name);
        this.index = index;
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

    public int getPlankMeta() {
        return plankMeta;
    }

    public ResourceLocation getSlabResource() {
        return slabResource;
    }

    public int getSlabMeta() {
        return slabMeta;
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
    }


}
