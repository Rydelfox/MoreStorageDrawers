package com.rydelfox.morestoragedrawers.block.tile;

import com.jaquadro.minecraft.storagedrawers.api.event.DrawerPopulatedEvent;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawer;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerAttributes;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.tiledata.StandardDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.inventory.ItemStackHelper;
import com.rydelfox.morestoragedrawers.network.ItemUpdateMessage;
import com.rydelfox.morestoragedrawers.network.MoreStorageDrawersPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

import com.jaquadro.minecraft.storagedrawers.block.tile.tiledata.StandardDrawerGroup.DrawerData;

public class TileEntityDrawersMore extends TileEntityDrawers {

    static Capability<IDrawerAttributes> DRAWER_ATTRIBUTES_CAPABILITY = null;

    private long lastClickTime;
    private UUID lastClickUUID;

    public TileEntityDrawersMore (BlockEntityType<?> tileEntityType, BlockPos pos, BlockState state) {
        super(tileEntityType, pos, state);
    }

    public static class Slot1 extends TileEntityDrawersMore {
        private GroupData groupData = new GroupData(1);

        public Slot1 (BlockPos pos, BlockState state) {
            super(Tiles.Tile.MORE_DRAWERS_1, pos, state);
            groupData.setCapabilityProvider(this);
            injectPortableData(groupData);
        }

        @Override
        public IDrawerGroup getGroup () {
            return groupData;
        }

        @Override
        protected void onAttributeChanged () {
            super.onAttributeChanged();
            groupData.syncAttributes();
        }
    }

    public static class Slot2 extends TileEntityDrawersMore {
        private GroupData groupData = new GroupData(2);

        public Slot2 (BlockPos pos, BlockState state) {
            super(Tiles.Tile.MORE_DRAWERS_2, pos, state);
            groupData.setCapabilityProvider(this);
            injectPortableData(groupData);
        }

        @Override
        public IDrawerGroup getGroup () {
            return groupData;
        }

        @Override
        protected void onAttributeChanged () {
            super.onAttributeChanged();
            groupData.syncAttributes();
        }
    }

    public static class Slot4 extends TileEntityDrawersMore {
        private GroupData groupData = new GroupData(4);

        public Slot4 (BlockPos pos, BlockState state) {
            super(Tiles.Tile.MORE_DRAWERS_4, pos, state);
            groupData.setCapabilityProvider(this);
            injectPortableData(groupData);
        }

        @Override
        public IDrawerGroup getGroup () {
            return groupData;
        }

        @Override
        protected void onAttributeChanged () {
            super.onAttributeChanged();
            groupData.syncAttributes();
        }
    }

    public static TileEntityDrawersMore createEntity(int slotCount, BlockPos pos, BlockState state) {
        switch (slotCount) {
            case 1:
                return new Slot1(pos, state);
            case 2:
                return new Slot2(pos, state);
            case 4:
                return new Slot4(pos, state);
            default:
                return null;
        }
    }

    protected void syncClientItem(int slot, ItemStack item) {
        if (getLevel() != null && getLevel().isClientSide)
            return;

        PacketDistributor.TargetPoint point = new PacketDistributor.TargetPoint(
                getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), 500, getLevel().dimension());
        MoreStorageDrawersPacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> point), new ItemUpdateMessage(getBlockPos(), slot, item));
    }

    @OnlyIn(Dist.CLIENT)
    public void clientUpdateItem (final int slot, final ItemStack item) {
        if(!getLevel().isClientSide)
            return;

        Minecraft.getInstance().tell(() -> TileEntityDrawersMore.this.clientUpdateItemAsync(slot, item));
    }

    @OnlyIn(Dist.CLIENT)
    private void clientUpdateItemAsync (int slot, ItemStack item) {
        IDrawer drawer = getGroup().getDrawer(slot);
        if (drawer.isEnabled() && drawer.getStoredItemPrototype() != item) {
            drawer.setStoredItem(item);
        }
    }

    @Override
    public IDrawerGroup getGroup () {
        return null;
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

            if (getLevel() != null && !getLevel().isClientSide && !getStoredItemPrototype().isEmpty()) {
                syncClientItem(slot, getStoredItemPrototype());
                syncClientCount(slot, getStoredItemCount());
                setChanged();
            }
            // At this point, client claims to contain air
        }

        @Override
        protected void onAmountChanged () {
            if (getLevel() != null && !getLevel().isClientSide) {
                syncClientCount(slot, getStoredItemCount());
                setChanged();
            }
        }

        @Override
        protected IDrawer setStoredItem(@Nonnull ItemStack itemPrototype, boolean notify) {
            if(ItemStackHelper.isStackEncoded(itemPrototype)) {
                itemPrototype = ItemStackHelper.decodeItemStackPrototype(itemPrototype);
            }

            itemPrototype = ItemStackHelper.getItemPrototype(itemPrototype);
            if (itemPrototype.isEmpty()) {
                reset(notify);
                return this;
            }

            setStoredItemRaw(itemPrototype);
            forceUpdate();
            return this;
        }

        @Override
        public void setStoredItemCount (int amount) {
            setStoredItemCount(amount, true);
        }

        @Override
        protected void setStoredItemCount (int amount, boolean notify) {
            if (getStoredItemPrototype().isEmpty() || getStoredItemCount() == amount)
                return;

            if (getRemainingCapacity() == Integer.MAX_VALUE)
                return;

            super.setStoredItemCount(amount, notify);
            forceUpdate();
        }

        protected void forceUpdate() {
            putItemsIntoSlot(slot, ItemStack.EMPTY, 0);
            if(!getLevel().isClientSide) {
                syncClientItem(slot, getStoredItemPrototype());
                syncClientCount(slot, getStoredItemCount());
            }
        }
    }

}
