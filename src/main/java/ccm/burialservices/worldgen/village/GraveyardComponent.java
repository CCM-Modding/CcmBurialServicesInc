package ccm.burialservices.worldgen.village;

import ccm.burialservices.block.ToolBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.ComponentVillage;
import net.minecraft.world.gen.structure.ComponentVillageStartPiece;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

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

        // Door
        this.fillWithAir(world, sbb, 7, 1, 1, 7, 2, 1);
        this.placeDoorAtCurrentPosition(world, sbb, random, 7, 1, 1, this.getMetadataWithOffset(Block.doorWood.blockID, 0));

        // Spade
        int i = this.getXWithOffset(6, 2);
        int j = this.getYWithOffset(1);
        int k = this.getZWithOffset(6, 2);
        ToolBlock.place(world, i, j, k, Item.shovelIron);

        return true;
    }

    public static GraveyardComponent buildComponent(ComponentVillageStartPiece villagePiece, List pieces, Random random, int p1, int p2, int p3, int p4, int p5)
    {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p1, p2, p3, 0, 0, 0, 10, 4, 10, p4);
        return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new GraveyardComponent(villagePiece, p5, random, structureboundingbox, p4) : null;
    }
}
