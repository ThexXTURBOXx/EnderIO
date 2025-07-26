package crazypants.enderio.machine.painter;

import crazypants.enderio.EnderIO;
import crazypants.enderio.compat.AtlasResolver;
import crazypants.enderio.compat.TextureAnimatedAtlasFX;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import crazypants.enderio.GuiHandler;
import crazypants.enderio.ModObject;
import crazypants.enderio.machine.AbstractMachineBlock;
import crazypants.enderio.machine.AbstractMachineEntity;

public class BlockPainter extends AbstractMachineBlock<TileEntityPainter> {

  public static final String KEY_SOURCE_BLOCK_ID = "sourceBlockId";
  public static final String KEY_SOURCE_BLOCK_META = "sourceBlockMeta";

  public static BlockPainter create() {
    BlockPainter ppainter = new BlockPainter();
    ppainter.init();
    ppainter.initP();
    return ppainter;
  }

  private BlockPainter() {
    super(ModObject.blockPainter, TileEntityPainter.class);
  }

  private void initP() {
    int[] anim = new int[]{0, 1, 2, 3, 4, 5};
    for (int i = 0; i < anim.length; i++)
      anim[i] = AtlasResolver.getLocationIndex("enderio:painterFrontOn" + anim[i]);
    Minecraft.getMinecraft().renderEngine.registerTextureFX(new TextureAnimatedAtlasFX(
            7, anim[0], EnderIO.ATLAS_RESOLVER.getTextureFile(), anim));
  }

  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    // The server needs the container as it manages the adding and removing of
    // items, which are then sent to the client for display
    TileEntity te = world.getBlockTileEntity(x, y, z);
    if (te instanceof TileEntityPainter) {
      return new PainterContainer(player.inventory, (AbstractMachineEntity) te);
    }
    return null;
  }

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    TileEntity te = world.getBlockTileEntity(x, y, z);
    return new GuiPainter(player.inventory, (AbstractMachineEntity) te);
  }

  @Override
  protected int getGuiId() {
    return GuiHandler.GUI_ID_PAINTER;
  }

  @Override
  protected String getMachineFrontIconKey(boolean active) {
    if (active) {
      return "enderio:painterFrontOn0";
    }
    return "enderio:painterFrontOff";
  }

}
