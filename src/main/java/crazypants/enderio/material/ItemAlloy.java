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

public class ItemAlloy extends Item {

  static final boolean useNuggets = false;

  private final int[] icons;
  private final int numItems;

  public static ItemAlloy create() {
    ItemAlloy alloy = new ItemAlloy();
    alloy.init();
    return alloy;
  }

  private ItemAlloy() {
    super(ModObject.itemAlloy.id);
    setHasSubtypes(true);
    setMaxDamage(0);
    setCreativeTab(EnderIOTab.tabEnderIO);
    setItemName(ModObject.itemAlloy.unlocalisedName);

    numItems = Alloy.values().length;
    if (useNuggets) {
      numItems = numItems * 2;
    }
    icons = new int[numItems];
  }

  private void init() {
    LanguageRegistry.addName(this, ModObject.itemAlloy.name);
    GameRegistry.registerItem(this, ModObject.itemAlloy.unlocalisedName);
    for (int i = 0; i < Alloy.values().length; i++) {
      LanguageRegistry.instance().addStringLocalization(getItemName() + "." + Alloy.values()[i].unlocalisedName + ".name", Alloy.values()[i].uiName);
    }
    if (useNuggets) {
      for (int i = 0; i < Alloy.values().length; i++) {
        LanguageRegistry.instance().addStringLocalization(getItemName() + "." + Alloy.values()[i].unlocalisedName + "Nugget" + ".name",
            Alloy.values()[i].uiName + " Nugget");
      }
    }
    int numAlloys = Alloy.values().length;
    for (int i = 0; i < numAlloys; i++) {
      icons[i] = EnderIO.ATLAS_RESOLVER.getLocationIndex(Alloy.values()[i].iconKey);
    }
    if (useNuggets) {
      for (int i = 0; i < numAlloys; i++) {
        icons[i + numAlloys] = EnderIO.ATLAS_RESOLVER.getLocationIndex(Alloy.values()[i].iconKey + "Nugget");
      }
    }
  }

  @Override
  public int getIconFromDamage(int damage) {
    damage = MathHelper.clamp_int(damage, 0, numItems - 1);
    return icons[damage];
  }

  @Override
  public String getItemNameIS(ItemStack par1ItemStack) {
    int i = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, numItems - 1);
    if (i < Alloy.values().length) {
      return super.getItemName() + "." + Alloy.values()[i].unlocalisedName;
    } else {
      return super.getItemName() + "." + Alloy.values()[i - Alloy.values().length].unlocalisedName + "Nugget";
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
    for (int j = 0; j < numItems; ++j) {
      par3List.add(new ItemStack(par1, 1, j));
    }
  }

}
