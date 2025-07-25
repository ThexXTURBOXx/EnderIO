package crazypants.enderio.machine.power;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import thermalexpansion.api.core.IChargeableItem;

public class ChargableSlot extends Slot {

    public ChargableSlot(IInventory par1IInventory, int par2, int par3, int par4) {
        super(par1IInventory, par2, par3, par4);
    }

    @Override
    public boolean isItemValid(ItemStack par1ItemStack) {
        return isItemValid(slotNumber, par1ItemStack);
    }

    public boolean isItemValid(int slot, ItemStack par1ItemStack) {
        return isItemChargable(par1ItemStack);
    }

    public static boolean isItemChargable(ItemStack par1ItemStack) {
        if (par1ItemStack == null) return false;
        return par1ItemStack.getItem() instanceof IChargeableItem;
    }

}
