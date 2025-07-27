package crazypants.enderio.compat;

import net.minecraft.world.World;

public interface IRedstoneStrengthProvider {

    Integer getStrongPower(World world, int x, int y, int z, int side);

    Integer getWeakPower(World world, int x, int y, int z, int side);

}
