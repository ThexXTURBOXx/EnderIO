package crazypants.enderio.machine.generator;

import crazypants.enderio.EnderIO;
import crazypants.enderio.compat.AtlasResolver;
import crazypants.enderio.compat.TextureAnimatedAtlasFX;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import crazypants.enderio.GuiHandler;
import crazypants.enderio.ModObject;
import crazypants.enderio.machine.AbstractMachineBlock;

public class BlockStirlingGenerator extends AbstractMachineBlock<TileEntityStirlingGenerator> {

  public static BlockStirlingGenerator create() {
    BlockStirlingGenerator gen = new BlockStirlingGenerator();
    gen.init();
    gen.initS();
    return gen;
  }

  protected BlockStirlingGenerator() {
    super(ModObject.blockStirlingGenerator, TileEntityStirlingGenerator.class);
  }

  private void initS() {
    int[] anim = new int[]{0, 1, 1, 1, 1, 1};
    for (int i = 0; i < anim.length; i++)
      anim[i] = AtlasResolver.getLocationIndex("enderio:stirlingGenFrontOn" + anim[i]);
    Minecraft.getMinecraft().renderEngine.registerTextureFX(new TextureAnimatedAtlasFX(
            2, anim[0], EnderIO.ATLAS_RESOLVER.getTextureFile(), anim));
  }

  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    return new StirlingGeneratorContainer(player.inventory, (TileEntityStirlingGenerator) world.getBlockTileEntity(x, y, z));
  }

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    return new GuiStirlingGenerator(player.inventory, (TileEntityStirlingGenerator) world.getBlockTileEntity(x, y, z));
  }

  @Override
  protected int getGuiId() {
    return GuiHandler.GUI_ID_STIRLING_GEN;
  }

  @Override
  protected String getMachineFrontIconKey(boolean active) {
    if (active) {
      return "enderio:stirlingGenFrontOn0";
    }
    return "enderio:stirlingGenFrontOff";
  }

  @Override
  protected String getSideIconKey() {
    return "enderio:stirlingGenSide";
  }

}
