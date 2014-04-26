package ccm.burialservices;

import ccm.burialservices.block.GraveBlock;
import ccm.burialservices.te.GraveTE;
import ccm.burialservices.util.BSConstants;
import ccm.nucleumOmnium.helpers.MiscHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;

public class GraveHandler
{
    public static boolean onDeath(World world, EntityPlayer player, ArrayList<EntityItem> drops)
    {
        NBTTagCompound graveData = MiscHelper.getPersistentDataTag(player, BSConstants.NBT_PLAYER_GRAVE_DATA);
        if (graveData.getBoolean(BSConstants.NBT_PLAYER_DISABLEGRAVE)) return false;

        int x = (int) player.posX;
        int y = (int) player.posY;
        int z = (int) player.posZ;
        world.setBlock(x, y, z, GraveBlock.getInstance().blockID, world.rand.nextInt(4), 3);
        GraveTE te  = ((GraveTE) world.getBlockTileEntity(x, y, z));
        te.applyPlayerProperties(player);

        int cap = graveData.hasKey(BSConstants.NBT_GRAVE_CAPACITY) ? graveData.getInteger(BSConstants.NBT_GRAVE_CAPACITY) : BurialServices.getConfig().defaultGraveSize;
        ItemStack[] graveContent = new ItemStack[cap];
        int i = 0; // keeps track of pos in graveContent
        Iterator<EntityItem> itemIterator = drops.iterator();
        while (itemIterator.hasNext())
        {
            ItemStack stack = itemIterator.next().getEntityItem();
            if (GraveTE.willHold(stack))
            {
                te.setHolding(stack);
                graveContent[i ++] = stack;
                itemIterator.remove();
                break;
            }
        }

        itemIterator = drops.iterator();
        while (itemIterator.hasNext() && i < cap)
        {
            ItemStack stack = itemIterator.next().getEntityItem();
            graveContent[i ++] = stack;
            itemIterator.remove();
        }
        te.setContents(graveContent);

        return drops.isEmpty();
    }
}
