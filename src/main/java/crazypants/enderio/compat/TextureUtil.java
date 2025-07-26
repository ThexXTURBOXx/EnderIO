package crazypants.enderio.compat;

public class TextureUtil {

    public static float getMinU(int index) {
        return (index % 16 * 16 + 0) / 256.0F;
    }

    public static float getMaxU(int index) {
        return (index % 16 * 16 + 16) / 256.0F;
    }

    public static float getMinV(int index) {
        return (index / 16 * 16 + 0) / 256.0F;
    }

    public static float getMaxV(int index) {
        return (index / 16 * 16 + 16) / 256.0F;
    }

}
