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

import ccm.burialservices.BurialServices;
import ccm.burialservices.block.GraveBlock;
import ccm.nucleumOmnium.helpers.MiscHelper;
import ccm.nucleumOmnium.helpers.NetworkHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;

public class EventHandler
{
    public static final EventHandler INSTANCE    = new EventHandler();
    public              boolean      forceRender = false;

    private EventHandler() {}

    public void init()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * For deaths
     */
    @ForgeSubscribe(priority = EventPriority.HIGHEST)
    public void deathEvent(PlayerDropsEvent event)
    {
        World world = event.entityPlayer.getEntityWorld();
        if (world.isRemote) return;
        event.setCanceled(GraveBlock.place(event.entityPlayer.worldObj, event.entityPlayer, event.drops));
    }

    /**
     * For catching interactions with the Undertaker
     */
    @ForgeSubscribe
    public void interactEvent(EntityInteractEvent event)
    {
        if (event.target instanceof EntityVillager && ((EntityVillager) event.target).getProfession() == BurialServices.getConfig().villagerID)
        {
            if (FMLCommonHandler.instance().getSide().isServer())
            {
                PacketDispatcher.sendPacketToPlayer(NetworkHelper.makeNBTPacket(BSConstants.CHANNEL_GRAVE_UPGRADE, MiscHelper.getPersistentDataTag(event.entityPlayer, BSConstants.NBT_PLAYER_GRAVE_DATA)), (Player) event.entityPlayer);
            }
            event.setCanceled(MinecraftServer.getServer().isSinglePlayer());
            FMLNetworkHandler.openGui(event.entityPlayer, BurialServices.instance, GuiHandler.undertakerID, event.entityPlayer.worldObj, 0, 0, 0);
        }
    }
}
