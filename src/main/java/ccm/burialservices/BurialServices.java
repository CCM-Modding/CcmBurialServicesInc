package ccm.burialservices;

import ccm.burialservices.block.ToolBlock;
import ccm.burialservices.client.renderers.ToolRenderer;
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
        new ToolBlock(config.ironShovelID);
        GameRegistry.registerTileEntity(ToolTE.class, "ToolTE");
        GameRegistry.registerBlock(ToolBlock.getInstance(), "ToolBlock");
        LanguageRegistry.addName(ToolBlock.getInstance(), "ToolBlock");
        if (event.getSide().isClient())
        {
            ClientRegistry.bindTileEntitySpecialRenderer(ToolTE.class, new ToolRenderer());
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
