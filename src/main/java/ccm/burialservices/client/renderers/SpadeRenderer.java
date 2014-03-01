package ccm.burialservices.client.renderers;

import ccm.burialservices.te.SpadeTE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class SpadeRenderer extends TileEntitySpecialRenderer
{
    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float TickTime)
    {
        SpadeTE te = (SpadeTE) tileentity;
        Icon icon = te.getStack().getIconIndex();
        int meta = te.getBlockMetadata();

        if (icon == null) return;

        GL11.glPushMatrix();
        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        texturemanager.bindTexture(texturemanager.getResourceLocation(te.getStack().getItemSpriteNumber()));
        Tessellator tessellator = Tessellator.instance;

        GL11.glTranslated(x, y, z); //Center to block

        float f = icon.getMinU();
        float f1 = icon.getMaxU();
        float f2 = icon.getMinV();
        float f3 = icon.getMaxV();

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        GL11.glTranslatef(-0.5f, .5f, 0.5f); // Center on block

        float shift = 0.3f;
        switch (ForgeDirection.values()[meta])
        {
            case NORTH:
                GL11.glTranslatef(0, 0, -shift);
                GL11.glRotatef(15f, -0.5f, 0, 0);
                break;
            case SOUTH:
                GL11.glTranslatef(0, 0, shift);
                GL11.glRotatef(-15f, -0.5f, 0, 0);
                break;
            case EAST:
                GL11.glRotatef(90f, 0, 1, 0);
                GL11.glTranslatef(-1, 0, 1);
                GL11.glTranslatef(0, 0, shift);
                GL11.glRotatef(-15f, -0.5f, 0, 0);
                break;
            case WEST:
                GL11.glRotatef(90f, 0, 1, 0);
                GL11.glTranslatef(-1, 0, 1);
                GL11.glTranslatef(0, 0, -shift);
                GL11.glRotatef(15f, -0.5f, 0, 0);
                break;
            case DOWN:
                GL11.glRotatef(90f, 0, 1, 0);
                GL11.glTranslatef(-1, 0, 1);
                break;
        }

        GL11.glTranslatef(0, 0, -0.03f); //Icon depth of the shovel

        GL11.glRotatef(180f, 1, 0, 0);
        GL11.glRotatef(-45f, 0, 0, 1);

        GL11.glScalef(1.5f, 1.5f, 1.5f);
        ItemRenderer.renderItemIn2D(tessellator, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 0.06F / 1.5f);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }
}
