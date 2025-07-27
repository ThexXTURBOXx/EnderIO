package crazypants.enderio.compat;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.ForgeHooksClient;

public class DynTexBlockInWorldRenderer implements ISimpleBlockRenderingHandler {

    public static final int ID = RenderingRegistry.getNextAvailableRenderId();
    public static final DynTexBlockInWorldRenderer INSTANCE = new DynTexBlockInWorldRenderer();

    public static void init() {
        RenderingRegistry.registerBlockHandler(ID, INSTANCE);
    }

    @Override
    public void renderInventoryBlock(Block block, int x, int y, RenderBlocks renderBlocks) {
        // Assumed to be done by dedicated item renderer
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess iBlockAccess, int x, int y, int z, Block block, int side,
                                    RenderBlocks renderBlocks) {
        if (!(block instanceof IDynTexBlock)) return false;

        IDynTexBlock b = (IDynTexBlock) block;
        ForgeHooksClient.bindTexture(b.getTextureFile(iBlockAccess, x, y, z, side), 0);
        b.setOriginalRenderType();
        renderBlocks.renderBlockByRenderType(block, x, y, z);
        b.setDynRenderType();
        ForgeHooksClient.unbindTexture();

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory() {
        return false;
    }

    @Override
    public int getRenderId() {
        return ID;
    }

}
