package crazypants.enderio.machine.painter;

import crazypants.enderio.machine.MachineSlot;
import crazypants.enderio.machine.OutputSlot;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import crazypants.enderio.machine.AbstractMachineContainer;
import crazypants.enderio.machine.AbstractMachineEntity;

public class PainterContainer extends AbstractMachineContainer {

  public PainterContainer(InventoryPlayer playerInv, AbstractMachineEntity te) {
    super(playerInv, te);
  }

  @Override
  protected void addMachineSlots(InventoryPlayer playerInv) {
    addSlotToContainer(new MachineSlot(tileEntity, 0, 67, 34));
    addSlotToContainer(new MachineSlot(tileEntity, 1, 37, 34));
    addSlotToContainer(new OutputSlot(tileEntity, 2, 121, 34));
  }

}
