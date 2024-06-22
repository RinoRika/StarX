package net.minecraft.client.renderer.entity;

import java.util.Random;

import cn.stars.starx.util.misc.ModuleInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderEntityItem extends Render<EntityItem>
{
    private final RenderItem itemRenderer;
    private Random field_177079_e = new Random();

    public RenderEntityItem(RenderManager renderManagerIn, RenderItem p_i46167_2_)
    {
        super(renderManagerIn);
        this.itemRenderer = p_i46167_2_;
        this.shadowSize = 0.15F;
        this.shadowOpaque = 0.75F;
    }

    private int func_177077_a(EntityItem itemIn, double p_177077_2_, double p_177077_4_, double p_177077_6_, float p_177077_8_, IBakedModel p_177077_9_)
    {
        ItemStack itemstack = itemIn.getEntityItem();
        Item item = itemstack.getItem();

        if (item == null)
        {
            return 0;
        }
        else
        {
            boolean flag = p_177077_9_.isGui3d();
            int i = this.getModelCount(itemstack);
            float f = 0.25F;
            float f1 = MathHelper.sin(((float)itemIn.getAge() + p_177077_8_) / 10.0F + itemIn.hoverStart) * 0.1F + 0.1F;
            float f2 = p_177077_9_.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;
            GlStateManager.translate((float)p_177077_2_, (float)p_177077_4_ + f1 + 0.25F * f2, (float)p_177077_6_);

            if (flag || this.renderManager.options != null)
            {
                float f3 = (((float)itemIn.getAge() + p_177077_8_) / 20.0F + itemIn.hoverStart) * (180F / (float)Math.PI);
                GlStateManager.rotate(f3, 0.0F, 1.0F, 0.0F);
            }

            if (!flag)
            {
                float f6 = -0.0F * (float)(i - 1) * 0.5F;
                float f4 = -0.0F * (float)(i - 1) * 0.5F;
                float f5 = -0.046875F * (float)(i - 1) * 0.5F;
                GlStateManager.translate(f6, f4, f5);
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            return i;
        }
    }

    private int getModelCount(ItemStack stack)
    {
        int i = 1;

        if (stack.stackSize > 48)
        {
            i = 5;
        }
        else if (stack.stackSize > 32)
        {
            i = 4;
        }
        else if (stack.stackSize > 16)
        {
            i = 3;
        }
        else if (stack.stackSize > 1)
        {
            i = 2;
        }

        return i;
    }

    public static long tick;
    private static double rotation = 0.0D;
    private static Random random = new Random();

    public void doRender(EntityItem entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        if (ModuleInstance.getModule("ItemPhysics").isEnabled()) {
            if (!entity.onGround) {
                rotation *= 1.005f;
                entity.rotationPitch += rotation;
            }

            Minecraft mc = Minecraft.getMinecraft();

            //ItemPhysics pro rendering
            rotation = 2;
            if (!mc.inGameHasFocus) rotation = 0;

            ItemStack itemstack = entity.getEntityItem();
            int i = itemstack != null && itemstack.getItem() != null ? Item.getIdFromItem(itemstack.getItem()) + itemstack.getMetadata() : 187;
            random.setSeed(i);

            Minecraft.getMinecraft().getTextureManager().bindTexture(getEntityTexture(entity));
            Minecraft.getMinecraft().getTextureManager().getTexture(getEntityTexture(entity))
                    .setBlurMipmap(false, false);

            GlStateManager.enableRescaleNormal();
            GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
            GlStateManager.enableBlend();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
            GlStateManager.pushMatrix();
            IBakedModel ibakedmodel = mc.getRenderItem().getItemModelMesher().getItemModel(itemstack);
            boolean flag1 = ibakedmodel.isGui3d();
            boolean is3D = ibakedmodel.isGui3d();
            int j = getModelCount(itemstack);

            GlStateManager.translate((float) x, (float) y, (float) z);

            if (ibakedmodel.isGui3d()) GlStateManager.scale(0.5F, 0.5F, 0.5F);

            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(((EntityItem) entity).rotationYaw, 0.0F, 0.0F, 1.0F);

            GlStateManager.translate(0, 0, is3D ? -0.08 : -0.04);

            //Handle Rotations
            if (is3D || mc.getRenderManager().options != null) {
                if (is3D) {
                    if (!((EntityItem) entity).onGround) {
                        ((EntityItem) entity).rotationPitch += rotation;
                    }
                } else {
                    if (!Double.isNaN(((EntityItem) entity).posX) && !Double.isNaN(((EntityItem) entity).posY) && !Double.isNaN(((EntityItem) entity).posZ) && ((EntityItem) entity).worldObj != null) {
                        if (((EntityItem) entity).onGround) {
                            ((EntityItem) entity).rotationPitch = 0;
                        } else {
                            ((EntityItem) entity).rotationPitch += rotation;
                        }
                    }
                }

                GlStateManager.rotate(((EntityItem) entity).rotationPitch, 1, 0, 0.0F);
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            for (int k = 0; k < j; k++) {
                GlStateManager.pushMatrix();
                if (flag1) {
                    if (k > 0) {
                        float f7 = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        float f9 = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        float f6 = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        GlStateManager.translate(f7, f9, f6);
                    }

                    mc.getRenderItem().renderItem(itemstack, ibakedmodel);
                    GlStateManager.popMatrix();
                } else {
                    mc.getRenderItem().renderItem(itemstack, ibakedmodel);
                    GlStateManager.popMatrix();
                    GlStateManager.translate(0.0F, 0.0F, 0.05375F);
                }
            }

            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            Minecraft.getMinecraft().getTextureManager().bindTexture(getEntityTexture(entity));
            Minecraft.getMinecraft().getTextureManager().getTexture(getEntityTexture(entity)).restoreLastBlurMipmap();
        } else {
            ItemStack itemstack = entity.getEntityItem();
            this.field_177079_e.setSeed(187L);
            boolean flag = false;

            if (this.bindEntityTexture(entity)) {
                this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).setBlurMipmap(false, false);
                flag = true;
            }

            GlStateManager.enableRescaleNormal();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.pushMatrix();
            IBakedModel ibakedmodel = this.itemRenderer.getItemModelMesher().getItemModel(itemstack);
            int i = this.func_177077_a(entity, x, y, z, partialTicks, ibakedmodel);

            for (int j = 0; j < i; ++j) {
                if (ibakedmodel.isGui3d()) {
                    GlStateManager.pushMatrix();

                    if (j > 0) {
                        float f = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        float f1 = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        float f2 = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        GlStateManager.translate(f, f1, f2);
                    }

                    GlStateManager.scale(0.5F, 0.5F, 0.5F);
                    ibakedmodel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GROUND);
                    this.itemRenderer.renderItem(itemstack, ibakedmodel);
                    GlStateManager.popMatrix();
                } else {
                    GlStateManager.pushMatrix();
                    ibakedmodel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GROUND);
                    this.itemRenderer.renderItem(itemstack, ibakedmodel);
                    GlStateManager.popMatrix();
                    float f3 = ibakedmodel.getItemCameraTransforms().ground.scale.x;
                    float f4 = ibakedmodel.getItemCameraTransforms().ground.scale.y;
                    float f5 = ibakedmodel.getItemCameraTransforms().ground.scale.z;
                    GlStateManager.translate(0.0F * f3, 0.0F * f4, 0.046875F * f5);
                }
            }

            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            this.bindEntityTexture(entity);

            if (flag) {
                this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).restoreLastBlurMipmap();
            }

            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    protected ResourceLocation getEntityTexture(EntityItem entity)
    {
        return TextureMap.locationBlocksTexture;
    }
}
