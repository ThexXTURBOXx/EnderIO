package crazypants.render;

import net.minecraft.item.Item;

public class IconUtil {

  public static int getIconForItem(int itemId, int meta) {
    if (itemId < 0 || itemId >= Item.itemsList.length) {
      return 0;
    }
    Item item = Item.itemsList[itemId];
    if (item == null) {
      return 0;
    }
    return item.getIconFromDamage(meta);
  }

}
