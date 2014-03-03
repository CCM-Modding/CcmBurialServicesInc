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

package ccm.burialservices.client.gui;

import ccm.burialservices.te.ToolTE;
import ccm.burialservices.util.BSConstants;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

@SideOnly(Side.CLIENT)
public class SwordSignGui extends GuiScreen
{
    private static final String allowedCharacters = ChatAllowedCharacters.allowedCharacters;
    protected            String screenTitle       = "Edit sign message:";
    private int       updateCounter;
    private int       editLine;
    private GuiButton doneBtn;

    private ToolTE te;
    private TileEntitySign fakeSign = new TileEntitySign();

    public SwordSignGui(ToolTE toolTE)
    {
        this.te = toolTE;
        fakeSign.setWorldObj(toolTE.getWorldObj());
        fakeSign.xCoord = te.xCoord;
        fakeSign.yCoord = te.yCoord;
        fakeSign.zCoord = te.zCoord;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        this.buttonList.add(this.doneBtn = new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, "Done"));
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
        NetClientHandler netclienthandler = this.mc.getNetHandler();

        if (netclienthandler != null)
        {
            ByteArrayOutputStream streambyte = new ByteArrayOutputStream();
            DataOutputStream stream = new DataOutputStream(streambyte);

            try
            {
                stream.writeInt(te.xCoord);
                stream.writeInt(te.yCoord);
                stream.writeInt(te.zCoord);

                stream.writeBoolean(te.lastAdded == 1);

                stream.writeUTF(fakeSign.signText[0]);
                stream.writeUTF(fakeSign.signText[1]);
                stream.writeUTF(fakeSign.signText[2]);
                stream.writeUTF(fakeSign.signText[3]);

                stream.close();
                streambyte.close();

                PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket(BSConstants.CHANNEL_SIGN_UPDATE, streambyte.toByteArray()));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        ++this.updateCounter;
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
            if (par1GuiButton.id == 0)
            {
                this.te.onInventoryChanged();
                this.mc.displayGuiScreen((GuiScreen) null);
            }
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        if (par2 == 200)
        {
            this.editLine = this.editLine - 1 & 3;
        }

        if (par2 == 208 || par2 == 28 || par2 == 156)
        {
            this.editLine = this.editLine + 1 & 3;
        }

        if (par2 == 14 && this.fakeSign.signText[this.editLine].length() > 0)
        {
            this.fakeSign.signText[this.editLine] = this.fakeSign.signText[this.editLine].substring(0, this.fakeSign.signText[this.editLine].length() - 1);
        }

        if (allowedCharacters.indexOf(par1) >= 0 && this.fakeSign.signText[this.editLine].length() < 15)
        {
            this.fakeSign.signText[this.editLine] = this.fakeSign.signText[this.editLine] + par1;
        }

        if (par2 == 1)
        {
            this.actionPerformed(this.doneBtn);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 40, 16777215);
        GL11.glPushMatrix();
        GL11.glTranslatef((float) (this.width / 2), 0.0F, 50.0F);
        float f1 = 93.75F;
        GL11.glScalef(-f1, -f1, -f1);
        GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        Block block = this.te.getBlockType();

        if (block == Block.signPost)
        {
            float f2 = (float) (this.te.getBlockMetadata() * 360) / 16.0F;
            GL11.glRotatef(f2, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, -1.0625F, 0.0F);
        }
        else
        {
            int k = this.te.getBlockMetadata();
            float f3 = 0.0F;

            if (k == 2)
            {
                f3 = 180.0F;
            }

            if (k == 4)
            {
                f3 = 90.0F;
            }

            if (k == 5)
            {
                f3 = -90.0F;
            }

            GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, -1.0625F, 0.0F);
        }

        if (this.updateCounter / 6 % 2 == 0)
        {
            fakeSign.lineBeingEdited = editLine;
        }

        TileEntityRenderer.instance.renderTileEntityAt(this.fakeSign, -0.5D, -0.75D, -0.5D, 0.0F);
        GL11.glPopMatrix();
        super.drawScreen(par1, par2, par3);
    }
}
