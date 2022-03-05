package com.rydelfox.morestoragedrawers.block;

import com.rydelfox.morestoragedrawers.config.EnumToggle;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nonnull;

public enum EnumMod implements IStringSerializable {
    ARSNOUVEAU("ars_nouveau", EnumVariant.ARSNOUVEAU_ARCHWOOD),
    ASTRALSORCERY("astralsorcery", EnumVariant.ASTRALSORCERY_INFUSED),
    BOTANIA("botania", EnumVariant.BOTANIA_LIVINGWOOD);

    private String id;
    private EnumVariant defaultMaterial;

    EnumMod (String modId, EnumVariant defaultMaterial) {
        this.id = modId;
        this.defaultMaterial = defaultMaterial;
    }

    @Override
    @Nonnull
    public String getSerializedName() {
        return id;
    }

    public EnumVariant getDefaultMaterial() {
        return defaultMaterial;
    }

    public boolean isLoaded() {
        return ModList.get().isLoaded(id);
    }

    public boolean isEnabled (EnumToggle toggle) {
        switch (toggle) {
            case ENABLED:
                return true;
            case DISABLED:
                return false;
            case AUTO:
            default:
                return isLoaded();
        }
    }

    public static EnumMod byId (String id) {
        for (EnumMod mod : values()) {
            if (mod.getSerializedName().equals(id))
                return mod;
        }
        return null;
    }
}
