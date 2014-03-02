package ccm.burialservices.worldgen.village;

import ccm.burialservices.util.BSConstants;
import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.world.gen.structure.ComponentVillageStartPiece;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureVillagePieceWeight;

import java.util.List;
import java.util.Random;

public class GraveyardHandler implements VillagerRegistry.IVillageCreationHandler
{
    public GraveyardHandler()
    {
        MapGenStructureIO.func_143031_a(getComponentClass(), BSConstants.MODID + ":" + getComponentClass().getSimpleName());
    }

    @Override
    public StructureVillagePieceWeight getVillagePieceWeight(Random random, int i)
    {
        return new StructureVillagePieceWeight(getComponentClass(), 10000, 1);
    }

    @Override
    public Class<?> getComponentClass()
    {
        return GraveyardComponent.class;
    }

    @Override
    public Object buildComponent(StructureVillagePieceWeight villagePiece, ComponentVillageStartPiece startPiece, List pieces, Random random, int p1, int p2, int p3, int p4, int p5)
    {
        return GraveyardComponent.buildComponent(startPiece, pieces, random, p1, p2, p3, p4, p5);
    }
}
