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

package ccm.burialservices;

import ccm.burialservices.util.PacketHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

import java.util.logging.Logger;

import static ccm.burialservices.util.BSConstants.*;

@Mod(modid = MODID, name = MODNAME, dependencies = "required-after:NucleumOmnium;required-after:PlaceableTools")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {CHANNEL_SIGN_UPDATE, CHANNEL_GRAVE_UPGRADE}, packetHandler = PacketHandler.class)
public class BurialServices
{
    @Mod.Instance(MODID)
    public static BurialServices instance;

    @Mod.Metadata(MODID)
    public static ModMetadata metadata;

    @SidedProxy(serverSide = "ccm.burialservices.CommonProxy", clientSide = "ccm.burialservices.client.ClientProxy")
    public static CommonProxy proxy;

    private BSConfig config;
    private Logger   logger;

    public static Logger getLogger()
    {
        return instance.logger;
    }

    public static BSConfig getConfig()
    {
        return instance.config;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        config = new BSConfig(event.getSuggestedConfigurationFile());

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }
}
