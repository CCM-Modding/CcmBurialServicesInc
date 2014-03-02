package ccm.burialservices.util;

import ccm.burialservices.block.ToolBlock;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
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
        ItemStack itemStack = event.entityPlayer.getHeldItem();
        if (!world.isRemote && itemStack != null && event.entityPlayer.isSneaking() && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && ToolBlock.getInstance().checkMaterial(world.getBlockMaterial(event.x, event.y, event.z), itemStack.getItem()))
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
        }
    }
}
