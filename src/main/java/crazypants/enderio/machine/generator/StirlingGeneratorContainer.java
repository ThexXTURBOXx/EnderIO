package crazypants.enderio.machine.generator;

import crazypants.enderio.machine.MachineSlot;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import crazypants.enderio.machine.AbstractMachineContainer;
import crazypants.enderio.machine.AbstractMachineEntity;

public class StirlingGeneratorContainer extends AbstractMachineContainer {

  public StirlingGeneratorContainer(InventoryPlayer playerInv, AbstractMachineEntity te) {
    super(playerInv, te);
  }

  @Override
  protected void addMachineSlots(InventoryPlayer playerInv) {
    addSlotToContainer(new MachineSlot(tileEntity, 0, 80, 34));
  }

}
