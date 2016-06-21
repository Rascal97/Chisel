package team.chisel.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import team.chisel.common.item.ItemChisel;

public class SlotChiselSelection extends Slot {

    private final ContainerChisel container;
    private final InventoryChiselSelection selInventory;

    public SlotChiselSelection(ContainerChisel container, InventoryChiselSelection inv, IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);

        this.container = container;
        selInventory = inv;
    }

    @Override
    public boolean isItemValid(ItemStack itemstack) {
        return false;
    }

    @Override
    public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
        if (container.finished)
            return false;

        return par1EntityPlayer.inventory.getItemStack() == null;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack itemstack) {
        ItemStack heldStack = player.inventory.getItemStack();
        ItemStack crafted = selInventory.inventory[InventoryChiselSelection.normalSlots];

        if (heldStack == null) {
            selInventory.decrStackSize(InventoryChiselSelection.normalSlots, 1);
        } else {
            putStack(itemstack.copy());

            player.inventory.setItemStack(null);

            if (selInventory.inventory[InventoryChiselSelection.normalSlots] == null)
                return;
            if (selInventory.chisel != null && selInventory.chisel.getItem() instanceof ItemChisel){
                selInventory.chisel.damageItem(selInventory.inventory[InventoryChiselSelection.normalSlots].stackSize, player);
            }
            player.inventory.setItemStack(new ItemStack(itemstack.getItem(), selInventory.inventory[InventoryChiselSelection.normalSlots].stackSize, itemstack.getItemDamage()));
            selInventory.setInventorySlotContents(InventoryChiselSelection.normalSlots, null);
        }

        selInventory.updateItems();


        if (player.worldObj.isRemote) {
            //todo Add sound
//            String sound = CarvableBlocks.fromItemStack(crafted).getSound();
//            ClientUtil.playSound(player.worldObj, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ), sound);
        } else {
            //container.playerInventory.player.addStat(Statistics.blocksChiseled, crafted.stackSize);
        }
    }
}