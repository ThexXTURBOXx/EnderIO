package crazypants.enderio.material;

import crazypants.enderio.EnderIO;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import crazypants.enderio.EnderIOTab;
import crazypants.enderio.ModObject;

public class ItemMJReader extends Item {

  public static ItemMJReader create() {
    ItemMJReader result = new ItemMJReader();
    result.init();
    return result;
  }

  protected ItemMJReader() {
    super(ModObject.itemMJReader.id);
    setCreativeTab(EnderIOTab.tabEnderIO);
    setItemName(ModObject.itemMJReader.unlocalisedName);
    setMaxStackSize(64);
  }

  protected void init() {
    LanguageRegistry.addName(this, ModObject.itemMJReader.name);
    GameRegistry.registerItem(this, ModObject.itemMJReader.unlocalisedName);
    setIconIndex(EnderIO.ATLAS_RESOLVER.getLocationIndex("enderio:mJReader"));
  }

}
