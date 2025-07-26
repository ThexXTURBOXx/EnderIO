package crazypants.enderio.machine;

import java.util.Arrays;
import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import crazypants.enderio.ClientProxy;
import crazypants.enderio.EnderIO;
import crazypants.enderio.EnderIOTab;
import crazypants.enderio.ModObject;
import crazypants.enderio.compat.AtlasResolver;
import crazypants.enderio.conduit.ConduitUtil;
import crazypants.util.Util;

public abstract class AbstractMachineBlock<T extends AbstractMachineEntity> extends BlockContainer implements IGuiHandler {

  public static final String[] REDSTONE_CONTROL_ICON_FILES = new String[RedstoneControlMode.values().length];
  public static final int[] REDSTONE_CONTROL_ICONS = new int[RedstoneControlMode.values().length];

  @SideOnly(Side.CLIENT)
  public static void initIcon() {
    Arrays.fill(REDSTONE_CONTROL_ICON_FILES, AtlasResolver.getTextureFile("enderio"));
    REDSTONE_CONTROL_ICONS[RedstoneControlMode.IGNORE.ordinal()] = AtlasResolver.getLocationIndex("enderio:iconRedstoneIgnore");
    REDSTONE_CONTROL_ICONS[RedstoneControlMode.ON.ordinal()] = AtlasResolver.getLocationIndex("enderio:iconRedstoneOn");
    REDSTONE_CONTROL_ICONS[RedstoneControlMode.OFF.ordinal()] = AtlasResolver.getLocationIndex("enderio:iconRedstoneOff");
    REDSTONE_CONTROL_ICONS[RedstoneControlMode.NEVER.ordinal()] = AtlasResolver.getLocationIndex("enderio:iconRedstoneNever");
  }

  @SideOnly(Side.CLIENT)
  public static String getRedstoneControlIconFile(RedstoneControlMode mode) {
    return REDSTONE_CONTROL_ICON_FILES[mode.ordinal()];
  }

  @SideOnly(Side.CLIENT)
  public static int getRedstoneControlIcon(RedstoneControlMode mode) {
    return REDSTONE_CONTROL_ICONS[mode.ordinal()];
  }

  protected int[][] iconBuffer;

  protected final Random random;

  protected final ModObject modObject;

  protected final Class<T> teClass;

  protected AbstractMachineBlock(ModObject mo, Class<T> teClass) {
    super(mo.id, new Material(MapColor.ironColor));
    modObject = mo;
    this.teClass = teClass;
    setHardness(2.0F);
    setStepSound(soundMetalFootstep);
    setBlockName(mo.unlocalisedName);
    setCreativeTab(EnderIOTab.tabEnderIO);
    random = new Random();

  }

  protected void init() {
    LanguageRegistry.addName(this, modObject.name);
    GameRegistry.registerBlock(this, modObject.unlocalisedName);
    GameRegistry.registerTileEntity(teClass, modObject.unlocalisedName + "TileEntity");
    EnderIO.guiHandler.registerGuiHandler(getGuiId(), this);
    setTextureFile(AtlasResolver.getTextureFile("enderio"));
    iconBuffer = new int[1][12];
    String side = getSideIconKey();
    // first the 6 sides in OFF state
    iconBuffer[0][0] = AtlasResolver.getLocationIndex(side);
    iconBuffer[0][1] = AtlasResolver.getLocationIndex(side);
    iconBuffer[0][2] = AtlasResolver.getLocationIndex(side);
    iconBuffer[0][3] = AtlasResolver.getLocationIndex(getMachineFrontIconKey(false));
    iconBuffer[0][4] = AtlasResolver.getLocationIndex(side);
    iconBuffer[0][5] = AtlasResolver.getLocationIndex(side);

    iconBuffer[0][6] = AtlasResolver.getLocationIndex(side);
    iconBuffer[0][7] = AtlasResolver.getLocationIndex(side);
    iconBuffer[0][8] = AtlasResolver.getLocationIndex(side);
    iconBuffer[0][9] = AtlasResolver.getLocationIndex(getMachineFrontIconKey(true));
    iconBuffer[0][10] = AtlasResolver.getLocationIndex(side);
    iconBuffer[0][11] = AtlasResolver.getLocationIndex(side);
  }

  @Override
  public TileEntity createNewTileEntity(World world) {
    return null;
  }

  @Override
  public TileEntity createTileEntity(World world, int metadata) {
    try {
      return teClass.newInstance();
    } catch (Exception e) {
      FMLCommonHandler.instance().raiseException(e, "Could not create tile entity from class " + teClass, true);
      return null;
    }
  }

