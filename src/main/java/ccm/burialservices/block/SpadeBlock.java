package ccm.burialservices.block;

import ccm.burialservices.te.SpadeTE;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.ArrayList;

public class SpadeBlock extends BlockContainer
{
    private static SpadeBlock instance;

    public static SpadeBlock getInstance()
    {
        return instance;
    }

    public SpadeBlock(int par1)
    {
        super(par1, Material.circuits);
        setCreativeTab(CreativeTabs.tabBlock);
        setHardness(0);
        setResistance(0);
        setUnlocalizedName("SpadeBlock");

        instance = this;

        MinecraftForge.EVENT_BUS.register(this);
    }

    @ForgeSubscribe
    public void clickEvent(PlayerInteractEvent event)
    {
        World world = event.entityPlayer.getEntityWorld();
        ItemStack itemStack = event.entityPlayer.getHeldItem();
        if (!world.isRemote && itemStack != null && itemStack.getItem() instanceof ItemSpade && event.entityPlayer.isSneaking() && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && event.face == 1)
        {
            Material material = world.getBlockMaterial(event.x, event.y, event.z);
            if (world.isAirBlock(event.x, event.y + 1, event.z) && world.isBlockSolidOnSide(event.x, event.y, event.z, ForgeDirection.UP) && checkMaterial(material))
            {
                event.setCanceled(true);
                world.setBlock(event.x, event.y + 1, event.z, SpadeBlock.getInstance().blockID, onBlockPlaced(world, event.x, event.y, event.z, event.face, 0f, 0f, 0f, 0), 3);
                this.onBlockPlacedBy(world, event.x, event.y + 1, event.z, event.entityPlayer, itemStack);
            }
        }
    }

    private boolean checkMaterial(Material material)
    {
        return material == Material.grass || material == Material.ground || material == Material.craftedSnow || material == Material.clay || material == Material.snow || material == Material.sand;
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta)
    {
        meta = ForgeDirection.UNKNOWN.ordinal();
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            if (direction == ForgeDirection.DOWN || direction == ForgeDirection.UP) continue;

            if (world.isBlockSolidOnSide(x + direction.offsetX, y + 1 + direction.offsetY, z + direction.offsetZ, direction.getOpposite(), false)) meta = direction.ordinal();
        }
        if (meta == ForgeDirection.UNKNOWN.ordinal())
        {
            meta = world.rand.nextInt(2);
        }
        System.out.println(meta);

        return meta;
    }

    @Override
    public void breakBlock(World par1World, int x, int y, int z, int blockID, int meta)
    {
        TileEntity te = par1World.getBlockTileEntity(x, y, z);
        if (te != null && te instanceof SpadeTE)
        {
            SpadeTE inv = (SpadeTE) te;
            inv.removeBlock(par1World);
        }
        super.breakBlock(par1World, x, y, z, blockID, meta);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityliving, ItemStack stack)
    {
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof SpadeTE)
        {
            SpadeTE inv = (SpadeTE) te;
            inv.placeBlock(entityliving, stack, world.getBlockMetadata(x, y, z));
            stack.stackSize--;
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new SpadeTE(world);
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
        return ((SpadeTE) world.getBlockTileEntity(x, y, z)).getStack();
    }

    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
    {
        return new ArrayList<>();
    }

    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z)
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

    public static void place(World world, int x, int y, int z)
    {
        world.setBlock(x, y, z, SpadeBlock.getInstance().blockID, SpadeBlock.getInstance().onBlockPlaced(world, x, y, z, 0, 0f, 0f, 0f, 0), 3);
    }
}
