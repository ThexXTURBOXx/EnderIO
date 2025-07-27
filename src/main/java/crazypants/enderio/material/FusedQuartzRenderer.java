package crazypants.enderio.material;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import crazypants.enderio.EnderIO;
import crazypants.enderio.machine.painter.PainterUtil;
import crazypants.enderio.machine.painter.TileEntityCustomBlock;
import crazypants.render.RenderUtil;

public class FusedQuartzRenderer implements ISimpleBlockRenderingHandler {

  public static final int ID = RenderingRegistry.getNextAvailableRenderId();
  public static final FusedQuartzRenderer INSTANCE = new FusedQuartzRenderer();

  public static void init() {
    RenderingRegistry.registerBlockHandler(ID, INSTANCE);
  }

  static int renderPass;

  @Override
  public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
    renderer.setOverrideBlockTexture(EnderIO.blockFusedQuartz.getBlockTextureFromSideAndMetadata(0, 0));
    renderer.renderBlockAsItem(Block.glass, 0, 1);
    renderer.clearOverrideBlockTexture();
  }

  @Override
  public boolean shouldRender3DInInventory() {
    return true;
  }

  @Override
  public int getRenderId() {
    return ID;
  }

  @Override
  public boolean renderWorldBlock(IBlockAccess blockAccess, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
    if (renderPass == 0) {
      RenderUtil.setTesselatorBrightness(blockAccess, x, y, z);
      TileEntityCustomBlock tecb = null;
      TileEntity te = blockAccess.getBlockTileEntity(x, y, z);
      if (te instanceof TileEntityCustomBlock) {
        tecb = (TileEntityCustomBlock) te;
      }
      renderFrame(blockAccess, x, y, z, tecb, false);
    } else {
      renderer.setOverrideBlockTexture(EnderIO.blockFusedQuartz.realBlockIcon);
      renderer.renderStandardBlock(block, x, y, z);
      renderer.clearOverrideBlockTexture();
    }
    return true;
  }

  public void renderFrameItem(ItemStack stack) {
    RenderUtil.bindBlockTexture();
    Tessellator.instance.startDrawingQuads();
    TileEntityCustomBlock tecb = new TileEntityCustomBlock();
    tecb.setSourceBlockId(PainterUtil.getSourceBlockId(stack));
    tecb.setSourceBlockMetadata(PainterUtil.getSourceBlockMetadata(stack));
    renderFrame(null, 0, 0, 0, tecb, true);
    Tessellator.instance.draw();
  }

  private void renderFrame(IBlockAccess blockAccess, int x, int y, int z, TileEntityCustomBlock tecb, boolean forceAllEdges) {
    String textureFile = EnderIO.blockFusedQuartz.getTextureFile();
    if (tecb != null && tecb.getSourceBlockId() > 0) {
      textureFile = tecb.getSourceBlock().getTextureFile();
    }

    int texture = EnderIO.blockFusedQuartz.getBlockTextureFromSideAndMetadata(0, 0);
    ForgeHooksClient.bindTexture(textureFile, 0);
    for (ForgeDirection face : ForgeDirection.VALID_DIRECTIONS) {
      if (tecb != null && tecb.getSourceBlockId() > 0) {
        texture = tecb.getSourceBlock().getBlockTextureFromSideAndMetadata(face.ordinal(), tecb.getSourceBlockMetadata());
      }
      RenderUtil.renderConnectedTextureFace(blockAccess, x, y, z, face, texture, forceAllEdges);
    }
    ForgeHooksClient.unbindTexture();
  }

}
