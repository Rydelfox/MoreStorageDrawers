package com.rydelfox.morestoragedrawers.block.tile;

import com.jaquadro.minecraft.storagedrawers.StorageDrawers;
import com.jaquadro.minecraft.storagedrawers.api.event.DrawerPopulatedEvent;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawer;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerAttributes;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawersStandard;
import com.jaquadro.minecraft.storagedrawers.block.tile.tiledata.StandardDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.inventory.ItemStackHelper;
import com.rydelfox.morestoragedrawers.MoreStorageDrawers;
import com.rydelfox.morestoragedrawers.block.ModBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class TileEntityDrawersMore extends TileEntityDrawers {

    @CapabilityInject(IDrawerAttributes.class)
    static Capability<IDrawerAttributes> DRAWER_ATTRIBUTES_CAPABILITY = null;

    private long lastClickTime;
    private UUID lastClickUUID;

    public TileEntityDrawersMore (TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public static class Slot1 extends TileEntityDrawersMore {
        private GroupData groupData = new GroupData(1);

        public Slot1 () {
            super(ModBlocks.Tile.STANDARD_DRAWERS_1);
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

        public Slot2 () {
            super(ModBlocks.Tile.STANDARD_DRAWERS_2);
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

        public Slot4 () {
            super(ModBlocks.Tile.STANDARD_DRAWERS_4);
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

            if (getLevel() != null && !getLevel().isClientSide) {
                syncClientCount(slot, getStoredItemCount());
                setChanged();
            }
            MoreStorageDrawers.logInfo("StandardDrawerData.onItemChanged");
            MoreStorageDrawers.logInfo("Drawer slot 1 contains"+getGroup().getDrawer(1).getStoredItemPrototype().getItem().getRegistryName().getPath());
            // At this point, client claims to contain air
        }

        @Override
        protected IDrawer setStoredItem(@Nonnull ItemStack itemPrototype, boolean notify) {
            MoreStorageDrawers.logInfo("setStoredItem, "+itemPrototype.getItem().getRegistryName().getPath());
            if(ItemStackHelper.isStackEncoded(itemPrototype)) {
                itemPrototype = ItemStackHelper.decodeItemStackPrototype(itemPrototype);
            }
            MoreStorageDrawers.logInfo("After decode, "+itemPrototype.getItem().getRegistryName().getPath());

            itemPrototype = ItemStackHelper.getItemPrototype(itemPrototype);
            MoreStorageDrawers.logInfo("After getItemPrototype, "+itemPrototype.getItem().getRegistryName().getPath());
            if (itemPrototype.isEmpty()) {
                reset(notify);
                return this;
            }

            setStoredItemRaw(itemPrototype);
            return this;
        }
    }

    @Override
    public int putItemsIntoSlot (int slot, @Nonnull ItemStack stack, int count) {
        MoreStorageDrawers.logInfo("putItemsIntoSlot, item: "+stack.getItem().getRegistryName().getPath());
        IDrawer drawer = getGroup().getDrawer(slot);
        if (!drawer.isEnabled()) {
            MoreStorageDrawers.logInfo("Drawer is disabled");
            return 0;
        }

        if (drawer.isEmpty()) {
            MoreStorageDrawers.logInfo("Storing item");
            drawer = drawer.setStoredItem(stack);
        }

        if (!drawer.canItemBeStored(stack)) {
            MoreStorageDrawers.logInfo("Item can't be stored");
            return 0;
        }

        int countAdded = Math.min(count, stack.getCount());
        if (!super.getDrawerAttributes().isVoid())
            countAdded = Math.min(countAdded, drawer.getRemainingCapacity());

        int newCount = drawer.getStoredItemCount() + countAdded;
        drawer.setStoredItemCount(newCount);
        stack.shrink(countAdded);
        MoreStorageDrawers.logInfo("Drawer contains "+drawer.getStoredItemCount()+" "+drawer.getStoredItemPrototype().getItem().getRegistryName().getPath());

        setChanged();
        MoreStorageDrawers.logInfo("SetChanged. Drawer contains "+drawer.getStoredItemCount()+" "+drawer.getStoredItemPrototype().getItem().getRegistryName().getPath());

        // Safety - sometimes drawers were having items not go in properly
        IDrawer drawer2 = getDrawer(slot);
        IDrawer drawer3 = super.getDrawer(slot);
        if (drawer2 != drawer) {
            MoreStorageDrawers.logInfo("Drawer/Group Drawer Mismatch after insertion");
        }
        if (drawer3 != drawer) {
            MoreStorageDrawers.logInfo("Drawer/Super Drawer Mismatch after insertion");
        }
        if(drawer.getStoredItemCount() == 0) {
            MoreStorageDrawers.logInfo("drawer is empty");
        }
        if(drawer2.getStoredItemCount() == 0) {
            MoreStorageDrawers.logInfo("drawer2 is empty");
        }
        if(drawer3.getStoredItemCount() == 0) {
            MoreStorageDrawers.logInfo("drawer3 is empty");
        }


        return countAdded;
    }

    @Override
    public int interactPutCurrentItemIntoSlot (int slot, PlayerEntity player) {
        MoreStorageDrawers.logInfo("interactPutCurrentItemIntoSlot");
        IDrawer drawer = getGroup().getDrawer(slot);
        IDrawer otherDrawer = super.getDrawer(slot);
        if (otherDrawer != drawer ) {
            MoreStorageDrawers.logInfo("Super drawer mismatch!");
        }
        IDrawer thirdDrawer = getDrawer(slot);
        if (thirdDrawer != drawer)
            MoreStorageDrawers.logInfo("Non-Group drawers mismatch");
        if (!drawer.isEnabled())
            return 0;

        int count = 0;
        ItemStack playerStack = player.inventory.getSelected();
        if (!playerStack.isEmpty())
            count = putItemsIntoSlot(slot, playerStack, playerStack.getCount());
        MoreStorageDrawers.logInfo("interactPutCurrentItemIntoSlot - drawer has "+drawer.getStoredItemPrototype().getItem().getRegistryName().getPath());

        return count;
    }

    @Override
    public int interactPutCurrentInventoryIntoSlot (int slot, PlayerEntity player) {
        MoreStorageDrawers.logInfo("interactPutCurrentInventoryIntoSlot");
        IDrawer drawer = getGroup().getDrawer(slot);
        if (!drawer.isEnabled())
            return 0;

        int count = 0;
        if (!drawer.isEmpty()) {
            for (int i = 0, n = player.inventory.getContainerSize(); i < n; i++) {
                ItemStack subStack = player.inventory.getItem(i);
                if (!subStack.isEmpty()) {
                    int subCount = putItemsIntoSlot(slot, subStack, subStack.getCount());
                    if (subCount > 0 && subStack.getCount() == 0)
                        player.inventory.setItem(i, ItemStack.EMPTY);

                    count += subCount;
                }
            }
        }

        if (count > 0)
            StorageDrawers.proxy.updatePlayerInventory(player);

        MoreStorageDrawers.logInfo("interactPutCurrentInventoryIntoSlot - drawer has "+drawer.getStoredItemPrototype().getItem().getRegistryName().getPath());

        return count;
    }

    @Override
    public int interactPutItemsIntoSlot (int slot, PlayerEntity player) {
        MoreStorageDrawers.logInfo("interactPutItemsIntoSlot");
        int count;
        if (getLevel().getGameTime() - lastClickTime < 10 && player.getUUID().equals(lastClickUUID))
            count = interactPutCurrentInventoryIntoSlot(slot, player);
        else
            count = interactPutCurrentItemIntoSlot(slot, player);

        lastClickTime = getLevel().getGameTime();
        lastClickUUID = player.getUUID();

        IDrawer drawer = getDrawer(slot);
        IDrawer groupDrawer = getGroup().getDrawer(slot);
        MoreStorageDrawers.logInfo("interactPutItemsIntoSlot - drawer has "+drawer.getStoredItemPrototype().getItem().getRegistryName().getPath()+", group drawer has "+groupDrawer.getStoredItemPrototype().getItem().getRegistryName().getPath());

        return count;
    }

}
