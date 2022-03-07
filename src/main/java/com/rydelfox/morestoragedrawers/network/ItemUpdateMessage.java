package com.rydelfox.morestoragedrawers.network;

import com.rydelfox.morestoragedrawers.block.tile.TileEntityDrawersMore;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ItemUpdateMessage {

    private BlockPos pos;
    private int slot;
    private ItemStack item;

    private boolean failed;

    public ItemUpdateMessage (BlockPos pos, int slot, ItemStack item) {
        this.pos = pos;
        this.slot = slot;
        this.item = item;
        this.failed = false;
    }

    private ItemUpdateMessage (boolean failed) {
        this.failed = failed;
    }

    public static ItemUpdateMessage decode (PacketBuffer buf) {
        return new ItemUpdateMessage(buf.readBlockPos(), buf.readByte(), buf.readItem());
    }

    public static void encode (ItemUpdateMessage object, PacketBuffer buf) {
        buf.writeBlockPos(object.pos);
        buf.writeByte(object.slot);
        buf.writeItemStack(object.item, false);
    }

    public static void handle(ItemUpdateMessage msg, Supplier<NetworkEvent.Context> ctx) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> handleClient(msg, ctx.get()));
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClient(ItemUpdateMessage msg, NetworkEvent.Context ctx) {
        if (!msg.failed) {
            World world = Minecraft.getInstance().level;
            if (world != null) {
                BlockPos pos = msg.pos;
                TileEntity tileEntity = world.getBlockEntity(pos);
                if (tileEntity instanceof TileEntityDrawersMore) {
                    ((TileEntityDrawersMore) tileEntity).clientUpdateItem(msg.slot, msg.item);
                }
            }
        }
    }
}
