package com.rydelfox.morestoragedrawers.block;

import com.jaquadro.minecraft.storagedrawers.StorageDrawers;
import com.jaquadro.minecraft.storagedrawers.api.storage.*;
import com.jaquadro.minecraft.storagedrawers.block.BlockDrawers;
import com.jaquadro.minecraft.storagedrawers.config.ClientConfig;
import com.jaquadro.minecraft.storagedrawers.config.CommonConfig;
import com.jaquadro.minecraft.storagedrawers.inventory.ContainerDrawers1;
import com.jaquadro.minecraft.storagedrawers.inventory.ContainerDrawers2;
import com.jaquadro.minecraft.storagedrawers.inventory.ContainerDrawers4;
import com.jaquadro.minecraft.storagedrawers.item.ItemKey;
import com.jaquadro.minecraft.storagedrawers.item.ItemUpgrade;
import com.rydelfox.morestoragedrawers.MoreStorageDrawers;
import com.rydelfox.morestoragedrawers.block.tile.TileEntityDrawersMore;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public abstract class BlockDrawersExtended extends BlockDrawers
{

    // TODO: TE.getModelData()
    //public static final IUnlistedProperty<DrawerStateModelData> STATE_MODEL = UnlistedModelData.create(DrawerStateModelData.class);

    private static final VoxelShape AABB_FULL = Block.box(0, 0, 0, 16, 16, 16);
    private static final VoxelShape AABB_NORTH_FULL = Shapes.join(AABB_FULL, Block.box(1, 1, 0, 15, 15, 1), BooleanOp.ONLY_FIRST);
    private static final VoxelShape AABB_SOUTH_FULL = Shapes.join(AABB_FULL, Block.box(1, 1, 15, 15, 15, 16), BooleanOp.ONLY_FIRST);
    private static final VoxelShape AABB_WEST_FULL = Shapes.join(AABB_FULL, Block.box(0, 1, 1, 1, 15, 15), BooleanOp.ONLY_FIRST);
    private static final VoxelShape AABB_EAST_FULL = Shapes.join(AABB_FULL, Block.box(15, 1, 1, 16, 15, 15), BooleanOp.ONLY_FIRST);
    private static final VoxelShape AABB_NORTH_HALF = Block.box(0, 0, 8, 16, 16, 16);
    private static final VoxelShape AABB_SOUTH_HALF = Block.box(0, 0, 0, 16, 16, 8);
    private static final VoxelShape AABB_WEST_HALF = Block.box(8, 0, 0, 16, 16, 16);
    private static final VoxelShape AABB_EAST_HALF = Block.box(0, 0, 0, 8, 16, 16);

    private long ignoreEventTime;

    private static final ThreadLocal<Boolean> inTileLookup = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue () {
            return false;
        }
    };

    public BlockDrawersExtended(int drawerCount, boolean halfDepth, int storageUnits, Block.Properties properties) {
        super(drawerCount, halfDepth, storageUnits, properties);
    }

    @OnlyIn(Dist.CLIENT)
    public void initDynamic () { }

    public AABB[] getSlotGeometry() {
        return super.slotGeometry;
    }

    public AABB getSlotGeometry(int slot) {
        return super.slotGeometry[slot];
    }

    public void setSlotGeometry(int slot, AABB axis) {
        super.slotGeometry[slot] = new AABB(axis.minX, axis.minY, axis.minZ, axis.maxX, axis.maxY, axis.maxZ);
    }

    public AABB[] getCountGeometry() {
        return super.countGeometry;
    }

    public AABB getCountGeometry(int slot) {
        return super.countGeometry[slot];
    }

    public void setCountGeometry(int slot, AABB axis) {
        super.countGeometry[slot] = new AABB(axis.minX, axis.minY, axis.minZ, axis.maxX, axis.maxY, axis.maxZ);
    }

    public AABB[] getLabelGeometry() {
        return super.labelGeometry;
    }

    public AABB getLabelGeometry(int slot) {
        return super.labelGeometry[slot];
    }

    public void setLabelGeometry(int slot, AABB axis) {
        super.labelGeometry[slot] = new AABB(axis.minX, axis.minY, axis.minZ, axis.maxX, axis.maxY, axis.maxZ);
    }

    public AABB[] getIndGeometry() {
        return super.indGeometry;
    }

    public AABB getIndGeometry(int slot) {
        return super.indGeometry[slot];
    }

    public void setIndGeometry(int slot, AABB axis) {
        super.indGeometry[slot] = new AABB(axis.minX, axis.minY, axis.minZ, axis.maxX, axis.maxY, axis.maxZ);
    }

    public AABB[] getIndBaseGeometry() {
        return super.indBaseGeometry;
    }

    public AABB getIndBaseGeometry(int slot) {
        return super.indBaseGeometry[slot];
    }

    public void setIndBaseGeometry(int slot, AABB axis) {
        super.indBaseGeometry[slot] = new AABB(axis.minX, axis.minY, axis.minZ, axis.maxX, axis.maxY, axis.maxZ);
    }

    @Override
    public void setPlacedBy  (Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
        super.setPlacedBy(world, pos, state, entity, stack);
    }

    @Override
    public InteractionResult use (BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack item = player.getItemInHand(hand);

        if (!(getTileEntitySafe(world, pos) instanceof TileEntityDrawersMore)) {
            return super.use(state, world, pos, player, hand, hit);
        }
        if (hand == InteractionHand.OFF_HAND)
            return InteractionResult.PASS;

        if (world.isClientSide  && Util.getMillis() == ignoreEventTime) {
            ignoreEventTime = 0;
            return InteractionResult.PASS;
        }

        TileEntityDrawersMore tileDrawers = (TileEntityDrawersMore)getTileEntitySafe(world, pos);

        //if (!SecurityManager.hasAccess(player.getGameProfile(), tileDrawers))
        //    return false;

        if (CommonConfig.GENERAL.debugTrace.get()) {
            StorageDrawers.log.info("BlockDrawers.onBlockActivated");
            StorageDrawers.log.info((item.isEmpty()) ? "  null item" : "  " + item.toString());
        }


        if (!item.isEmpty()) {
            if (item.getItem() instanceof ItemKey)
                return InteractionResult.PASS;

            /*if (item.getItem() instanceof ItemTrim && player.isSneaking()) {
                if (!retrimBlock(world, pos, item))
                    return false;
                if (!player.capabilities.isCreativeMode) {
                    item.shrink(1);
                    if (item.getCount() <= 0)
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
                }
                return true;
            }*/
            if (item.getItem() instanceof ItemUpgrade) {
                if (!tileDrawers.upgrades().canAddUpgrade(item)) {
                    if (!world.isClientSide)
                        player.displayClientMessage(new TranslatableComponent("message.storagedrawers.cannot_add_upgrade"), true);

                    return InteractionResult.PASS;
                }

                if (!tileDrawers.upgrades().addUpgrade(item)) {
                    if (!world.isClientSide)
                        player.displayClientMessage(new TranslatableComponent("message.storagedrawers.max_upgrades"), true);

                    return InteractionResult.PASS;
                }

                world.sendBlockUpdated(pos, state, state, 3);

                if (!player.isCreative()) {
                    item.shrink(1);
                    if (item.getCount() <= 0)
                        player.getInventory().setItem(player.getInventory().selected, ItemStack.EMPTY);
                }

                return InteractionResult.SUCCESS;
            }
            /*else if (item.getItem() instanceof ItemPersonalKey) {
                String securityKey = ((ItemPersonalKey) item.getItem()).getSecurityProviderKey(item.getItemDamage());
                ISecurityProvider provider = StorageDrawers.securityRegistry.getProvider(securityKey);
                if (tileDrawers.getOwner() == null) {
                    tileDrawers.setOwner(player.getPersistentID());
                    tileDrawers.setSecurityProvider(provider);
                }
                else if (SecurityManager.hasOwnership(player.getGameProfile(), tileDrawers)) {
                    tileDrawers.setOwner(null);
                    tileDrawers.setSecurityProvider(null);
                }
                else
                    return false;
                return true;
            }*/
        }
        else if (item.isEmpty() && player.isShiftKeyDown()) {
            MoreStorageDrawers.logInfo("Empty hand and sneaking");
            /*if (tileDrawers.isSealed()) {
                tileDrawers.setIsSealed(false);
                return true;
            }
            else if (StorageDrawers.config.cache.enableDrawerUI) {
                player.openGui(StorageDrawers.instance, GuiHandler.drawersGuiID, world, pos.getX(), pos.getY(), pos.getZ());
                return true;
            }*/

            if (CommonConfig.GENERAL.enableUI.get() && !world.isClientSide) {
                NetworkHooks.openGui((ServerPlayer)player, new MenuProvider()
                {
                    @Override
                    public Component getDisplayName () {
                        return new TranslatableComponent(getDescriptionId());
                    }

                    @Nullable
                    @Override
                    public AbstractContainerMenu createMenu (int windowId, Inventory playerInv, Player playerEntity) {
                        if (BlockDrawersExtended.super.getDrawerCount() == 1)
                            return new ContainerDrawers1(windowId, playerInv, tileDrawers);
                        else if (BlockDrawersExtended.super.getDrawerCount() == 2)
                            return new ContainerDrawers2(windowId, playerInv, tileDrawers);
                        else if (BlockDrawersExtended.super.getDrawerCount() == 4)
                            return new ContainerDrawers4(windowId, playerInv, tileDrawers);
                        return null;
                    }
                }, extraData -> {
                    extraData.writeBlockPos(pos);
                });
                return InteractionResult.SUCCESS;
            }
        }

        if (state.getValue(FACING) != hit.getDirection())
            return InteractionResult.PASS;

        //if (tileDrawers.isSealed())
        //    return false;

        int slot = getDrawerSlot(hit);
        IDrawer drawer = tileDrawers.getGroup().getDrawer(slot);
        tileDrawers.interactPutItemsIntoSlot(slot, player);

        if (item.isEmpty())
            player.setItemInHand(hand, ItemStack.EMPTY);


        drawer = tileDrawers.getGroup().getDrawer(slot);

        return InteractionResult.SUCCESS;
    }

    private Vec3 normalizeHitVec (Vec3 hit) {
        return new Vec3(
                ((hit.x < 0) ? hit.x - Math.floor(hit.x) : hit.x) % 1,
                ((hit.y < 0) ? hit.y - Math.floor(hit.y) : hit.y) % 1,
                ((hit.z < 0) ? hit.z - Math.floor(hit.z) : hit.z) % 1
        );
    }

    @Override
    public void attack(BlockState state, Level worldIn, BlockPos pos, Player playerIn) {
        if(!(getTileEntitySafe(worldIn, pos) instanceof TileEntityDrawersMore)) {
            super.attack(state, worldIn, pos, playerIn);
        }
        if (worldIn.isClientSide)
            return;

        if (CommonConfig.GENERAL.debugTrace.get())
            StorageDrawers.log.info("onBlockClicked");

        BlockHitResult rayResult = rayTraceEyes(worldIn, playerIn, playerIn.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue() + 1);
        if (rayResult.getType() == HitResult.Type.MISS)
            return;

        Direction side = rayResult.getDirection();

        TileEntityDrawersMore tileDrawers = (TileEntityDrawersMore)getTileEntitySafe(worldIn, pos);
        if (state.getValue(FACING) != rayResult.getDirection())
            return;

        //if (tileDrawers.isSealed())
        //    return;

        //if (!SecurityManager.hasAccess(playerIn.getGameProfile(), tileDrawers))
        //    return;

        int slot = getDrawerSlot(rayResult);
        IDrawer drawer = tileDrawers.getDrawer(slot);

        ItemStack item;
        boolean invertShift = ClientConfig.GENERAL.invertShift.get();

        if (playerIn.isShiftKeyDown() != invertShift)
            item = tileDrawers.takeItemsFromSlot(slot, drawer.getStoredItemStackSize());
        else
            item = tileDrawers.takeItemsFromSlot(slot, 1);

        if (CommonConfig.GENERAL.debugTrace.get())
            StorageDrawers.log.info((item.isEmpty()) ? "  null item" : "  " + item.toString());

        if (!item.isEmpty()) {
            if (!playerIn.getInventory().add(item)) {
                dropItemStack(worldIn, pos.relative(side), playerIn, item);
                worldIn.sendBlockUpdated(pos, state, state, 3);
            }
            else
                worldIn.playSound(null, pos.getX() + .5f, pos.getY() + .5f, pos.getZ() + .5f, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, .2f, ((worldIn.random.nextFloat() - worldIn.random.nextFloat()) * .7f + 1) * 2);
        }
    }

    private void dropItemStack (Level world, BlockPos pos, Player player, @Nonnull ItemStack stack) {
        ItemEntity entity = new ItemEntity(world, pos.getX() + .5f, pos.getY() + .3f, pos.getZ() + .5f, stack);
        Vec3 motion = entity.getDeltaMovement();
        entity.push(-motion.x, -motion.y, -motion.z);
        world.addFreshEntity(entity);
    }



    @Override
    public void onRemove (BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntityDrawersMore tile = (TileEntityDrawersMore)getTileEntity(world, pos);

        if (tile != null) {
            /*for (int i = 0; i < tile.upgrades().getSlotCount(); i++) {
                ItemStack stack = tile.upgrades().getUpgrade(i);
                if (!stack.isEmpty()) {
                    if (stack.getItem() instanceof ItemUpgradeCreative)
                        continue;
                    spawnAsEntity(world, pos, stack);
                }
            }*/

            //if (!tile.getDrawerAttributes().isUnlimitedVending())
            //    DrawerInventoryHelper.dropInventoryItems(world, pos, tile.getGroup());
        }

        super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    public List<ItemStack> getDrops (BlockState state, LootContext.Builder builder) {
        List<ItemStack> items = new ArrayList<>();
        items.add(getMainDrop(state, (TileEntityDrawersMore)builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY)));
        return items;
    }

    protected ItemStack getMainDrop (BlockState state, TileEntityDrawersMore tile) {
        ItemStack drop = new ItemStack(this);
        if (tile == null)
            return drop;

        CompoundTag data = drop.getTag();
        if (data == null)
            data = new CompoundTag();

        boolean hasContents = false;
        for (int i = 0; i < tile.getGroup().getDrawerCount(); i++) {
            IDrawer drawer = tile.getGroup().getDrawer(i);
            if (drawer != null && !drawer.isEmpty())
                hasContents = true;
        }
        for (int i = 0; i < tile.upgrades().getSlotCount(); i++) {
            if (!tile.upgrades().getUpgrade(i).isEmpty())
                hasContents = true;
        }

        if (hasContents) {
            CompoundTag tiledata = new CompoundTag();
            tile.save(tiledata);

            tiledata.remove("x");
            tiledata.remove("y");
            tiledata.remove("z");

            data.put("tile", tiledata);
            drop.setTag(data);
        }

        return drop;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getSignal  (BlockState state, BlockGetter blockAccess, BlockPos pos, Direction side) {
        if (!isSignalSource (state))
            return 0;

        TileEntityDrawersMore tile = (TileEntityDrawersMore)getTileEntity(blockAccess, pos);
        if (tile == null || !tile.isRedstone())
            return 0;

        return tile.getRedstoneLevel();
    }


}
