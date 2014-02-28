package ccm.burialservices.block;

import ccm.burialservices.BurialServices;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Random;

public class IronShovel extends Block
{
    private static IronShovel instance;

    public static IronShovel getInstance()
    {
        return instance;
    }

    public IronShovel(int par1)
    {
        super(par1, Material.circuits);
        setCreativeTab(CreativeTabs.tabBlock);
        setHardness(0);
        setResistance(0);
        setUnlocalizedName("burialservices.ironshovel");

        instance = this;

        MinecraftForge.EVENT_BUS.register(this);
    }

    @ForgeSubscribe
    public void clickEvent(PlayerInteractEvent event)
    {
        World world = event.entityPlayer.getEntityWorld();
        ItemStack itemStack = event.entityPlayer.getHeldItem();
        if (!world.isRemote && itemStack.itemID == blockID && event.entityPlayer.isSneaking() && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && event.face == 1 && world.isAirBlock(event.x, event.y + 1, event.z) && world.isBlockSolidOnSide(event.x, event.y, event.z, ForgeDirection.UP))
        {
            event.setCanceled(true);
            world.setBlock(event.x, event.y + 1, event.z, IronShovel.getInstance().blockID);
            BurialServices.getLogger().info("PlayerInteractEvent " + event.x + ";" + event.y + ";" + event.z);
        }
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> list = new ArrayList<ItemStack>();
        list.add(new ItemStack(Item.shovelIron, 1, world.rand.nextInt(EnumToolMaterial.IRON.getMaxUses())));
        return list;
    }

    @Override
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return Item.shovelIron.itemID;
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta)
    {
        //dropBlockAsItem_do(world, x, y, z, new ItemStack(Item.shovelIron, 1, world.rand.nextInt(EnumToolMaterial.IRON.getMaxUses())));
    }
}
