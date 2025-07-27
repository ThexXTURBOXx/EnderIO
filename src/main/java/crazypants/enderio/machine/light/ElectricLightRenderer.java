package crazypants.enderio.machine.light;

import cpw.mods.fml.client.registry.RenderingRegistry;
import java.util.Arrays;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import crazypants.render.BoundingBox;
import crazypants.render.CubeRenderer;
import crazypants.render.RenderUtil;

public class ElectricLightRenderer implements ISimpleBlockRenderingHandler {

  public static final int ID = RenderingRegistry.getNextAvailableRenderId();
  public static final ElectricLightRenderer INSTANCE = new ElectricLightRenderer();

  public static void init() {
    RenderingRegistry.registerBlockHandler(ID, INSTANCE);
  }

  @Override
  public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {

    BoundingBox bb = new BoundingBox(0, 0, 0, 1, 0.2, 1);
    boolean doDraw = false;
    if (!Tessellator.instance.isDrawing) {
      doDraw = true;
      Tessellator.instance.startDrawingQuads();
    }
    String[] files = new String[6];
    Arrays.fill(files, block.getTextureFile());
    int[] textures = new int[6];
    textures[0] = block.getBlockTextureFromSide(ForgeDirection.NORTH.ordinal());
    textures[1] = block.getBlockTextureFromSide(ForgeDirection.SOUTH.ordinal());
    textures[2] = block.getBlockTextureFromSide(ForgeDirection.DOWN.ordinal());
    textures[3] = block.getBlockTextureFromSide(ForgeDirection.UP.ordinal());
    textures[4] = block.getBlockTextureFromSide(ForgeDirection.WEST.ordinal());
    textures[5] = block.getBlockTextureFromSide(ForgeDirection.EAST.ordinal());

    CubeRenderer.render(bb, files, textures, null);

    if (doDraw) {
      Tessellator.instance.draw();
    }
  }

  @Override
  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
    block.setBlockBoundsBasedOnState(world, x, y, z);
    BoundingBox bb = new BoundingBox(block.getBlockBoundsMinX(), block.getBlockBoundsMinY(), block.getBlockBoundsMinZ(), block.getBlockBoundsMaxX(),
        block.getBlockBoundsMaxY(), block.getBlockBoundsMaxZ());

    bb = bb.translate(x, y, z);
    RenderUtil.setTesselatorBrightness(world, x, y, z);

    String[] files = new String[6];
    Arrays.fill(files, block.getTextureFile());
    int[] textures = new int[6];
    textures[0] = block.getBlockTexture(world, x, y, z, ForgeDirection.NORTH.ordinal());
    textures[1] = block.getBlockTexture(world, x, y, z, ForgeDirection.SOUTH.ordinal());
    textures[2] = block.getBlockTexture(world, x, y, z, ForgeDirection.UP.ordinal());
    textures[3] = block.getBlockTexture(world, x, y, z, ForgeDirection.DOWN.ordinal());
    textures[4] = block.getBlockTexture(world, x, y, z, ForgeDirection.WEST.ordinal());
    textures[5] = block.getBlockTexture(world, x, y, z, ForgeDirection.EAST.ordinal());

    CubeRenderer.render(bb, files, textures, null);

    return true;
  }

  @Override
  public boolean shouldRender3DInInventory() {
    return true;
  }

  @Override
  public int getRenderId() {
    return ID;
  }

}
