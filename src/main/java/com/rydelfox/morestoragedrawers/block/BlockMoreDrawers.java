package com.rydelfox.morestoragedrawers.block;

//import com.jaquadro.minecraft.storagedrawers.block.BlockStandardDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import com.rydelfox.morestoragedrawers.block.BlockStandardDrawers;
import com.rydelfox.morestoragedrawers.block.tile.TileEntityDrawersMore;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.world.IBlockReader;

public class BlockMoreDrawers extends BlockStandardDrawers {
    public BlockMoreDrawers (int drawerCount, boolean halfDepth, int storageUnits, AbstractBlock.Properties properties) {
        super(drawerCount, halfDepth, storageUnits, properties);
    }

    public BlockMoreDrawers (int drawerCount, boolean halfDepth, AbstractBlock.Properties properties) {
        super(drawerCount, halfDepth, calcUnits(drawerCount, halfDepth), properties);
    }

    private static int calcUnits(int drawerCount, boolean halfDepth) {
        return halfDepth ? 4 / drawerCount : 8 / drawerCount;
    }

    @Override
    public TileEntityDrawers createTileEntity (BlockState state, IBlockReader world) {
        return TileEntityDrawersMore.createEntity(getDrawerCount());
    }
}
