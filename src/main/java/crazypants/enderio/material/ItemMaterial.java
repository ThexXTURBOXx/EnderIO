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

public class ItemMaterial extends Item {

  private final int[] icons;

  public static ItemMaterial create() {
    ItemMaterial mp = new ItemMaterial();
    mp.init();
    return mp;
  }

  private ItemMaterial() {
    super(ModObject.itemMaterial.id);
    setHasSubtypes(true);
    setMaxDamage(0);
    setCreativeTab(EnderIOTab.tabEnderIO);
    setItemName(ModObject.itemMaterial.unlocalisedName);

    icons = new int[Material.values().length];
  }

  private void init() {
    LanguageRegistry.addName(this, ModObject.itemMaterial.name);
    GameRegistry.registerItem(this, ModObject.itemMaterial.unlocalisedName);
    for (int i = 0; i < Material.values().length; i++) {
      LanguageRegistry.instance().addStringLocalization(getItemName() + "." + Material.values()[i].unlocalisedName + ".name",
          Material.values()[i].uiName);
    }
    int numParts = Material.values().length;
    for (int i = 0; i < numParts; i++) {
      icons[i] = EnderIO.ATLAS_RESOLVER.getLocationIndex(Material.values()[i].iconKey);
    }
  }

  @Override
  public int getIconFromDamage(int damage) {
    damage = MathHelper.clamp_int(damage, 0, Material.values().length);
    return icons[damage];
  }

  @Override
  public String getItemNameIS(ItemStack par1ItemStack) {
    int i = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, Material.values().length);
    return super.getItemName() + "." + Material.values()[i].unlocalisedName;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
    for (int j = 0; j < Material.values().length; ++j) {
      par3List.add(new ItemStack(par1, 1, j));
    }
  }

}
