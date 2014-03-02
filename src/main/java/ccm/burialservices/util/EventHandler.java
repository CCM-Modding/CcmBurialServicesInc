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

package ccm.burialservices.util;

import ccm.burialservices.block.ToolBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.*;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class EventHandler
{
    public static final EventHandler INSTANCE = new EventHandler();

    private EventHandler() {}

    public void init()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * For deaths
     */
    @ForgeSubscribe(priority = EventPriority.HIGHEST)
    public void deathEvent(PlayerDropsEvent event)
    {

    }

    /**
     * For placing tools in the world
     */
    @ForgeSubscribe
    public void clickEvent(PlayerInteractEvent event)
    {
        World world = event.entityPlayer.getEntityWorld();
        if (world.isRemote) return;
        ItemStack itemStack = event.entityPlayer.getHeldItem();
        if (itemStack != null && event.entityPlayer.isSneaking() && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && ToolBlock.getInstance().checkMaterial(world.getBlockMaterial(event.x, event.y, event.z), itemStack.getItem()))
        {
            int x = event.x, y = event.y, z = event.z;
            if (event.face == 1 && itemStack.getItem() instanceof ItemSpade)
            {
                y++; // Cause the shovel gets placed above the block clicked
                if (world.isAirBlock(x, y, z) && world.isBlockSolidOnSide(event.x, event.y, event.z, ForgeDirection.UP))
                {
                    event.setCanceled(true);
                    world.setBlock(x, y, z, ToolBlock.getInstance().blockID, ToolBlock.getInstance().getMetaForShovel(world, x, y, z), 3);
                    ToolBlock.getInstance().onBlockPlacedBy(world, x, y, z, event.entityPlayer, itemStack);
                }
            }
            if (event.face != 1 && event.face != 0 && itemStack.getItem() instanceof ItemAxe || itemStack.getItem() instanceof ItemPickaxe)
            {
                ForgeDirection direction = ForgeDirection.VALID_DIRECTIONS[event.face];
                x += direction.offsetX;
                y += direction.offsetY;
                z += direction.offsetZ;

                if (world.isAirBlock(x, y, z) && world.isBlockSolidOnSide(event.x, event.y, event.z, direction.getOpposite()))
                {
                    event.setCanceled(true);
                    world.setBlock(x, y, z, ToolBlock.getInstance().blockID, direction.getOpposite().ordinal(), 3);
                    ToolBlock.getInstance().onBlockPlacedBy(world, x, y, z, event.entityPlayer, itemStack);
                }
            }
            if (itemStack.getItem() instanceof ItemSword)
            {
                ForgeDirection direction = ForgeDirection.VALID_DIRECTIONS[event.face];
                x += direction.offsetX;
                y += direction.offsetY;
                z += direction.offsetZ;

                if (world.isAirBlock(x, y, z) && world.isBlockSolidOnSide(event.x, event.y, event.z, direction.getOpposite()))
                {
                    int meta = direction.getOpposite().ordinal();
                    if (meta == 0)
                    {
                        System.out.println(direction.getOpposite());
                        meta = world.rand.nextInt(2);
                    }
                    event.setCanceled(true);
                    world.setBlock(x, y, z, ToolBlock.getInstance().blockID, meta, 3);
                    ToolBlock.getInstance().onBlockPlacedBy(world, x, y, z, event.entityPlayer, itemStack);
                }
            }
        }
    }
}
