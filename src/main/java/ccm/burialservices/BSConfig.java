package ccm.burialservices;

import net.minecraftforge.common.Configuration;

import java.io.File;

public class BSConfig
{
    public int ironShovelID = 270;

    public BSConfig(File suggestedConfigurationFile)
    {
        final Configuration config = new Configuration(suggestedConfigurationFile);

        ironShovelID = config.getBlock("ironShovelID", ironShovelID).getInt();

        config.save();
    }
}
