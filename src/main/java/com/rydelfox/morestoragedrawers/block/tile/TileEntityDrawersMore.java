package com.rydelfox.morestoragedrawers.block.tile;

import com.jaquadro.minecraft.storagedrawers.api.event.DrawerPopulatedEvent;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerAttributes;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawersStandard;
import com.jaquadro.minecraft.storagedrawers.block.tile.tiledata.StandardDrawerGroup;
import com.rydelfox.morestoragedrawers.block.ModBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityDrawersMore extends TileEntityDrawersStandard {

    @CapabilityInject(IDrawerAttributes.class)
    static Capability<IDrawerAttributes> DRAWER_ATTRIBUTES_CAPABILITY = null;

    public TileEntityDrawersMore (TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public static class Slot1 extends  TileEntityDrawersMore {
        private GroupData groupData = new GroupData(1);

        public Slot1 () {
            super(ModBlocks.Tile.STANDARD_DRAWERS_1);
            groupData.setCapabilityProvider(this);
            injectPortableData(groupData);
        }

        @Override
        public IDrawerGroup getGroup() {
            return groupData;
        }

        @Override
        protected void onAttributeChanged() {
            super.onAttributeChanged();
            groupData.syncAttributes();
        }
    }

    public static class Slot2 extends  TileEntityDrawersMore {
        private GroupData groupData = new GroupData(2);

        public Slot2 () {
            super(ModBlocks.Tile.STANDARD_DRAWERS_2);
            groupData.setCapabilityProvider(this);
            injectPortableData(groupData);
        }

        @Override
        public IDrawerGroup getGroup() {
            return groupData;
        }

        @Override
        protected void onAttributeChanged() {
            super.onAttributeChanged();
            groupData.syncAttributes();
        }
    }

    public static class Slot4 extends  TileEntityDrawersMore {
        private GroupData groupData = new GroupData(4);

        public Slot4 () {
            super(ModBlocks.Tile.STANDARD_DRAWERS_4);
            groupData.setCapabilityProvider(this);
            injectPortableData(groupData);
        }

        @Override
        public IDrawerGroup getGroup() {
            return groupData;
        }

        @Override
        protected void onAttributeChanged() {
            super.onAttributeChanged();
            groupData.syncAttributes();
        }
    }

    public static TileEntityDrawersMore createEntity(int slotCount) {
        switch (slotCount) {
            case 1:
                return new Slot1();
            case 2:
                return new Slot2();
            case 4:
                return new Slot4();
            default:
                return null;
        }
    }

    private class GroupData extends StandardDrawerGroup {
        private final LazyOptional<?> attributesHandler = LazyOptional.of(TileEntityDrawersMore.this::getDrawerAttributes);

        public GroupData (int slotCount) {
            super(slotCount);
        }

        @Nonnull
        @Override
        protected DrawerData createDrawer (int slot) {
            return new StandardDrawerData(this, slot);
        }

        @Override
        public boolean isGroupValid() {
            return TileEntityDrawersMore.this.isGroupValid();
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
            if (capability == TileEntityDrawersMore.DRAWER_ATTRIBUTES_CAPABILITY) {
                return attributesHandler.cast();
            }
            return super.getCapability(capability, facing);
        }
    }

    private class StandardDrawerData extends StandardDrawerGroup.DrawerData {
        private int slot;

        public StandardDrawerData (StandardDrawerGroup group, int slot) {
            super(group);
            this.slot = slot;
        }

        @Override
        protected int getStackCapacity() {
            return upgrades().getStorageMultiplier() * getEffectiveDrawerCapacity();
        }

        @Override
        protected void onItemChanged() {
            DrawerPopulatedEvent event = new DrawerPopulatedEvent(this);
            MinecraftForge.EVENT_BUS.post(event);

            if (getLevel() != null && !getLevel().isClientSide) {
                syncClientCount(slot, getStoredItemCount());
                setChanged();
            }
        }
    }

}
