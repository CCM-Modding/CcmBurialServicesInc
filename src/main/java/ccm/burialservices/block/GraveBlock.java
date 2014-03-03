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

package ccm.burialservices.block;

import ccm.burialservices.te.GraveTE;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;

public class GraveBlock extends BlockContainer
{
    private static GraveBlock instance;

    public static GraveBlock getInstance()
    {
        return instance;
    }

    public GraveBlock(int par1)
    {
        super(par1, Material.ground);
        setCreativeTab(CreativeTabs.tabBlock);
        setHardness(1.5F);
        setResistance(5F);
        setUnlocalizedName("GraveBlock");

        instance = this;
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new GraveTE(world);
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
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack stack)
    {
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof GraveTE)
        {
            GraveTE inv = (GraveTE) te;
            inv.placeBlock(entityLiving, stack);
        }
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return false;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z)
    {
        switch (blockAccess.getBlockMetadata(x, y, z))
        {
            case 0:
                this.setBlockBounds(0, 0, 0, 1, 0.4f, 2);
                break;
            case 1:
                this.setBlockBounds(0, 0, 0, 2, 0.4f, 1);
                break;
            case 2:
                this.setBlockBounds(0, 0, -1, 1, 0.4f, 1);
                break;
            case 3:
                this.setBlockBounds(-1, 0, 0, 1, 0.4f, 1);
                break;
        }
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9)
    {
        return ((GraveTE) world.getBlockTileEntity(x, y, z)).onActivated(player, side);
    }

    public static boolean place(World world, EntityPlayer entityPlayer, ArrayList<EntityItem> drops)
    {
        int x = (int) entityPlayer.posX;
        int y = (int) entityPlayer.posY;
        int z = (int) entityPlayer.posZ;

        world.setBlock(x, y, z, GraveBlock.getInstance().blockID, world.rand.nextInt(4), 3);

        ((GraveTE) world.getBlockTileEntity(x, y, z)).fillFromDeath(entityPlayer, drops);

        return true;
    }

    @SideOnly(Side.CLIENT)
    public boolean addBlockDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer)
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public boolean addBlockHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer)
    {
        return true;
    }
}
