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

import ccm.burialservices.BurialServices;
import ccm.burialservices.util.BSConstants;
import ccm.nucleumOmnium.helpers.MiscHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GraveTE extends TileEntity
{
    private ItemStack   holding  = null;
    private ItemStack[] contents = new ItemStack[0];
    private String      username = "";
    private ResourceLocation skin;
    public  String[]         text;
    private int         age = 0;

    public GraveTE()
    {

    }

    public GraveTE(World world)
    {
        setWorldObj(world);
    }

    public ItemStack getHolding()
    {
        return holding;
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
        holding = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("holding"));
        text = tag.getString("text").split("\n");
        age = tag.getInteger("age");
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
        tag.setCompoundTag("holding", holding == null ? new NBTTagCompound() : holding.writeToNBT(new NBTTagCompound()));
        tag.setString("text", BSConstants.TEXT_JOINER.join(text));
        tag.setInteger("age", age);
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
        holding = ItemStack.loadItemStackFromNBT(pkt.data.getCompoundTag("holding"));
        text = pkt.data.getString("text").split("\n");
        age = pkt.data.getInteger("age");
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
        root.setCompoundTag("holding", holding == null ? new NBTTagCompound() : holding.writeToNBT(new NBTTagCompound()));
        root.setString("text", BSConstants.TEXT_JOINER.join(text));
        root.setInteger("age", age);

        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 15, root);
    }

    public void placeBlock(EntityLivingBase entityLiving, ItemStack stack)
    {
        username = entityLiving.getEntityName();
        if (entityLiving instanceof EntityPlayer) applyPlayerProperties((EntityPlayer) entityLiving);
    }

    public static boolean willHold(ItemStack itemStack)
    {
        if (itemStack == null) return false;
        Item item = itemStack.getItem();
        return item instanceof ItemTool || item instanceof ItemHoe || item instanceof ItemSword;
    }

    public boolean onActivated(EntityPlayer player, int side)
    {
        //if (player.getHeldItem() != null && willHold(player.getHeldItem())) contents = new ItemStack[] {player.getHeldItem()};
        return false;
    }

    public void applyPlayerProperties(EntityPlayer entityPlayer)
    {
        username = entityPlayer.username;
        NBTTagCompound data = MiscHelper.getPersistentDataTag(entityPlayer, BSConstants.NBT_PLAYER_GRAVE_DATA);
        text = data.getString("text").split("\n");
        boolean empty = true;
        for (String temp : text) if (!temp.isEmpty()) empty = false;
        if (empty)
        {
            text = new String[] {"", "Here lies", username, ""};
        }
    }

    public void setHolding(ItemStack holding)
    {
        this.holding = holding;
    }

    public ItemStack[] getContents()
    {
        return contents;
    }

    public void setContents(ItemStack[] contents)
    {
        this.contents = contents;
    }

    public String getUsername()
    {
        return username;
    }

    public ResourceLocation getLocationSkin()
    {
        if (skin == null) skin = AbstractClientPlayer.getLocationSkin(username);
        if (skin == null) skin = AbstractClientPlayer.locationStevePng;
        return skin;
    }

    public void invalidate()
    {
        super.invalidate();

        if (worldObj.isRemote) return;

        for (ItemStack itemStack : contents)
        {
            MiscHelper.dropItems(worldObj, itemStack, xCoord, yCoord, zCoord);
        }

        if (BurialServices.getConfig().giveSkull)
        {
            ItemStack skull = new ItemStack(Item.skull, 1, 3);
            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setString("SkullOwner", username);
            skull.setTagCompound(tagCompound);

            MiscHelper.dropItems(worldObj, skull, xCoord, yCoord, zCoord);
        }
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return AxisAlignedBB.getAABBPool().getAABB(xCoord - 1, yCoord - 1, zCoord - 1, xCoord + 2, yCoord + 2, zCoord + 2);
    }
}
