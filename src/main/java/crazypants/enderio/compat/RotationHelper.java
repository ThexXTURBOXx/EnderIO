package crazypants.enderio.compat;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class RotationHelper {
    private static final ForgeDirection[] UP_DOWN_AXES = new ForgeDirection[]{ForgeDirection.UP, ForgeDirection.DOWN};

    public static ForgeDirection[] getValidVanillaBlockRotations(Block block) {
        return block instanceof BlockChest ? UP_DOWN_AXES : ForgeDirection.VALID_DIRECTIONS;
    }

    public static boolean rotateVanillaBlock(Block block, World worldObj, int x, int y, int z, ForgeDirection axis) {
        if (worldObj.isRemote) {
            return false;
        } else if (!(block instanceof BlockChest) || axis != ForgeDirection.UP && axis != ForgeDirection.DOWN) {
            return !(block instanceof BlockPistonBase) && !(block instanceof BlockDispenser)
                    ? false
                    : rotateBlock(worldObj, x, y, z, axis, 7);
        } else {
            return rotateBlock(worldObj, x, y, z, axis, 7);
        }
    }

    private static boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis, int mask) {
        int rotMeta = worldObj.getBlockMetadata(x, y, z);
        int masked = rotMeta & ~mask;
        ForgeDirection orientation = ForgeDirection.getOrientation(rotMeta & mask);
        ForgeDirection rotated = orientation.getRotation(axis);
        worldObj.setBlockMetadataWithNotify(x, y, z, rotated.ordinal() & mask | masked);
        return true;
    }
}
