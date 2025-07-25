package crazypants.enderio.trigger;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import crazypants.enderio.EnderIO;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import thermalexpansion.api.core.IChargeableItem;
import buildcraft.api.gates.ActionManager;
import buildcraft.api.gates.ITriggerParameter;
import buildcraft.api.gates.Trigger;
import crazypants.enderio.machine.power.TileCapacitorBank;

public class TriggerEnderIO extends Trigger {

  public static String[] descriptions = new String[] { "Capacitor Bank has no energy stored", "Capacitor Bank has energy stored",
      "Capacitor Bank is full with energy", "Capacitor Bank is charging items", "Capacitor Bank finished charging items" };

  public static int[] ICONS = new int[5];

  public String uniqueTag;

  public int triggerIndex;

  public TriggerEnderIO(String uniqueTag, int triggerID) {
    super(getNextFreeTriggerID());

    this.uniqueTag = uniqueTag;
    this.triggerIndex = triggerID;
  }

  private static int getNextFreeTriggerID() {
    int i;
    for (i = 0; i < ActionManager.triggers.length; i++)
      if(ActionManager.triggers[i] == null) break;
    return i;
  }

  @SideOnly(Side.CLIENT)
  public static void initIcons() {
    ICONS[0] = EnderIO.ATLAS_RESOLVER.getLocationIndex("enderio:triggers/noEnergy");
    ICONS[1] = EnderIO.ATLAS_RESOLVER.getLocationIndex("enderio:triggers/hasEnergy");
    ICONS[2] = EnderIO.ATLAS_RESOLVER.getLocationIndex("enderio:triggers/fullEnergy");
    ICONS[3] = EnderIO.ATLAS_RESOLVER.getLocationIndex("enderio:triggers/charging");
    ICONS[4] = EnderIO.ATLAS_RESOLVER.getLocationIndex("enderio:triggers/chargingDone");
  }

  @Override
  public String getDescription() {
    return descriptions[triggerIndex];
  }

  @Override
  public boolean isTriggerActive(TileEntity tile, ITriggerParameter parameter) {
    if(tile instanceof TileCapacitorBank) {
      TileCapacitorBank capacitorBank = (TileCapacitorBank) tile;

      if(triggerIndex == 0) {
        return capacitorBank.getEnergyStored() == 0;
      }
      if(triggerIndex == 1) {
        return capacitorBank.getEnergyStored() != 0;
      }
      if(triggerIndex == 2) {
        return capacitorBank.getEnergyStored() == capacitorBank.getMaxEnergyStored();
      }
      if(triggerIndex == 3 || triggerIndex == 4) {
        ItemStack[] items = new ItemStack[capacitorBank.getSizeInventory()];
        for (int i = 0; i < items.length; i++) {
          items[i] = capacitorBank.getStackInSlot(i);
        }
        boolean capacitorBankEmpty = true;
        for (int i = 0; i < items.length; i++) {
          if(items[i] != null && items[i].getItem() instanceof IChargeableItem) {
            capacitorBankEmpty = false;
          }
        }
        if(capacitorBankEmpty) {
          return false;
        }
        boolean hasUnchargedItems = false;
        for (int i = 0; i < items.length; i++) {
          if(items[i] != null && items[i].getItem() instanceof IChargeableItem) {
            IChargeableItem item = (IChargeableItem) items[i].getItem();
            if(item.getEnergyStored(items[i]) < item.getMaxEnergyStored(items[i])) {
              hasUnchargedItems = true;
            }
          }
        }
        if(hasUnchargedItems && triggerIndex == 3) {
          return true;
        }
        if(!hasUnchargedItems && triggerIndex == 4) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public int getIndexInTexture() {
    return ICONS[triggerIndex];
  }

  @Override
  public String getTextureFile() {
    return EnderIO.ATLAS_RESOLVER.getTextureFile();
  }

  @Override
  public boolean hasParameter() {
    return false;
  }

}
