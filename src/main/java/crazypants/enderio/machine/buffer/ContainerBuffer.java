package crazypants.enderio.machine.buffer;

import java.awt.Point;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import crazypants.enderio.machine.AbstractMachineEntity;
import crazypants.enderio.machine.gui.AbstractMachineContainer;

public class ContainerBuffer extends AbstractMachineContainer {

  public ContainerBuffer(InventoryPlayer playerInv, AbstractMachineEntity te) {
    super(playerInv, te);
  }

  @Override
  protected void addMachineSlots(InventoryPlayer playerInv) {
    Point point = new Point(62, 15);
    for (int i = 0; i < 9; i++) {
      addSlotToContainer(new Slot(this.tileEntity, i, point.x + ((i % 3) * 18), point.y + ((i / 3) * 18)));
    }
  }
}
