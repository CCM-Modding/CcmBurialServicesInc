package ccm.burialservices;

import ccm.burialservices.block.IronShovel;
import ccm.nucleumOmnium.NOConfig;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.client.resources.Language;
import scala.tools.nsc.typechecker.Infer;
import sun.org.mozilla.javascript.internal.Token;

import java.util.logging.Logger;

import static ccm.burialservices.util.BSConstants.*;
import static ccm.nucleumOmnium.util.NOConstants.NO_MODID;

@Mod(modid = MODID, name = MODNAME, dependencies = "required-after:NucleumOmnium")
public class BurialServices
{
    @Mod.Instance(MODID)
    public static BurialServices instance;

    @Mod.Metadata(MODID)
    private static ModMetadata metadata;

    private BSConfig config;
    private Logger logger;

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
        new IronShovel(config.ironShovelID);
        GameRegistry.registerBlock(IronShovel.getInstance(), "IronShovel");
        LanguageRegistry.addName(IronShovel.getInstance(), "Iron Shovel");
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