  @Override
  public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9) {

    if (ConduitUtil.isToolEquipped(entityPlayer) && entityPlayer.isSneaking()) {
      if (entityPlayer.getCurrentEquippedItem().getItem() instanceof IToolWrench) {
        IToolWrench wrench = (IToolWrench) entityPlayer.getCurrentEquippedItem().getItem();
        if (wrench.canWrench(entityPlayer, x, y, z)) {
          removeBlockByPlayer(world, entityPlayer, x, y, z);
          if (entityPlayer.getCurrentEquippedItem().getItem() instanceof IToolWrench) {
            ((IToolWrench) entityPlayer.getCurrentEquippedItem().getItem()).wrenchUsed(entityPlayer, x, y, z);
          }
          return true;
        }
      }
    }

    if (entityPlayer.isSneaking()) {
      return false;
    }
    entityPlayer.openGui(EnderIO.instance, getGuiId(), world, x, y, z);
    return true;
  }

  @Override
  public int getBlockTexture(IBlockAccess world, int x, int y, int z, int blockSide) {
    // used to render the block in the world
    TileEntity te = world.getBlockTileEntity(x, y, z);
    int facing = 0;
    if (te instanceof AbstractMachineEntity) {
      AbstractMachineEntity me = (AbstractMachineEntity) te;
      facing = me.facing;
    }
    if (isActive(world, x, y, z)) {
      return iconBuffer[0][ClientProxy.sideAndFacingToSpriteOffset[blockSide][facing] + 6];
    } else {
      return iconBuffer[0][ClientProxy.sideAndFacingToSpriteOffset[blockSide][facing]];
    }
  }

  @Override
  public int getBlockTextureFromSideAndMetadata(int blockSide, int blockMeta) {
    // This is used to render the block as an item
    return iconBuffer[0][blockSide];
  }

  @Override
  public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
    if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
      TileEntity ent = world.getBlockTileEntity(x, y, z);
      if (ent != null) {
        if (teClass.isAssignableFrom(ent.getClass())) {
          @SuppressWarnings("unchecked")
          T te = (T) world.getBlockTileEntity(x, y, z);
          Util.dropItems(world, te, x, y, z, true);
        }
      }
    }
    world.removeBlockTileEntity(x, y, z);
  }

  @Override
  public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z) {
    if (!world.isRemote && !player.capabilities.isCreativeMode) {
      ItemStack st = new ItemStack(this);
      Util.dropItems(world, st, x, y, z, false);
    }
    return super.removeBlockByPlayer(world, player, x, y, z);
  }

  @Override
  public int idDropped(int par1, Random par2Random, int par3) {
    return 0;
  }

  @Override
  public int quantityDropped(Random r) {
    return 0;
  }

  @Override
  public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving player) {
    super.onBlockPlacedBy(world, x, y, z, player);
    int heading = MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
    AbstractMachineEntity te = (AbstractMachineEntity) world.getBlockTileEntity(x, y, z);
    switch (heading) {
    case 0:
      te.setFacing((short) 2);
      break;
    case 1:
      te.setFacing((short) 5);
      break;
    case 2:
      te.setFacing((short) 3);
      break;
    case 3:
      te.setFacing((short) 4);
      break;
    default:
      break;
    }
    world.markBlockForUpdate(x, y, z);
  }

  @Override
  public void onBlockAdded(World world, int x, int y, int z) {
    super.onBlockAdded(world, x, y, z);
    world.markBlockForUpdate(x, y, z);
  }

  @Override
  public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
    TileEntity ent = world.getBlockTileEntity(x, y, z);
    if (ent instanceof AbstractMachineEntity) {
      AbstractMachineEntity te = (AbstractMachineEntity) ent;
      te.onNeighborBlockChange(blockId);
    }
  }

  @Override
  public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
    // If active, randomly throw some smoke around
    if (isActive(world, x, y, z)) {
      float startX = x + 1.0F;
      float startY = y + 1.0F;
      float startZ = z + 1.0F;
      for (int i = 0; i < 4; i++) {
        float xOffset = -0.2F - rand.nextFloat() * 0.6F;
        float yOffset = -0.1F + rand.nextFloat() * 0.2F;
        float zOffset = -0.2F - rand.nextFloat() * 0.6F;
        world.spawnParticle("smoke", startX + xOffset, startY + yOffset, startZ + zOffset, 0.0D, 0.0D, 0.0D);
      }
    }
  }

  protected String getSideIconKey() {
    return "enderio:machineSide";
  }

  protected abstract int getGuiId();

  protected abstract String getMachineFrontIconKey(boolean active);

  private boolean isActive(IBlockAccess blockAccess, int x, int y, int z) {
    TileEntity te = blockAccess.getBlockTileEntity(x, y, z);
    if(te instanceof AbstractMachineEntity) {
      return ((AbstractMachineEntity) te).isActive();
    }
    return false;
  }

}
