package crazypants.enderio.machine.alloy;

import crazypants.enderio.machine.MachineSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import crazypants.enderio.machine.AbstractMachineContainer;
import crazypants.util.Util;

public class ContainerAlloySmelter extends AbstractMachineContainer {

  private EntityPlayer thePlayer;
  private TileAlloySmelter smelter;

  public ContainerAlloySmelter(InventoryPlayer playerInv, TileAlloySmelter te) {
    super(playerInv, te);
    thePlayer = playerInv.player;
    smelter = te;
  }

  @Override
  protected void addMachineSlots(InventoryPlayer playerInv) {
    addSlotToContainer(new MachineSlot(tileEntity, 0, 54, 17));
    addSlotToContainer(new MachineSlot(tileEntity, 1, 78, 7));
    addSlotToContainer(new MachineSlot(tileEntity, 2, 103, 17));
    addSlotToContainer(new SlotSmelter(tileEntity, 3, 79, 57));

  }

  private class SlotSmelter extends Slot {

    int numResults = 0;

    public SlotSmelter(IInventory par1iInventory, int par2, int par3, int par4) {
      super(par1iInventory, par2, par3, par4);
    }

    @Override
    public ItemStack decrStackSize(int par1) {
      if (getHasStack()) {
        numResults += Math.min(par1, getStack().stackSize);
      }
      return super.decrStackSize(par1);
    }

    @Override
    public boolean isItemValid(ItemStack par1ItemStack) {
      return false;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack output) {
      onCrafting(output);
      super.onPickupFromSlot(par1EntityPlayer, output);
    }

    @Override
    protected void onCrafting(ItemStack par1ItemStack, int par2) {
      numResults += par2;
      onCrafting(par1ItemStack);
    }

    @Override
    protected void onCrafting(ItemStack output) {
      output.onCrafting(thePlayer.worldObj, thePlayer, numResults);
      if (output != null) {
        if (!thePlayer.worldObj.isRemote) {
          ItemStack outputSized = output.copy();
          outputSized.stackSize = numResults;
          float experience = smelter.getExperienceForOutput(outputSized);
          Util.giveExperience(thePlayer, experience);
        }

      }
      numResults = 0;
    }

  }

}
