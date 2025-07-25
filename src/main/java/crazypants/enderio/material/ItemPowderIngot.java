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

public class ItemPowderIngot extends Item {

  private final int[] icons;

  public static ItemPowderIngot create() {
    ItemPowderIngot mp = new ItemPowderIngot();
    mp.init();
    return mp;
  }

  private ItemPowderIngot() {
    super(ModObject.itemPowderIngot.id);
    setHasSubtypes(true);
    setMaxDamage(0);
    setCreativeTab(EnderIOTab.tabEnderIO);
    setItemName(ModObject.itemPowderIngot.unlocalisedName);

    icons = new int[PowderIngot.values().length];
  }

  private void init() {
    LanguageRegistry.addName(this, ModObject.itemPowderIngot.name);
    GameRegistry.registerItem(this, ModObject.itemPowderIngot.unlocalisedName);
    for (int i = 0; i < PowderIngot.values().length; i++) {
      LanguageRegistry.instance().addStringLocalization(getItemName() + "." + PowderIngot.values()[i].unlocalisedName + ".name",
          PowderIngot.values()[i].uiName);
    }
    int numParts = PowderIngot.values().length;
    for (int i = 0; i < numParts; i++) {
      icons[i] = EnderIO.ATLAS_RESOLVER.getLocationIndex(PowderIngot.values()[i].iconKey);
    }
  }

  @Override
  public int getIconFromDamage(int damage) {
    damage = MathHelper.clamp_int(damage, 0, PowderIngot.values().length);
    return icons[damage];
  }

  @Override
  public String getItemNameIS(ItemStack par1ItemStack) {
    int i = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, PowderIngot.values().length);
    return super.getItemName() + "." + PowderIngot.values()[i].unlocalisedName;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
    for (int j = 0; j < PowderIngot.values().length; ++j) {
      par3List.add(new ItemStack(par1, 1, j));
    }
  }

}
