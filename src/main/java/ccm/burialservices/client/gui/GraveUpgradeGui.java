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

import ccm.burialservices.util.BSConstants;
import ccm.nucleumOmnium.helpers.NetworkHelper;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class GraveUpgradeGui extends GuiScreen
{
    public static final String undertakerTitle = "I'm an Undertaker, upgrade your graves here.";
    public static final String graveTitle      = "Upgrade your graves. (Cheaper at if you find an undertaker...)";
    public final boolean        isUndertaker;
    private      boolean        gotData;
    private      NBTTagCompound data;

    public GraveUpgradeGui(boolean isUndertaker)
    {
        this.isUndertaker = isUndertaker;
    }

    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
        NetClientHandler netclienthandler = this.mc.getNetHandler();

        if (netclienthandler != null)
        {
            PacketDispatcher.sendPacketToServer(NetworkHelper.makeNBTPacket(BSConstants.CHANNEL_GRAVE_UPGRADE, data));
        }
    }

    public void updateInfoFromPacket(NBTTagCompound data)
    {
        gotData = true;
        this.data = data;
    }

    public void initGui()
    {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + 100, "Done"));
        super.initGui();
    }

    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
            if (par1GuiButton.id == 0)
            {
                this.mc.displayGuiScreen(null);
            }
        }
    }

    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, isUndertaker ? undertakerTitle : graveTitle, this.width / 2, 10, 16777215);

        if (!gotData)
        {
            this.drawCenteredString(this.fontRenderer, "Wait a second for the Grimm Reaper to send over your data...", this.width / 2, this.height / 2, 16777215);
        }

        super.drawScreen(par1, par2, par3);
    }
}
