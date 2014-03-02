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
