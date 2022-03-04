package com.rydelfox.morestoragedrawers.client.renderer;

import com.jaquadro.minecraft.storagedrawers.inventory.ItemStackHelper;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class StorageRenderItem extends ItemRenderer {
    private ItemRenderer parent;

    @Nonnull
    public ItemStack overrideStack;

    public StorageRenderItem(TextureManager texManager, ModelManager modelManager, ItemColors colors) {
        super(texManager, modelManager, colors);
        parent = Minecraft.getInstance().getItemRenderer();
        overrideStack = ItemStack.EMPTY;
    }

    @Override
    public ItemModelMesher getItemModelShaper() {
        return parent.getItemModelShaper();
    }

    @Override
    public void render(ItemStack itemStackIn, ItemCameraTransforms.TransformType transformTypeIn, boolean leftHand, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn, IBakedModel modelIn) {
        parent.render(itemStackIn, transformTypeIn, leftHand, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, modelIn);
    }

    public void renderStatic(ItemStack itemStackIn, ItemCameraTransforms.TransformType transformTypeIn, int combinedLightIn, int combinedOverlayIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn) {
        parent.renderStatic(itemStackIn, transformTypeIn, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
    }

    public void renderQuadList(MatrixStack matrixStackIn, IVertexBuilder bufferIn, List<BakedQuad> quadsIn, ItemStack itemStackIn, int combinedLightIn, int combinedOverlayIn) {
        parent.renderQuadList(matrixStackIn, bufferIn, quadsIn, itemStackIn, combinedLightIn, combinedOverlayIn);
    }

    @Override
    public IBakedModel getModel (@Nonnull ItemStack stack, World world, LivingEntity entity) {
        return parent.getModel(stack, world, entity);
    }

    @Override
    public void renderGuiItem (@Nonnull ItemStack stack, int x, int y) {
        parent.renderGuiItem(stack, x, y);
    }

    @Override
    public void renderAndDecorateItem (@Nonnull ItemStack stack, int xPosition, int yPosition) {
        parent.renderAndDecorateItem(stack, xPosition, yPosition);
    }

    @Override
    public void renderAndDecorateItem(@Nullable LivingEntity entityIn, ItemStack itemIn, int x, int y) {
        parent.renderAndDecorateItem(entityIn, itemIn, x, y);
    }

    @Override
    public void renderGuiItemDecorations (FontRenderer fr, @Nonnull ItemStack stack, int xPosition, int yPosition) {
        parent.renderGuiItemDecorations(fr, stack, xPosition, yPosition);
    }

    @Override
    public void renderGuiItemDecorations (FontRenderer font, @Nonnull ItemStack item, int x, int y, String text)
    {
        if (item != overrideStack) {
            super.renderGuiItemDecorations(font, item, x, y, text);
            return;
        }

        if (!item.isEmpty())
        {
            item = ItemStackHelper.decodeItemStack(item);

            float scale = .5f;
            float xoff = 0;
            //if (font.getUnicodeFlag()) {
            //    scale = 1f;
            //    xoff = 1;
            //}

            int stackSize = item.getCount();
            if (ItemStackHelper.isStackEncoded(item))
                stackSize = 0;

            MatrixStack matrixstack = new MatrixStack();
            if (stackSize >= 0 || text != null) {
                if (stackSize >= 100000000)
                    text = (text == null) ? String.format("%.0fM", stackSize / 1000000f) : text;
                else if (stackSize >= 1000000)
                    text = (text == null) ? String.format("%.1fM", stackSize / 1000000f) : text;
                else if (stackSize >= 100000)
                    text = (text == null) ? String.format("%.0fK", stackSize / 1000f) : text;
                else if (stackSize >= 10000)
                    text = (text == null) ? String.format("%.1fK", stackSize / 1000f) : text;
                else
                    text = (text == null) ? String.valueOf(stackSize) : text;

                int textX = (int) ((x + 16 + xoff - font.width(text) * scale) / scale) - 1;
                int textY = (int) ((y + 16 - 7 * scale) / scale) - 1;

                int color = 16777215;
                if (stackSize == 0)
                    color = (255 << 16) | (96 << 8) | (96);

                matrixstack.scale(scale, scale, 1);
                matrixstack.translate(0.0D, 0.0D, (double) (this.blitOffset + 200.0F));
                IRenderTypeBuffer.Impl buffer = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
                font.drawInBatch(text, textX, textY, color, true, matrixstack.last().pose(), buffer, false, 0, 15728880);
                buffer.endBatch();
            }

            if (item.getItem().showDurabilityBar(item)) {
                RenderSystem.disableDepthTest();
                RenderSystem.disableTexture();
                RenderSystem.disableBlend();
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuilder();
                int barWidth = (int)(item.getItem().getDurabilityForDisplay(item));
                double health = item.getItem().getDurabilityForDisplay(item);
                int i = Math.round(13.0F - (float)health * 13.0F);
                int j = item.getItem().getRGBDurabilityForDisplay(item);
                this.draw(bufferbuilder, x + 2, y + 13, 13, 2, 0, 0, 0, 255);
                this.draw(bufferbuilder, x + 2, y + 13, barWidth, 1, j >> 16 & 255, j >> 8 & 255, j & 255, 255);
                RenderSystem.enableBlend();
                RenderSystem.enableTexture();
                RenderSystem.enableDepthTest();
            }

            ClientPlayerEntity clientplayerentity = Minecraft.getInstance().player;
            float f3 = clientplayerentity == null ? 0.0F : clientplayerentity.getCooldowns().getCooldownPercent(item.getItem(), Minecraft.getInstance().getFrameTime());
            if (f3 > 0.0F) {
                RenderSystem.disableDepthTest();
                RenderSystem.disableTexture();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                Tessellator tessellator1 = Tessellator.getInstance();
                BufferBuilder bufferbuilder1 = tessellator1.getBuilder();
                this.draw(bufferbuilder1, x, y + MathHelper.floor(16.0F * (1.0F - f3)), 16, MathHelper.ceil(16.0F * f3), 255, 255, 255, 127);
                RenderSystem.enableTexture();
                RenderSystem.enableDepthTest();
            }
        }
    }

    @Override
    public void onResourceManagerReload (IResourceManager rManager) {
        parent.onResourceManagerReload(rManager);
    }

    public void draw (BufferBuilder tessellator, int x, int y, int w, int h, int r, int g, int b, int a) {
        tessellator.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        tessellator.vertex((float)(x + 0),(float)(y + 0), 0).color(r, g, b, a).endVertex();
        tessellator.vertex((float)(x + 0),(float)(y + h), 0).color(r, g, b, a).endVertex();
        tessellator.vertex((float)(x + w),(float)(y + h), 0).color(r, g, b, a).endVertex();
        tessellator.vertex((float)(x + w),(float)(y + 0), 0).color(r, g, b, a).endVertex();
        Tessellator.getInstance().end();
    }

}
