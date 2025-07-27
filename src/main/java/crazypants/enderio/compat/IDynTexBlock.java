package crazypants.enderio.compat;

import net.minecraft.world.IBlockAccess;

public interface IDynTexBlock {

    void setOriginalRenderType();

    void setDynRenderType();

    String getTextureFile(IBlockAccess world, int x, int y, int z, int side);

}
