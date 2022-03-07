package com.rydelfox.morestoragedrawers.block;

import com.jaquadro.minecraft.storagedrawers.StorageDrawers;
import com.jaquadro.minecraft.storagedrawers.api.storage.*;
import com.jaquadro.minecraft.storagedrawers.api.storage.attribute.LockAttribute;
import com.jaquadro.minecraft.storagedrawers.block.BlockDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import com.jaquadro.minecraft.storagedrawers.capabilities.CapabilityDrawerAttributes;
import com.jaquadro.minecraft.storagedrawers.config.ClientConfig;
import com.jaquadro.minecraft.storagedrawers.config.CommonConfig;
import com.jaquadro.minecraft.storagedrawers.core.ModItems;
import com.jaquadro.minecraft.storagedrawers.inventory.ContainerDrawers1;
import com.jaquadro.minecraft.storagedrawers.inventory.ContainerDrawers2;
import com.jaquadro.minecraft.storagedrawers.inventory.ContainerDrawers4;
import com.jaquadro.minecraft.storagedrawers.inventory.ContainerDrawersComp;
import com.jaquadro.minecraft.storagedrawers.item.ItemKey;
import com.jaquadro.minecraft.storagedrawers.item.ItemUpgrade;
import com.rydelfox.morestoragedrawers.MoreStorageDrawers;
import com.rydelfox.morestoragedrawers.block.tile.TileEntityDrawersMore;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class BlockDrawersExtended extends BlockDrawers
{

    // TODO: TE.getModelData()
    //public static final IUnlistedProperty<DrawerStateModelData> STATE_MODEL = UnlistedModelData.create(DrawerStateModelData.class);

    private static final VoxelShape AABB_FULL = Block.box(0, 0, 0, 16, 16, 16);
    private static final VoxelShape AABB_NORTH_FULL = VoxelShapes.join(AABB_FULL, Block.box(1, 1, 0, 15, 15, 1), IBooleanFunction.ONLY_FIRST);
    private static final VoxelShape AABB_SOUTH_FULL = VoxelShapes.join(AABB_FULL, Block.box(1, 1, 15, 15, 15, 16), IBooleanFunction.ONLY_FIRST);
    private static final VoxelShape AABB_WEST_FULL = VoxelShapes.join(AABB_FULL, Block.box(0, 1, 1, 1, 15, 15), IBooleanFunction.ONLY_FIRST);
    private static final VoxelShape AABB_EAST_FULL = VoxelShapes.join(AABB_FULL, Block.box(15, 1, 1, 16, 15, 15), IBooleanFunction.ONLY_FIRST);
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

    public AxisAlignedBB[] getSlotGeometry() {
        return super.slotGeometry;
    }

    public AxisAlignedBB getSlotGeometry(int slot) {
        return super.slotGeometry[slot];
    }

    public void setSlotGeometry(int slot, AxisAlignedBB axis) {
        super.slotGeometry[slot] = new AxisAlignedBB(axis.minX, axis.minY, axis.minZ, axis.maxX, axis.maxY, axis.maxZ);
    }

    public AxisAlignedBB[] getCountGeometry() {
        return super.countGeometry;
    }

    public AxisAlignedBB getCountGeometry(int slot) {
        return super.countGeometry[slot];
    }

    public void setCountGeometry(int slot, AxisAlignedBB axis) {
        super.countGeometry[slot] = new AxisAlignedBB(axis.minX, axis.minY, axis.minZ, axis.maxX, axis.maxY, axis.maxZ);
    }

    public AxisAlignedBB[] getLabelGeometry() {
        return super.labelGeometry;
    }

    public AxisAlignedBB getLabelGeometry(int slot) {
        return super.labelGeometry[slot];
    }

    public void setLabelGeometry(int slot, AxisAlignedBB axis) {
        super.labelGeometry[slot] = new AxisAlignedBB(axis.minX, axis.minY, axis.minZ, axis.maxX, axis.maxY, axis.maxZ);
    }

    public AxisAlignedBB[] getIndGeometry() {
        return super.indGeometry;
    }

    public AxisAlignedBB getIndGeometry(int slot) {
        return super.indGeometry[slot];
    }

    public void setIndGeometry(int slot, AxisAlignedBB axis) {
        super.indGeometry[slot] = new AxisAlignedBB(axis.minX, axis.minY, axis.minZ, axis.maxX, axis.maxY, axis.maxZ);
    }

    public AxisAlignedBB[] getIndBaseGeometry() {
        return super.indBaseGeometry;
    }

    public AxisAlignedBB getIndBaseGeometry(int slot) {
        return super.indBaseGeometry[slot];
    }

    public void setIndBaseGeometry(int slot, AxisAlignedBB axis) {
        super.indBaseGeometry[slot] = new AxisAlignedBB(axis.minX, axis.minY, axis.minZ, axis.maxX, axis.maxY, axis.maxZ);
    }

    @Override
    public void setPlacedBy  (World world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
        super.setPlacedBy(world, pos, state, entity, stack);
    }

    @Override
    public ActionResultType use (BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ItemStack item = player.getItemInHand(hand);

        if (!(getTileEntitySafe(world, pos) instanceof TileEntityDrawersMore)) {
            return super.use(state, world, pos, player, hand, hit);
        }
        if (hand == Hand.OFF_HAND)
            return ActionResultType.PASS;

        if (world.isClientSide  && Util.getMillis() == ignoreEventTime) {
            ignoreEventTime = 0;
            return ActionResultType.PASS;
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
                return ActionResultType.PASS;

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
                        player.displayClientMessage(new TranslationTextComponent("message.storagedrawers.cannot_add_upgrade"), true);

                    return ActionResultType.PASS;
                }

                if (!tileDrawers.upgrades().addUpgrade(item)) {
                    if (!world.isClientSide)
                        player.displayClientMessage(new TranslationTextComponent("message.storagedrawers.max_upgrades"), true);

                    return ActionResultType.PASS;
                }

                world.sendBlockUpdated(pos, state, state, 3);

                if (!player.isCreative()) {
                    item.shrink(1);
                    if (item.getCount() <= 0)
                        player.inventory.setItem(player.inventory.selected, ItemStack.EMPTY);
                }

                return ActionResultType.SUCCESS;
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
                NetworkHooks.openGui((ServerPlayerEntity)player, new INamedContainerProvider()
                {
                    @Override
                    public ITextComponent getDisplayName () {
                        return new TranslationTextComponent(getDescriptionId());
                    }

                    @Nullable
                    @Override
                    public Container createMenu (int windowId, PlayerInventory playerInv, PlayerEntity playerEntity) {
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
                return ActionResultType.SUCCESS;
            }
        }

        if (state.getValue(FACING) != hit.getDirection())
            return ActionResultType.PASS;

        //if (tileDrawers.isSealed())
        //    return false;

        int slot = getDrawerSlot(hit);
        IDrawer drawer = tileDrawers.getGroup().getDrawer(slot);
        tileDrawers.interactPutItemsIntoSlot(slot, player);

        if (item.isEmpty())
            player.setItemInHand(hand, ItemStack.EMPTY);


        drawer = tileDrawers.getGroup().getDrawer(slot);

        return ActionResultType.SUCCESS;
    }

    private Vector3d normalizeHitVec (Vector3d hit) {
        return new Vector3d(
                ((hit.x < 0) ? hit.x - Math.floor(hit.x) : hit.x) % 1,
                ((hit.y < 0) ? hit.y - Math.floor(hit.y) : hit.y) % 1,
                ((hit.z < 0) ? hit.z - Math.floor(hit.z) : hit.z) % 1
        );
    }

    @Override
    public void attack(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn) {
        if(!(getTileEntitySafe(worldIn, pos) instanceof TileEntityDrawersMore)) {
            super.attack(state, worldIn, pos, playerIn);
        }
        if (worldIn.isClientSide)
            return;

        if (CommonConfig.GENERAL.debugTrace.get())
            StorageDrawers.log.info("onBlockClicked");

        BlockRayTraceResult rayResult = rayTraceEyes(worldIn, playerIn, playerIn.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue() + 1);
        if (rayResult.getType() == RayTraceResult.Type.MISS)
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
            if (!playerIn.inventory.add(item)) {
                dropItemStack(worldIn, pos.relative(side), playerIn, item);
                worldIn.sendBlockUpdated(pos, state, state, 3);
            }
            else
                worldIn.playSound(null, pos.getX() + .5f, pos.getY() + .5f, pos.getZ() + .5f, SoundEvents.ITEM_PICKUP, SoundCategory.PLAYERS, .2f, ((worldIn.random.nextFloat() - worldIn.random.nextFloat()) * .7f + 1) * 2);
        }
    }

    private void dropItemStack (World world, BlockPos pos, PlayerEntity player, @Nonnull ItemStack stack) {
        ItemEntity entity = new ItemEntity(world, pos.getX() + .5f, pos.getY() + .3f, pos.getZ() + .5f, stack);
        Vector3d motion = entity.getDeltaMovement();
        entity.push(-motion.x, -motion.y, -motion.z);
        world.addFreshEntity(entity);
    }



    @Override
    public void onRemove (BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
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
        items.add(getMainDrop(state, (TileEntityDrawersMore)builder.getOptionalParameter(LootParameters.BLOCK_ENTITY)));
        return items;
    }

    protected ItemStack getMainDrop (BlockState state, TileEntityDrawersMore tile) {
        ItemStack drop = new ItemStack(this);
        if (tile == null)
            return drop;

        CompoundNBT data = drop.getTag();
        if (data == null)
            data = new CompoundNBT();

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
            CompoundNBT tiledata = new CompoundNBT();
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
    public int getSignal  (BlockState state, IBlockReader blockAccess, BlockPos pos, Direction side) {
        if (!isSignalSource (state))
            return 0;

        TileEntityDrawersMore tile = (TileEntityDrawersMore)getTileEntity(blockAccess, pos);
        if (tile == null || !tile.isRedstone())
            return 0;

        return tile.getRedstoneLevel();
    }


}
