package com.rydelfox.morestoragedrawers.block;

//import com.jaquadro.minecraft.storagedrawers.block.BlockStandardDrawers;
import com.rydelfox.morestoragedrawers.block.tile.TileEntityDrawersMore;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class BlockMoreDrawers extends BlockStandardDrawers {
    public BlockMoreDrawers (int drawerCount, boolean halfDepth, int storageUnits, BlockBehaviour.Properties properties) {
        super(drawerCount, halfDepth, storageUnits, properties);
    }

    public BlockMoreDrawers (int drawerCount, boolean halfDepth, BlockBehaviour.Properties properties) {
        super(drawerCount, halfDepth, calcUnits(drawerCount, halfDepth), properties);
    }

    private static int calcUnits(int drawerCount, boolean halfDepth) {
        return halfDepth ? 4 / drawerCount : 8 / drawerCount;
    }

    @Override
    public TileEntityDrawersMore newBlockEntity(BlockPos pos, BlockState state) {
        return TileEntityDrawersMore.createEntity(getDrawerCount(), pos, state);
    }
}