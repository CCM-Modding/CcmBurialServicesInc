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

import ccm.burialservices.BurialServices;
import ccm.burialservices.block.ToolBlock;
import ccm.burialservices.te.ToolTE;
import ccm.nucleumOmnium.helpers.MiscHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.ComponentVillage;
import net.minecraft.world.gen.structure.ComponentVillageStartPiece;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraveyardComponent extends ComponentVillage
{
    private int averageGroundLevel = -1;

    public GraveyardComponent()
    {

    }

    public GraveyardComponent(ComponentVillageStartPiece villagePiece, int componentType, Random random, StructureBoundingBox structureboundingbox, int coordBaseMode)
    {
        super(villagePiece, componentType);
        this.coordBaseMode = coordBaseMode;
        this.boundingBox = structureboundingbox;
    }

    @Override
    public boolean addComponentParts(World world, Random random, StructureBoundingBox sbb)
    {
        if (this.averageGroundLevel < 0)
        {
            this.averageGroundLevel = this.getAverageGroundLevel(world, sbb);

            if (this.averageGroundLevel < 0)
            {
                return true;
            }

            this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 2, 0);
        }

        // Fences
        this.fillWithBlocks(world, sbb, 0, 1, 0, 0, 1, 10, Block.cobblestoneWall.blockID, Block.cobblestoneWall.blockID, false);
        this.fillWithBlocks(world, sbb, 0, 1, 0, 10, 1, 0, Block.cobblestoneWall.blockID, Block.cobblestoneWall.blockID, false);
        this.fillWithBlocks(world, sbb, 10, 1, 0, 10, 1, 10, Block.cobblestoneWall.blockID, Block.cobblestoneWall.blockID, false);
        this.fillWithBlocks(world, sbb, 0, 1, 10, 10, 1, 10, Block.cobblestoneWall.blockID, Block.cobblestoneWall.blockID, false);

        // Opening in fences
        this.placeBlockAtCurrentPosition(world, 0, 0, 5, 1, 0, sbb);

        // Main path
        this.fillWithBlocks(world, sbb, 5, 0, 0, 5, 0, 9, Block.gravel.blockID, Block.gravel.blockID, true);

        // Cobble floor
        this.fillWithBlocks(world, sbb, 7, 0, 0, 10, 0, 3, Block.cobblestone.blockID, Block.cobblestone.blockID, true);
        for (int x = 7; x < 10; x++)
            for (int z = 0; z < 4; z++)
                this.fillCurrentPositionBlocksDownwards(world, Block.cobblestone.blockID, Block.cobblestone.blockID, x, 0, z, sbb);

        // Walls
        this.fillWithBlocks(world, sbb, 8, 1, 0, 9, 3, 0, Block.planks.blockID, Block.planks.blockID, false);
        this.fillWithBlocks(world, sbb, 7, 1, 1, 7, 3, 2, Block.planks.blockID, Block.planks.blockID, false);
        this.fillWithBlocks(world, sbb, 8, 1, 3, 9, 3, 3, Block.planks.blockID, Block.planks.blockID, false);
        this.fillWithBlocks(world, sbb, 10, 1, 1, 10, 3, 2, Block.planks.blockID, Block.planks.blockID, false);

        // Wood logs
        for (int x : new int[] {7, 10})
            for (int z : new int[] {0, 3})
                this.fillWithBlocks(world, sbb, x, 1, z, x, 3, z, Block.wood.blockID, Block.wood.blockID, false);
        this.fillWithBlocks(world, sbb, 7, 4, 0, 10, 4, 3, Block.wood.blockID, Block.wood.blockID, false);

        // Glass panes
        this.fillWithBlocks(world, sbb, 8, 2, 2, 9, 2, 3, Block.thinGlass.blockID, Block.thinGlass.blockID, true);

        // Torch
        this.placeBlockAtCurrentPosition(world, Block.torchWood.blockID, Block.torchWood.blockID, 8, 3, 1, sbb);

        // Hoe
        {
            int i = this.getXWithOffset(9, 2);
            int j = this.getYWithOffset(1);
            int k = this.getZWithOffset(9, 2);
            ToolBlock.placeLeaning(world, i, j, k, MiscHelper.getRandomFromSet(random, Item.hoeWood, Item.hoeStone, Item.hoeIron, Item.hoeGold, Item.hoeDiamond));
        }
        this.placeBlockAtCurrentPosition(world, Block.torchWood.blockID, Block.torchWood.blockID, 8, 3, 1, sbb);

        // Door
        this.fillWithAir(world, sbb, 7, 1, 1, 7, 2, 1);
        this.placeDoorAtCurrentPosition(world, sbb, random, 7, 1, 1, this.getMetadataWithOffset(Block.doorWood.blockID, 0));

        // Spade
        {
            int i = this.getXWithOffset(6, 2);
            int j = this.getYWithOffset(1);
            int k = this.getZWithOffset(6, 2);
            ToolBlock.placeLeaning(world, i, j, k, MiscHelper.getRandomFromSet(random, Item.shovelWood, Item.shovelStone, Item.shovelIron, Item.shovelGold, Item.shovelDiamond));
        }

        // Graves
        {
            //noinspection unchecked
            ArrayList<Integer>[] lines = new ArrayList[] {new ArrayList<Integer>(), new ArrayList<Integer>(), new ArrayList<Integer>(), new ArrayList<Integer>()};

            int x = 1;
            int j = this.getYWithOffset(1);
            for (int z = 1; z < 10; z += 2)
            {
                int i = this.getXWithOffset(x, z);
                int k = this.getZWithOffset(x, z);
                ToolBlock.placeOther(world, i, j, k, MiscHelper.getRandomFromSet(random, Item.swordWood, Item.swordStone, Item.swordGold), getMetaBaseOnRotation());
                ((ToolTE) world.getBlockTileEntity(i, j, k)).addSign(getFacingBaseOnRotation(false), lines);
            }
            for (int z = 2; z < 9; z += 2)
                this.placeBlockAtCurrentPosition(world, Block.flowerPot.blockID, MathHelper.getRandomIntegerInRange(random, 1, 12), x, 1, z, sbb);

            int[] crops = {Block.crops.blockID, Block.carrot.blockID, Block.potato.blockID};
            x = 9;
            for (int z = 0; z < 3; z++)
            {
                int i = this.getXWithOffset(x, 5 + z * 2);
                int k = this.getZWithOffset(x, 5 + z * 2);
                ToolBlock.placeOther(world, i, j, k, Item.swordWood, getMetaBaseOnRotation());
                ((ToolTE) world.getBlockTileEntity(i, j, k)).addSign(getFacingBaseOnRotation(true), lines);

                this.placeBlockAtCurrentPosition(world, Block.tilledField.blockID, 1, x - 2, 0, 5 + z * 2, sbb);
                this.placeBlockAtCurrentPosition(world, Block.tilledField.blockID, 1, x - 1, 0, 5 + z * 2, sbb);
                this.placeBlockAtCurrentPosition(world, crops[z], MathHelper.getRandomIntegerInRange(random, 2, 7), x - 2, 1, 5 + z * 2, sbb);
                this.placeBlockAtCurrentPosition(world, crops[z], MathHelper.getRandomIntegerInRange(random, 2, 7), x - 1, 1, 5 + z * 2, sbb);
            }

            this.placeBlockAtCurrentPosition(world, Block.flowerPot.blockID, 1 + random.nextInt(11), x, 1, 6, sbb);
            this.placeBlockAtCurrentPosition(world, Block.flowerPot.blockID, 1 + random.nextInt(11), x, 1, 8, sbb);
        }

        this.spawnVillagers(world, sbb, 5, 1, 5, 1);
        return true;
    }

    protected int getVillagerType(int par1)
    {
        return BurialServices.getConfig().villagerID;
    }

    public int getFacingBaseOnRotation(boolean b)
    {
        switch (coordBaseMode)
        {
            case 0:
                return b ? 0 : 1;
            case 1:
                return b ? 2 : 4;
            case 2:
                return b ? 1 : 0;
            case 3:
                return b ? 4 : 2;
        }
        return -1;
    }

    public int getMetaBaseOnRotation()
    {
        return coordBaseMode == 0 || coordBaseMode == 2 ? 1 : 0;
    }

    public static GraveyardComponent buildComponent(ComponentVillageStartPiece villagePiece, List pieces, Random random, int p1, int p2, int p3, int p4, int p5)
    {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p1, p2, p3, 0, 0, 0, 10, 4, 10, p4);
        return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new GraveyardComponent(villagePiece, p5, random, structureboundingbox, p4) : null;
    }
}
