package ccm.burialservices.block;

import ccm.burialservices.te.ToolTE;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;

public class ToolBlock extends BlockContainer
{
    private static ToolBlock instance;

    public static ToolBlock getInstance()
    {
        return instance;
    }

    public ToolBlock(int par1)
    {
        super(par1, Material.circuits);
        setCreativeTab(CreativeTabs.tabBlock);
        setHardness(0);
        setResistance(0);
        setUnlocalizedName("ToolBlock");
        instance = this;
    }

    public boolean checkMaterial(Material material, Item tool)
    {
        if (tool instanceof ItemSpade) return material == Material.grass || material == Material.ground || material == Material.craftedSnow || material == Material.clay || material == Material.snow || material == Material.sand;
        else if (tool instanceof ItemAxe) return material == Material.grass || material == Material.ground || material == Material.craftedSnow || material == Material.clay || material == Material.snow || material == Material.sand || material == Material.wood;
        else if (tool instanceof ItemPickaxe) return true;
        else return false;
    }

    public int getMetaForShovel(World world, int x, int y, int z)
    {
        int meta = -1;
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            if (direction == ForgeDirection.DOWN || direction == ForgeDirection.UP) continue;
            if (world.isBlockSolidOnSide(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ, direction.getOpposite(), false)) meta = direction.ordinal();
        }
        if (meta == -1)
        {
            meta = world.rand.nextInt(2);
        }

        return meta;
    }

    @Override
    public void breakBlock(World par1World, int x, int y, int z, int blockID, int meta)
    {
        TileEntity te = par1World.getBlockTileEntity(x, y, z);
        if (te != null && te instanceof ToolTE)
        {
            ToolTE inv = (ToolTE) te;
            inv.removeBlock(par1World);
        }
        super.breakBlock(par1World, x, y, z, blockID, meta);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack stack)
    {
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof ToolTE)
        {
            ToolTE inv = (ToolTE) te;
            inv.placeBlock(stack);
            stack.stackSize--;
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new ToolTE(world);
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return false;
    }

    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        return ((ToolTE) world.getBlockTileEntity(x, y, z)).getStack();
    }

    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
    {
        return new ArrayList<>();
    }

    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z)
    {

        ToolTE te = (ToolTE) blockAccess.getBlockTileEntity(x, y, z);
        if (te.getStack() == null) return;
        if (te.getStack().getItem() instanceof ItemSpade)
        {
            float height = 1.3f;
            float depth = -0.3f;
            float d1 = 0.25F;
            float d2 = 0.03F;
            switch (ForgeDirection.values()[blockAccess.getBlockMetadata(x, y, z)])
            {
                case NORTH:
                    this.setBlockBounds(0.5f - d1, depth, 0, 0.5f + d1, height, 0.5f + d2);
                    break;
                case SOUTH:
                    this.setBlockBounds(0.5f - d1, depth, 0.5f + d2, 0.5f + d1, height, 1);
                    break;
                case EAST:
                    this.setBlockBounds(0.5f - d2, depth, 0.5f - d1, 1, height, 0.5f + d1);
                    break;
                case WEST:
                    this.setBlockBounds(0, depth, 0.5f - d1, 0.5f + d2, height, 0.5f + d1);
                    break;
                case DOWN:
                    this.setBlockBounds(0.5f - d2, depth, 0.5f - d1, 0.5f + d2, height, 0.5f + d1);
                    break;
                case UP:
                    this.setBlockBounds(0.5f - d1, depth, 0.5f - d2, 0.5f + d1, height, 0.5f + d2);
                    break;
            }
        }
        else if (te.getStack().getItem() instanceof ItemAxe)
        {
            float height = 0.95f;
            float depth = -0.5f;
            float d1 = 0.03F;
            float d2 = 0.15F;
            switch (ForgeDirection.values()[blockAccess.getBlockMetadata(x, y, z)])
            {
                case NORTH:
                    this.setBlockBounds(0.5f - d1, depth, 0, 0.5f + d1, height, 0.5f + d2);
                    break;
                case SOUTH:
                    this.setBlockBounds(0.5f - d1, depth, 0.5f - d2, 0.5f + d1, height, 1);
                    break;
                case EAST:
                    this.setBlockBounds(0.5f - d2, depth, 0.5f - d1, 1, height, 0.5f + d1);
                    break;
                case WEST:
                    this.setBlockBounds(0, depth, 0.5f - d1, 0.5f + d2, height, 0.5f + d1);
                    break;
            }
        }
        else if (te.getStack().getItem() instanceof ItemPickaxe)
        {
            float height = 1.05f;
            float depth = -0.55f;
            float d1 = 0.03F;
            float d2 = 0.45F;
            switch (ForgeDirection.values()[blockAccess.getBlockMetadata(x, y, z)])
            {
                case NORTH:
                    this.setBlockBounds(0.5f - d1, depth, 0, 0.5f + d1, height, 0.5f + d2);
                    break;
                case SOUTH:
                    this.setBlockBounds(0.5f - d1, depth, 0.5f - d2, 0.5f + d1, height, 1);
                    break;
                case EAST:
                    this.setBlockBounds(0.5f - d2, depth, 0.5f - d1, 1, height, 0.5f + d1);
                    break;
                case WEST:
                    this.setBlockBounds(0, depth, 0.5f - d1, 0.5f + d2, height, 0.5f + d1);
                    break;
            }
        }
    }

    public static void place(World world, int x, int y, int z, Item tool)
    {
        world.setBlock(x, y, z, ToolBlock.getInstance().blockID, ToolBlock.getInstance().onBlockPlaced(world, x, y, z, 0, 0f, 0f, 0f, 0), 3);
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof ToolTE)
        {
            ((ToolTE) te).placeBlock(new ItemStack(tool, 1, world.rand.nextInt(tool.getMaxDamage())));
        }
    }
}
