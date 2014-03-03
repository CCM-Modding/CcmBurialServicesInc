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

import ccm.burialservices.te.ToolTE;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
        setHardness(1.5F);
        setResistance(5F);
        setUnlocalizedName("ToolBlock");
        instance = this;
    }

    public boolean checkMaterial(Material material, Item tool)
    {
        if (tool instanceof ItemSpade || tool instanceof ItemHoe) return material == Material.grass || material == Material.ground || material == Material.craftedSnow || material == Material.clay || material == Material.snow || material == Material.sand || material == Material.cloth;
        else if (tool instanceof ItemAxe || tool instanceof ItemSword)
            return material == Material.grass || material == Material.ground || material == Material.craftedSnow || material == Material.clay || material == Material.snow || material == Material.sand || material == Material.wood || material == Material.cloth;
        else if (tool instanceof ItemPickaxe) return true;
        else return false;
    }

    public int getMetaForLean(World world, int x, int y, int z)
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
            if (entityLiving instanceof EntityPlayer && !((EntityPlayer) entityLiving).capabilities.isCreativeMode) stack.stackSize--;
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
        if (te.getStack().getItem() instanceof ItemSpade || te.getStack().getItem() instanceof ItemHoe)
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
        else if (te.getStack().getItem() instanceof ItemSword)
        {
            float d1 = 0.03F;
            float d2 = 0.9f;
            switch (ForgeDirection.values()[blockAccess.getBlockMetadata(x, y, z)])
            {
                case NORTH:
                    this.setBlockBounds(0.5f - d1, 0, 0, 0.5f + d1, 1, 0.5f + d2);
                    break;
                case SOUTH:
                    this.setBlockBounds(0.5f - d1, 0, 0.5f - d2, 0.5f + d1, 1, 1);
                    break;
                case EAST:
                    this.setBlockBounds(0.5f - d2, 0, 0.5f - d1, 1, 1, 0.5f + d1);
                    break;
                case WEST:
                    this.setBlockBounds(0, 0, 0.5f - d1, 0.5f + d2, 1, 0.5f + d1);
                    break;
                case UP:
                    this.setBlockBounds(0.5f - d1, 0, 0, 0.5f + d1, 1.55f, 1);
                    break;
                case DOWN:
                    this.setBlockBounds(0, 0, 0.5f - d1, 1, 1.55f, 0.5f + d1);
                    break;
            }
        }
    }

    public static void placeLeaning(World world, int x, int y, int z, Item tool)
    {
        placeOther(world, x, y, z, tool, getInstance().getMetaForLean(world, x, y, z));
    }

    public static void placeOther(World world, int x, int y, int z, Item tool, int meta)
    {
        world.setBlock(x, y, z, ToolBlock.getInstance().blockID, meta, 3);
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof ToolTE)
        {
            ((ToolTE) te).placeBlock(new ItemStack(tool, 1, world.rand.nextInt(tool.getMaxDamage())));
        }
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
