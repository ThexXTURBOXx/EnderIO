package crazypants.enderio.machine;

import crazypants.enderio.ModObject;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class MachineSlot extends Slot {

    private final AbstractMachineEntity machine;

    public MachineSlot(AbstractMachineEntity par1Machine, int par2, int par3, int par4) {
        super(par1Machine, par2, par3, par4);
        this.machine = par1Machine;
    }

    @Override
    public boolean isItemValid(ItemStack par1ItemStack) {
        return isItemValid(slotNumber, par1ItemStack);
    }

    public boolean isItemValid(int slot, ItemStack par1ItemStack) {
        if (machine.slotDefinition.isUpgradeSlot(slot)) {
            return par1ItemStack.itemID == ModObject.itemBasicCapacitor.actualId && par1ItemStack.getItemDamage() > 0;
        }
        return machine.isMachineItemValidForSlot(slot, par1ItemStack);
    }

}
