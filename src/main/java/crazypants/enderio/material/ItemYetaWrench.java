package crazypants.enderio.material;

import crazypants.enderio.EnderIO;
import crazypants.enderio.compat.RotationHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import crazypants.enderio.EnderIOTab;
import crazypants.enderio.ModObject;

public class ItemYetaWrench extends Item implements IToolWrench {

  public static ItemYetaWrench create() {
    ItemYetaWrench result = new ItemYetaWrench();
    result.init();
    return result;
  }

  protected ItemYetaWrench() {
    super(ModObject.itemYetaWrench.id);
    setCreativeTab(EnderIOTab.tabEnderIO);
    setItemName(ModObject.itemYetaWrench.unlocalisedName);
    setMaxStackSize(1);
  }

  protected void init() {
    LanguageRegistry.addName(this, ModObject.itemYetaWrench.name);
    GameRegistry.registerItem(this, ModObject.itemYetaWrench.unlocalisedName);
    setIconIndex(EnderIO.ATLAS_RESOLVER.getLocationIndex("enderio:yetaWrench"));
  }

  @Override
  public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
    int blockId = world.getBlockId(x, y, z);
    Block block = Block.blocksList[blockId];
    if (block != null && RotationHelper.rotateVanillaBlock(block, world, x, y, z, ForgeDirection.getOrientation(side))) {
      player.swingItem();
      return !world.isRemote;
    }
    return false;
  }

  @Override
  public boolean canWrench(EntityPlayer player, int x, int y, int z) {
    return true;
  }

  @Override
  public void wrenchUsed(EntityPlayer player, int x, int y, int z) {
    player.swingItem();
  }

  @Override
  public boolean shouldPassSneakingClickToBlock(World par2World, int par4, int par5, int par6) {
    return true;
  }

}
