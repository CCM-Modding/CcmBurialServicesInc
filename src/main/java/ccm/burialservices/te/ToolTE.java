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

package ccm.burialservices.te;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ToolTE extends TileEntity
{
    private ItemStack stack;

    public ToolTE()
    {

    }

    public ToolTE(World world)
    {
        setWorldObj(world);
    }

    public void placeBlock(ItemStack stack)
    {
        if (stack != null) this.stack = stack.copy();
    }

    public void removeBlock(World par1World)
    {
        if (!par1World.isRemote && par1World.getGameRules().getGameRuleBooleanValue("doTileDrops") && stack != null)
        {
            float f = 0.7F;
            double d0 = (double) (par1World.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
            double d1 = (double) (par1World.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
            double d2 = (double) (par1World.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(par1World, (double) xCoord + d0, (double) yCoord + d1, (double) zCoord + d2, getStack());
            entityitem.delayBeforeCanPickup = 10;
            par1World.spawnEntityInWorld(entityitem);
        }
    }

    public ItemStack getStack()
    {
        return stack;
    }

    public void readFromNBT(NBTTagCompound tag)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) return;
        super.readFromNBT(tag);

        stack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("stack"));
    }

    public void writeToNBT(NBTTagCompound tag)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) return;
        super.writeToNBT(tag);
        tag.setCompoundTag("stack", getStack().writeToNBT(new NBTTagCompound()));
    }

    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
    {
        stack = ItemStack.loadItemStackFromNBT(pkt.data.getCompoundTag("stack"));
    }

    public Packet getDescriptionPacket()
    {
        NBTTagCompound root = new NBTTagCompound();
        root.setCompoundTag("stack", getStack().writeToNBT(new NBTTagCompound()));
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 15, root);
    }
}