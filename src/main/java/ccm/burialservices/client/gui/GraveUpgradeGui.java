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
import ccm.nucleumOmnium.helpers.MiscHelper;
import ccm.nucleumOmnium.helpers.NetworkHelper;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public class GraveUpgradeGui extends GuiScreen
{
    public static final String undertakerTitle = "I'm an Undertaker, upgrade your graves here.";
    public static final String graveTitle      = "Upgrade your graves. (Cheaper at if you find an undertaker...)";

    public static final int ID_DONE             = 0;
    public static final int ID_OPTIONS_EDITTEXT = 1;

    public static final int ID_EDITTEXT_APPLY = 100;

    public final boolean        isUndertaker;
    private      boolean        gotData;
    private      NBTTagCompound data;
    private GuiTextField[] textFields = new GuiTextField[4];
    private int mode;
    private ArrayList<GuiButton> optionButtons = new ArrayList<GuiButton>();
    private GuiButton editTextApplyButton;

    public GraveUpgradeGui(boolean isUndertaker)
    {
        this.isUndertaker = isUndertaker;
    }

    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
        NetClientHandler netclienthandler = this.mc.getNetHandler();

        if (MinecraftServer.getServer().isSinglePlayer())
        {
            MiscHelper.setPersistentDataTag(MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(MinecraftServer.getServer().getServerOwner()), BSConstants.NBT_PLAYER_GRAVE_DATA, data);
        }
        else if (netclienthandler != null)
        {
            PacketDispatcher.sendPacketToServer(NetworkHelper.makeNBTPacket(BSConstants.CHANNEL_GRAVE_UPGRADE, data));
        }
    }

    public void updateInfoFromPacket(NBTTagCompound data)
    {
        gotData = true;
        this.data = data;
        String[] strings = data.getString("text").split("\n");
        if (strings.length != 4) strings = new String[] {"", "", "", ""};
        for (int i = 0; i < 4; i++) textFields[i].setText(strings[i]);
    }

    public void initGui()
    {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        optionButtons.add(new GuiButton(ID_DONE, this.width / 2 - 100, this.height / 2 + 100, "Done"));
        optionButtons.add(new GuiButton(ID_OPTIONS_EDITTEXT, this.width / 2 - 150, 75, 100, 20, "Edit grave text"));

        editTextApplyButton = new GuiButton(ID_EDITTEXT_APPLY, this.width / 2 - 50, this.height / 2 + 55, 100, 20, "Apply");

        for (int i = 0; i < 4; i++)
        {
            textFields[i] = new GuiTextField(this.fontRenderer, this.width / 2 - 50, this.height / 2 - 25 + 25 * (2 - i), 100, 22);
            textFields[i].setMaxStringLength(15);
            textFields[i].setFocused(false);
            textFields[i].setText("");
        }

        setMode(ID_DONE, true);

        super.initGui();

        if (MinecraftServer.getServer().isSinglePlayer())
        {
            updateInfoFromPacket(MiscHelper.getPersistentDataTag(MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(MinecraftServer.getServer().getServerOwner()), BSConstants.NBT_PLAYER_GRAVE_DATA));
        }
    }

    protected void keyTyped(char par1, int par2)
    {
        for (int i = 0; i < 4; i++) textFields[i].textboxKeyTyped(par1, par2);

        super.keyTyped(par1, par2);
    }

    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
            switch (par1GuiButton.id)
            {
                case ID_DONE:
                    this.mc.displayGuiScreen(null);
                    break;
                case ID_OPTIONS_EDITTEXT:
                    setMode(ID_OPTIONS_EDITTEXT, true);
                    break;
                case ID_EDITTEXT_APPLY:
                    String[] strings = new String[4];
                    for (int i = 0; i < 4; i++) strings[i] = textFields[i].getText();
                    data.setString("text", BSConstants.TEXT_JOINER.join(strings));
                    setMode(ID_DONE, true);
                    break;
            }
        }
    }

    private void setMode(int id, boolean enable)
    {
        if (enable) setMode(mode, false);
        mode = id;
        switch (id)
        {
            case ID_DONE: //Main option menu
                if (enable)
                {
                    buttonList.addAll(optionButtons);
                }
                else
                {
                    buttonList.removeAll(optionButtons);
                }
                break;
            case ID_OPTIONS_EDITTEXT:
                if (enable)
                {
                    buttonList.add(editTextApplyButton);
                }
                else
                {
                    buttonList.remove(editTextApplyButton);
                }
        }
    }

    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        for (int i = 0; i < 4; i++) textFields[i].mouseClicked(par1, par2, par3);
    }

    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();

        if (!gotData)
        {
            this.drawCenteredString(this.fontRenderer, "Wait a second for the Grimm Reaper to send over your data...", this.width / 2, this.height / 2, 16777215);
            super.drawScreen(par1, par2, par3);
            return;
        }

        switch (mode)
        {
            case ID_DONE:
                this.drawCenteredString(this.fontRenderer, isUndertaker ? undertakerTitle : graveTitle, this.width / 2, 50, 16777215);
                break;
            case ID_OPTIONS_EDITTEXT:
                this.drawCenteredString(this.fontRenderer, "Edit the text on your grave:", this.width / 2, this.height / 2 - 75, 16777215);
                for (int i = 0; i < 4; i++) textFields[i].drawTextBox();
                break;
        }

        super.drawScreen(par1, par2, par3);
    }
}
