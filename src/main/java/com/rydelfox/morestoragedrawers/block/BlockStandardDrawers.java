package com.rydelfox.morestoragedrawers.block;

import com.jaquadro.minecraft.storagedrawers.block.tile.BlockEntityDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.BlockEntityDrawersStandard;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class BlockStandardDrawers extends BlockDrawersExtended {
    //public static final EnumProperty<EnumBasicDrawer> BLOCK = EnumProperty.create("block", EnumBasicDrawer.class);

    //@SideOnly(Side.CLIENT)
    //private StatusModelData[] statusInfo;

    public BlockStandardDrawers(int drawerCount, boolean halfDepth, int storageUnits, Block.Properties properties) {
        super(drawerCount, halfDepth, storageUnits, properties);
    }

    public BlockStandardDrawers(int drawerCount, boolean halfDepth, Block.Properties properties) {
        super(drawerCount, halfDepth, calcUnits(drawerCount, halfDepth), properties);
    }

    private static int calcUnits(int drawerCount, boolean halfDepth) {
        return halfDepth ? 4 / drawerCount : 8 / drawerCount;
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public void initDynamic () {
        statusInfo = new StatusModelData[EnumBasicDrawer.values().length];
        for (EnumBasicDrawer type : EnumBasicDrawer.values()) {
            ResourceLocation location = new ResourceLocation(StorageDrawers.MOD_ID + ":models/dynamic/basicDrawers_" + type.getName() + ".json");
            statusInfo[type.getMetadata()] = new StatusModelData(type.getDrawerCount(), location);
        }
    }
    @Override
    @SideOnly(Side.CLIENT)
    public StatusModelData getStatusInfo (IBlockState state) {
        if (state != null) {
            EnumBasicDrawer info = state.getValue(BLOCK);
            if (info != null)
                return statusInfo[info.getMetadata()];
        }
        return null;
    }*/

    @Override
    protected int getDrawerSlot(Direction side, Vec3 hit) {
        if (getDrawerCount() == 1)
            return 0;
        if (getDrawerCount() == 2)
            return hitTop(hit) ? 0 : 1;

        if (hitLeft(side, hit))
            return hitTop(hit) ? 0 : 2;
        else
            return hitTop(hit) ? 1 : 3;
    }

    public BlockEntityDrawers newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityDrawersStandard.createEntity(getDrawerCount(), pos, state);
    }
}