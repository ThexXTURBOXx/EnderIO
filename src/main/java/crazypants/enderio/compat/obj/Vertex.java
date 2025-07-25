package crazypants.enderio.compat.obj;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Vertex {
    public float x;
    public float y;
    public float z;

    public Vertex(float x, float y) {
        this(x, y, 0.0F);
    }

    public Vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
