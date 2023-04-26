package com.rydelfox.morestoragedrawers.block;

import com.rydelfox.morestoragedrawers.config.EnumToggle;
import net.minecraft.util.StringRepresentable;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nonnull;

public enum EnumMod implements StringRepresentable {
    DEFAULT("none", DrawerMaterial.DEFAULT),
    ARSNOUVEAU("ars_nouveau", DrawerMaterial.ARSNOUVEAU_ARCHWOOD),
    //ASTRALSORCERY("astralsorcery", DrawerMaterial.ASTRALSORCERY_INFUSED),
    BOTANIA("botania", DrawerMaterial.BOTANIA_LIVINGWOOD),
    //EIDOLON("eidolon", DrawerMaterial.EIDOLON_POLISHED),
    //HEXBLADES("hexblades", DrawerMaterial.HEXBLADES_DARK_POLISHED),
    NATURESAURA("naturesaura", DrawerMaterial.NATURESAURA_ANCIENT),
    TWILIGHTFOREST("twilightforest", DrawerMaterial.TWILIGHTFOREST_TOWER),
    BIOMESOPLENTY("biomesoplenty", DrawerMaterial.BIOMESOPLENTY_FIR),
    //BETTERENDFORGE("betterendforge", DrawerMaterial.BETTERENDFORGE_MOSSY_GLOWSHRROM),
    //DESOLATION("desolation", DrawerMaterial.DESOLATION_CHARRED),
    BIOMESYOULLGO("byg", DrawerMaterial.BIOMESYOULLGO_ASPEN),
    //OUTEREND("outer_end", DrawerMaterial.OUTEREND_AZURE),
    //WILDNATURE("wild_nature", DrawerMaterial.WILDNATURE_ORANGE_BIND);
    ;

    private String id;
    private DrawerMaterial defaultMaterial;

    EnumMod (String modId, DrawerMaterial defaultMaterial) {
        this.id = modId;
        this.defaultMaterial = defaultMaterial;
    }

    @Override
    @Nonnull
    public String getSerializedName() {
        return id;
    }

    public DrawerMaterial getDefaultMaterial() {
        return defaultMaterial;
    }

    public boolean isLoaded() {
        if (this == DEFAULT)
            return false;
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