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
import com.google.common.base.Strings;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import static ccm.burialservices.client.renderers.ToolRenderer.MODEL_SIGN;
import static ccm.burialservices.client.renderers.ToolRenderer.SIGN_TEXTURE;

@SideOnly(Side.CLIENT)
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

        float f5 = 0.9F;
        float f6 = 0.0625F;

        switch (te.getBlockMetadata())
        {
            case 0:
                break;
            case 1:
                GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslated(-1, 0, 0);
                break;
            case 2:
                GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslated(-1, 0, -1);
                break;
            case 3:
                GL11.glRotatef(-90F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslated(0, 0, -1);
                break;
        }

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

        ItemStack is = te.getHolding();
        if (is != null)
        {
            renderItemHolding(0, te.getBlockMetadata(), is, x, y, z, tickTime);
            if (is.getItem().requiresMultipleRenderPasses())
            {
                for (int i = 1; i < is.getItem().getRenderPasses(is.getItemDamage()); i++)
                {
                    renderItemHolding(i, te.getBlockMetadata(), is, x, y, z, tickTime);
                }
            }
        }

        renderSign(x, y, z, tickTime, te.text, te.getBlockMetadata(), is != null);
    }

    private void renderSign(double x, double y, double z, float tickTime, String[] signText, int meta, boolean renderingItem)
    {
        if (signText.length == 0) return;
        int i = 0;
        for (String line : signText) if (!Strings.isNullOrEmpty(line)) {i++;}
        if (signText.length != i) return;

        GL11.glPushMatrix();
        float f1 = 0.6666667F;
        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.75F * f1, (float) z + 0.5F);
        float f2 = 0.0F;

        switch (meta)
        {
            case 0:
                break;
            case 1:
                f2 = 90f;
                break;
            case 2:
                f2 = 180f;
                break;
            case 3:
                f2 = -90f;

        }
        GL11.glTranslated(0, -0.3, 0);
        if (renderingItem) GL11.glTranslated(0, 0.03, 0);

        GL11.glRotatef(f2, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
        GL11.glTranslated(0, -1.2, 0);

        GL11.glTranslatef(0.0F, -0.3125F, -0.4375F);
        GL11.glTranslatef(0f, 0.8F, 0.5f);
        MODEL_SIGN.signStick.showModel = false;

        this.bindTexture(SIGN_TEXTURE);
        GL11.glPushMatrix();
        GL11.glScalef(f1, -f1, -f1);
        MODEL_SIGN.renderSign();
        GL11.glPopMatrix();
        FontRenderer fontrenderer = this.getFontRenderer();
        f2 = 0.016666668F * f1;
        GL11.glTranslatef(0.0F, 0.5F * f1, 0.07F * f1);
        GL11.glScalef(f2, -f2, f2);
        GL11.glNormal3f(0.0F, 0.0F, -1.0F * f2);
        GL11.glDepthMask(false);
        byte b0 = 0;

        for (int j = 0; j < signText.length; ++j)
        {
            GL11.glPushMatrix();
            String s = signText[signText.length - j - 1];
            int width = fontrenderer.getStringWidth(s);
            if (width > 95)
            {
                float f = 1f - ((width) * 0.0015f);
                GL11.glScalef(f, f, f);
            }
            fontrenderer.drawString(s, -width / 2, j * 10 - signText.length * 5, b0);
            GL11.glPopMatrix();
        }

        GL11.glDepthMask(true);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    private static void renderItemHolding(int i, int meta, ItemStack stack, double x, double y, double z, float tickTime)
    {
        Icon icon = stack.getItem().getIcon(stack, i);

        if (icon == null) return;

        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z); //Center to block

        switch (meta)
        {
            case 0:
                break;
            case 1:
                GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslated(-1, 0, 0);
                break;
            case 2:
                GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslated(-1, 0, -1);
                break;
            case 3:
                GL11.glRotatef(-90F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslated(0, 0, -1);
                break;
        }

        if (stack.getItem() instanceof ItemSword)
        {
            GL11.glTranslated(0.44, 0.1d, 1.85);
            GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
        }
        else
        {
            GL11.glTranslated(0.5, 0.1d, 0.5);
        }
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
