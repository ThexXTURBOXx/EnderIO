package crazypants.enderio.machine;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class OutputSlot extends Slot {

    public OutputSlot(IInventory par1IInventory, int par2, int par3, int par4) {
        super(par1IInventory, par2, par3, par4);
    }

    @Override
    public boolean isItemValid(ItemStack par1ItemStack) {
        return isItemValid(slotNumber, par1ItemStack);
    }

    public boolean isItemValid(int slot, ItemStack par1ItemStack) {
        return false;
    }

}
