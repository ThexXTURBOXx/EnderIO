package crazypants.enderio.machine.solar;

import crazypants.enderio.compat.AtlasResolver;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import crazypants.enderio.Config;
import crazypants.enderio.EnderIOTab;
import crazypants.enderio.ModObject;
import crazypants.enderio.conduit.ConduitUtil;

public class BlockSolarPanel extends BlockContainer {

  public static BlockSolarPanel create() {
    BlockSolarPanel result = new BlockSolarPanel();
    result.init();
    return result;
  }

  private static final float BLOCK_HEIGHT = 0.15f;

  int sideIcon = AtlasResolver.getLocationIndex("enderio:solarPanelSide");

  private BlockSolarPanel() {
    super(ModObject.blockSolarPanel.id, AtlasResolver.getLocationIndex("enderio:solarPanelTop"), Material.ground);
    setHardness(0.5F);
    setStepSound(Block.soundStoneFootstep);
    setBlockName(ModObject.blockSolarPanel.unlocalisedName);
    if (Config.photovoltaicCellEnabled) {
      setCreativeTab(EnderIOTab.tabEnderIO);
    } else {
      setCreativeTab(null);
    }
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, BLOCK_HEIGHT, 1.0F);
    setTextureFile(AtlasResolver.getTextureFile("enderio"));
  }

  private void init() {
    LanguageRegistry.addName(this, ModObject.blockSolarPanel.name);
    GameRegistry.registerBlock(this, ModObject.blockSolarPanel.unlocalisedName);
    GameRegistry.registerTileEntity(TileEntitySolarPanel.class, ModObject.blockSolarPanel.unlocalisedName + "TileEntity");
  }

  @Override
  public boolean renderAsNormalBlock() {
    return false;
  }

  @Override
  public boolean isOpaqueCube() {
    return false;
  }

  @Override
  public TileEntity createNewTileEntity(World world) {
    return new TileEntitySolarPanel();
  }

  @Override
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9) {
    if (ConduitUtil.isToolEquipped(entityPlayer) && entityPlayer.isSneaking()) {
      if (entityPlayer.getCurrentEquippedItem().getItem() instanceof IToolWrench) {
        IToolWrench wrench = (IToolWrench) entityPlayer.getCurrentEquippedItem().getItem();
        if (wrench.canWrench(entityPlayer, x, y, z)) {
          removeBlockByPlayer(world, entityPlayer, x, y, z);
          if (!world.isRemote && !entityPlayer.capabilities.isCreativeMode) {
            dropBlockAsItem(world, x, y, z, 0, 0);
          }
          if (entityPlayer.getCurrentEquippedItem().getItem() instanceof IToolWrench) {
            ((IToolWrench) entityPlayer.getCurrentEquippedItem().getItem()).wrenchUsed(entityPlayer, x, y, z);
          }
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public int getBlockTextureFromSideAndMetadata(int side, int meta) {
    if (side == ForgeDirection.UP.ordinal()) {
      return blockIndexInTexture;
    }
    return sideIcon;
  }

  @Override
  public void onNeighborBlockChange(World world, int x, int y, int z, int par5) {
    TileEntity te = world.getBlockTileEntity(x, y, z);
    if (te instanceof TileEntitySolarPanel) {
      ((TileEntitySolarPanel) te).onNeighborBlockChange();
    }
  }

  @Override
  public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, BLOCK_HEIGHT, 1.0F);
  }

  @Override
  public void setBlockBoundsForItemRender() {
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, BLOCK_HEIGHT, 1.0F);
  }

  @Override
  public void addCollidingBlockToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
    setBlockBoundsBasedOnState(par1World, par2, par3, par4);
    super.addCollidingBlockToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
  }

}
