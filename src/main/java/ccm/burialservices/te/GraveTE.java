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

import com.google.common.base.Joiner;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GraveTE extends TileEntity
{
    private ItemStack[] contents = new ItemStack[0];
    private String username = "";
    private ResourceLocation locationSkin;

    public GraveTE()
    {

    }

    public GraveTE(World world)
    {
        setWorldObj(world);
    }

    @SideOnly(Side.CLIENT)
    public ResourceLocation getLocationSkin()
    {
        return locationSkin;
    }

    public ItemStack[] getContents()
    {
        return contents;
    }

    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        NBTTagList nbttaglist = tag.getTagList("contents");
        contents = new ItemStack[nbttaglist.tagCount()];
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            contents[i] = ItemStack.loadItemStackFromNBT((NBTTagCompound) nbttaglist.tagAt(i));
        }
        username = tag.getString("username");
    }

    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        NBTTagList nbttaglist = tag.getTagList("contents");
        for (ItemStack itemStack : contents)
        {
            nbttaglist.appendTag(itemStack == null ? new NBTTagCompound() : itemStack.writeToNBT(new NBTTagCompound()));
        }
        tag.setTag("contents", nbttaglist);
        tag.setString("username", username);
    }

    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
    {
        NBTTagList nbttaglist = pkt.data.getTagList("contents");
        contents = new ItemStack[nbttaglist.tagCount()];
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            contents[i] = ItemStack.loadItemStackFromNBT((NBTTagCompound) nbttaglist.tagAt(i));
        }
        username = pkt.data.getString("username");
    }

    public Packet getDescriptionPacket()
    {
        NBTTagCompound root = new NBTTagCompound();

        NBTTagList nbttaglist = root.getTagList("contents");
        for (ItemStack itemStack : contents)
        {
            nbttaglist.appendTag(itemStack == null ? new NBTTagCompound() : itemStack.writeToNBT(new NBTTagCompound()));
        }
        root.setTag("contents", nbttaglist);
        root.setString("username", username);

        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 15, root);
    }

    public void placeBlock(EntityLivingBase entityLiving, ItemStack stack)
    {
        username = entityLiving.getEntityName();
    }

    public boolean onActivated(EntityPlayer player, int side)
    {
        contents = new ItemStack[] {player.getHeldItem()};
        return true;
    }
}
