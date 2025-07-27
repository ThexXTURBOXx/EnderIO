package crazypants.enderio.compat;

import crazypants.enderio.EnderIO;
import crazypants.enderio.conduit.BlockConduitBundle;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.util.Facing;
import net.minecraft.world.World;

public class RedstoneCompat {

    private static final List<IRedstoneStrengthProvider> PROVIDERS = new ArrayList<IRedstoneStrengthProvider>();

    static {
        registerProvider(new IRedstoneStrengthProvider() {
            @Override
            public Integer getStrongPower(World world, int x, int y, int z, int side) {
                int id = world.getBlockId(x, y, z);
                if (id != Block.redstoneWire.blockID) return null;
                return Block.blocksList[id].isProvidingStrongPower(world, x, y, z, side)
                        ? world.getBlockMetadata(x, y, z) : 0;
            }

            @Override
            public Integer getWeakPower(World world, int x, int y, int z, int side) {
                int id = world.getBlockId(x, y, z);
                if (id != Block.redstoneWire.blockID) return null;
                return Block.blocksList[id].isProvidingWeakPower(world, x, y, z, side)
                        ? world.getBlockMetadata(x, y, z) : 0;
            }
        });

        registerProvider(new IRedstoneStrengthProvider() {
            @Override
            public Integer getStrongPower(World world, int x, int y, int z, int side) {
                int id = world.getBlockId(x, y, z);
                if (id != EnderIO.blockConduitBundle.blockID) return null;
                return BlockConduitBundle.getStrongPower(world, x, y, z, side);
            }

            @Override
            public Integer getWeakPower(World world, int x, int y, int z, int side) {
                int id = world.getBlockId(x, y, z);
                if (id != EnderIO.blockConduitBundle.blockID) return null;
                return BlockConduitBundle.getWeakPower(world, x, y, z, side);
            }
        });
    }

    public static void registerProvider(IRedstoneStrengthProvider provider) {
        PROVIDERS.add(provider);
    }

    public static int isProvidingStrongPower(World world, int x, int y, int z, int side) {
        for (IRedstoneStrengthProvider provider : PROVIDERS) {
            Integer sp = provider.getStrongPower(world, x, y, z, side);
            if (sp != null) return sp;
        }
        int id = world.getBlockId(x, y, z);
        return id == 0 ? 0 : (Block.blocksList[id].isProvidingStrongPower(world, x, y, z, side) ? 15 : 0);
    }

    public static int isProvidingWeakPower(World world, int x, int y, int z, int side) {
        for (IRedstoneStrengthProvider provider : PROVIDERS) {
            Integer sp = provider.getWeakPower(world, x, y, z, side);
            if (sp != null) return sp;
        }
        int id = world.getBlockId(x, y, z);
        return id == 0 ? 0 : (Block.blocksList[id].isProvidingWeakPower(world, x, y, z, side) ? 15 : 0);
    }

    public static int isBlockProvidingPowerTo(World w, int x, int y, int z, int side) {
        return w.getBlockId(x, y, z) == 0 ? 0 : isProvidingStrongPower(w, x, y, z, side);
    }

    public static int getIndirectPowerLevelTo(World w, int x, int y, int z, int side) {
        if (w.isBlockNormalCube(x, y, z)) {
            return getBlockPowerInput(w, x, y, z);
        } else {
            return w.getBlockId(x, y, z) == 0 ? 0 : isProvidingWeakPower(w, x, y, z, side);
        }
    }

    public static int getBlockPowerInput(World w, int x, int y, int z) {
        byte b0 = 0;
        int l = Math.max(b0, isBlockProvidingPowerTo(w, x, y - 1, z, 0));
        if (l >= 15) {
            return l;
        } else {
            l = Math.max(l, isBlockProvidingPowerTo(w, x, y + 1, z, 1));
            if (l >= 15) {
                return l;
            } else {
                l = Math.max(l, isBlockProvidingPowerTo(w, x, y, z - 1, 2));
                if (l >= 15) {
                    return l;
                } else {
                    l = Math.max(l, isBlockProvidingPowerTo(w, x, y, z + 1, 3));
                    if (l >= 15) {
                        return l;
                    } else {
                        l = Math.max(l, isBlockProvidingPowerTo(w, x - 1, y, z, 4));
                        if (l >= 15) {
                            return l;
                        } else {
                            l = Math.max(l, isBlockProvidingPowerTo(w, x + 1, y, z, 5));
                            return l >= 15 ? l : l;
                        }
                    }
                }
            }
        }
    }

    public static int getStrongestIndirectPower(World w, int x, int y, int z) {
        int l = 0;

        for (int i1 = 0; i1 < 6; i1++) {
            int j1 = getIndirectPowerLevelTo(w, x + Facing.offsetsXForSide[i1], y + Facing.offsetsYForSide[i1],
                    z + Facing.offsetsZForSide[i1], i1);
            if (j1 >= 15) {
                return 15;
            }

            if (j1 > l) {
                l = j1;
            }
        }

        return l;
    }

}
