package crazypants.enderio.material;

import crazypants.enderio.EnderIO;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import crazypants.enderio.EnderIOTab;
import crazypants.enderio.ModObject;
import crazypants.enderio.power.BasicCapacitor;
import crazypants.enderio.power.Capacitors;
import crazypants.enderio.power.ICapacitor;
import crazypants.enderio.power.ICapacitorItem;

public class ItemCapacitor extends Item implements ICapacitorItem {

  private static final BasicCapacitor CAP = new BasicCapacitor();

  public static ItemCapacitor create() {
    ItemCapacitor result = new ItemCapacitor();
    result.init();
    return result;
  }

  private final int[] icons;

  protected ItemCapacitor() {
    super(ModObject.itemBasicCapacitor.id);
    setCreativeTab(EnderIOTab.tabEnderIO);
    setItemName(ModObject.itemBasicCapacitor.unlocalisedName);
    setHasSubtypes(true);
    setMaxDamage(0);
    setMaxStackSize(64);

    icons = new int[Capacitors.values().length];
  }

  protected void init() {
    LanguageRegistry.addName(this, ModObject.itemBasicCapacitor.name);
    GameRegistry.registerItem(this, ModObject.itemBasicCapacitor.unlocalisedName);
    for (int i = 0; i < Capacitors.values().length; i++) {
      LanguageRegistry.instance().addStringLocalization(getItemName() + "." + Capacitors.values()[i].unlocalisedName + ".name",
          Capacitors.values()[i].uiName);
    }
    for (int i = 0; i < Capacitors.values().length; i++) {
      icons[i] = EnderIO.ATLAS_RESOLVER.getLocationIndex(Capacitors.values()[i].iconKey);
    }
  }

  @Override
  public int getIconFromDamage(int damage) {
    damage = MathHelper.clamp_int(damage, 0, Capacitors.values().length);
    return icons[damage];
  }

  @Override
  public String getItemNameIS(ItemStack par1ItemStack) {
    int i = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, Capacitors.values().length);
    return super.getItemName() + "." + Capacitors.values()[i].unlocalisedName;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
    for (int j = 0; j < Capacitors.values().length; ++j) {
      par3List.add(new ItemStack(par1, 1, j));
    }
  }

  @Override
  public ICapacitor getCapacitor(ItemStack stack) {
    int damage = MathHelper.clamp_int(stack.getItemDamage(), 0, Capacitors.values().length);
    return Capacitors.values()[damage].capacitor;
  }

}
