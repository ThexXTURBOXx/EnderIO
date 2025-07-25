package crazypants.enderio.machine.crusher;

import crazypants.enderio.machine.MachineSlot;
import crazypants.enderio.machine.OutputSlot;
import net.minecraft.entity.player.InventoryPlayer;
import crazypants.enderio.machine.AbstractMachineContainer;
import crazypants.enderio.machine.AbstractMachineEntity;

public class ContainerCrusher extends AbstractMachineContainer {

  public ContainerCrusher(InventoryPlayer playerInv, AbstractMachineEntity te) {
    super(playerInv, te);
  }

  @Override
  protected void addMachineSlots(InventoryPlayer playerInv) {
    addSlotToContainer(new MachineSlot(tileEntity, 0, 80, 12));
    addSlotToContainer(new OutputSlot(tileEntity, 1, 49, 59));
    addSlotToContainer(new OutputSlot(tileEntity, 2, 70, 59));
    addSlotToContainer(new OutputSlot(tileEntity, 3, 91, 59));
    addSlotToContainer(new OutputSlot(tileEntity, 4, 112, 59));
  }

}
