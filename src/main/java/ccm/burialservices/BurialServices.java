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

import ccm.burialservices.block.GraveBlock;
import ccm.burialservices.block.ToolBlock;
import ccm.burialservices.client.renderers.GraveRenderer;
import ccm.burialservices.client.renderers.ToolRenderer;
import ccm.burialservices.te.GraveTE;
import ccm.burialservices.te.ToolTE;
import ccm.burialservices.util.EventHandler;
import ccm.burialservices.worldgen.village.GraveyardHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;

import java.util.logging.Logger;

import static ccm.burialservices.util.BSConstants.MODID;
import static ccm.burialservices.util.BSConstants.MODNAME;

@Mod(modid = MODID, name = MODNAME, dependencies = "required-after:NucleumOmnium")
public class BurialServices
{
    @Mod.Instance(MODID)
    public static BurialServices instance;

    @Mod.Metadata(MODID)
    private static ModMetadata metadata;

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

    private static String getVersion()
    {
        return metadata.version;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        config = new BSConfig(event.getSuggestedConfigurationFile());
        new ToolBlock(config.toolBlockID);
        GameRegistry.registerTileEntity(ToolTE.class, "ToolTE");
        GameRegistry.registerBlock(ToolBlock.getInstance(), "ToolBlock");
        LanguageRegistry.addName(ToolBlock.getInstance(), "ToolBlock");

        new GraveBlock(config.graveBlockID);
        GameRegistry.registerTileEntity(GraveTE.class, "GraveTE");
        GameRegistry.registerBlock(GraveBlock.getInstance(), "GraveBlock");
        LanguageRegistry.addName(GraveBlock.getInstance(), "Grave");

        if (event.getSide().isClient())
        {
            ClientRegistry.bindTileEntitySpecialRenderer(ToolTE.class, new ToolRenderer());
            ClientRegistry.bindTileEntitySpecialRenderer(GraveTE.class, new GraveRenderer());
        }

        VillagerRegistry.instance().registerVillageCreationHandler(new GraveyardHandler());

        EventHandler.INSTANCE.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {

    }
}
