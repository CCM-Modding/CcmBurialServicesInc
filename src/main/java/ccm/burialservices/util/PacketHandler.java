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

package ccm.burialservices.util;

import ccm.burialservices.client.gui.GraveUpgradeGui;
import ccm.nucleumOmnium.helpers.MiscHelper;
import ccm.nucleumOmnium.helpers.NetworkHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import static ccm.burialservices.util.BSConstants.CHANNEL_GRAVE_UPGRADE;

public class PacketHandler implements IPacketHandler
{
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        try
        {
            if (packet.channel.equals(CHANNEL_GRAVE_UPGRADE))
            {
                if (FMLCommonHandler.instance().getEffectiveSide().isClient())
                {
                    if (Minecraft.getMinecraft().currentScreen instanceof GraveUpgradeGui)
                    {
                        ((GraveUpgradeGui) (Minecraft.getMinecraft().currentScreen)).updateInfoFromPacket(NetworkHelper.byteArrayToNBT(packet.data));
                    }
                }
                else
                {
                    MiscHelper.setPersistentDataTag((EntityPlayer) player, BSConstants.NBT_PLAYER_GRAVE_DATA, NetworkHelper.byteArrayToNBT(packet.data));
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
