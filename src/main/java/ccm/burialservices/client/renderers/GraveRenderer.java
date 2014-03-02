/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014. Dries K. Aka Dries007 and the CCM modding crew.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ccm.burialservices.client.renderers;

import ccm.burialservices.te.GraveTE;
import ccm.burialservices.util.EventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.Event;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GraveRenderer extends TileEntitySpecialRenderer
{
    private final ModelBiped modelBiped = new ModelBiped();
    ItemStack demo = new ItemStack(Item.pickaxeDiamond);

    public GraveRenderer()
    {
        modelBiped.isChild = false;
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float tickTime)
    {
        GraveTE te = (GraveTE) tileentity;

        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z); //Center to block

        ResourceLocation skin = te.getLocationSkin();
        if (skin == null) skin = AbstractClientPlayer.locationStevePng;

        float f5 = 0.9F;
        float f6 = 0.0625F;

        GL11.glTranslated(0.5, 0.1d, 0.5);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glScalef(f6, f6, f6);

        Minecraft.getMinecraft().renderEngine.bindTexture(skin);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);

        modelBiped.bipedHead.rotateAngleX = (float) Math.toRadians(15F);
        modelBiped.bipedHead.render(f5);
        modelBiped.bipedBody.render(f5);
        modelBiped.bipedRightArm.render(f5);
        modelBiped.bipedLeftArm.render(f5);
        modelBiped.bipedRightLeg.render(f5);
        modelBiped.bipedLeftLeg.render(f5);

        GL11.glPopMatrix();

        if (te.getContents().length > 0)
        {
            ItemStack is = te.getContents()[0];

            if (is != null)
            {
                renderItemHolding(0, is, x, y, z, tickTime);
                if (is.getItem().requiresMultipleRenderPasses())
                {
                    for (int i = 1; i < is.getItem().getRenderPasses(is.getItemDamage()); i++)
                    {
                        renderItemHolding(i, is, x, y, z, tickTime);
                    }
                }
            }
        }

    }

    private static void renderItemHolding(int i, ItemStack stack, double x, double y, double z, float tickTime)
    {
        Icon icon = stack.getItem().getIcon(stack, i);

        if (icon == null) return;

        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z); //Center to block
        GL11.glTranslated(0.5, 0.1d, 0.5);
        GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);

        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        texturemanager.bindTexture(texturemanager.getResourceLocation(stack.getItemSpriteNumber()));
        Tessellator tessellator = Tessellator.instance;

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        GL11.glTranslatef(0.65f, 0.6f, -0.1f); // Center on block
        GL11.glRotatef(135f, 0, 0, 1);

        ItemRenderer.renderItemIn2D(tessellator, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 0.06F);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }
}
