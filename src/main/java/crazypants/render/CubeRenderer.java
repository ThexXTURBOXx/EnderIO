package crazypants.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.common.ForgeDirection;
import crazypants.vecmath.Vector3d;

public final class CubeRenderer {

  public static final Vector3d[] verts = new Vector3d[8];
  static {
    for (int i = 0; i < verts.length; i++) {
      verts[i] = new Vector3d();
    }
  }

  public static void bind(String textureFile) {
    Minecraft.getMinecraft().renderEngine.bindTexture(
            Minecraft.getMinecraft().renderEngine.getTexture(textureFile));
  }

  public static void render(BoundingBox bb, int index) {
    render(bb, index, null, false);
  }

  public static void render(BoundingBox bb, int index, boolean tintSides) {
    render(bb, index, null, tintSides);
  }

  public static void render(BoundingBox bb, int index, VertexTransform xForm) {
    float minU = (index % 16 * 16 + 0) / 256.0F;
    float minV = (index % 16 * 16 + 16) / 256.0F;
    float maxU = (index / 16 * 16 + 0) / 256.0F;
    float maxV = (index / 16 * 16 + 16) / 256.0F;
    render(bb, minU, maxU, minV, maxV, xForm, false);
  }

  public static void render(BoundingBox bb, int index, VertexTransform xForm, boolean tintSides) {
    float minU = 0;
    float minV = 0;
    float maxU = 1;
    float maxV = 1;
    if (index > 0) {
      minU = (index % 16 * 16 + 0) / 256.0F;
      minV = (index % 16 * 16 + 16) / 256.0F;
      maxU = (index / 16 * 16 + 0) / 256.0F;
      maxV = (index / 16 * 16 + 16) / 256.0F;
    }
    render(bb, minU, maxU, minV, maxV, xForm, tintSides);
  }

  public static void render(BoundingBox bb, float minU, float maxU, float minV, float maxV, boolean tintSides) {
    render(bb, minU, maxU, minV, maxV, null, tintSides);
  }

  public static void render(BoundingBox bb, float minU, float maxU, float minV, float maxV) {
    render(bb, minU, maxU, minV, maxV, null, false);
  }

  public static void render(BoundingBox bb, float minU, float maxU, float minV, float maxV, VertexTransform xForm) {
    render(bb, minU, maxU, minV, maxV, xForm, false);
  }

  public static void render(BoundingBox bb, float minU, float maxU, float minV, float maxV, VertexTransform xForm, boolean tintSides) {
    setupVertices(bb, xForm);

    float tmp = minV;
    minV = maxV;
    maxV = tmp;

    Tessellator tessellator = Tessellator.instance;

    tessellator.setNormal(0, 0, -1);
    if (tintSides) {
      float cm = RenderUtil.getColorMultiplierForFace(ForgeDirection.NORTH);
      tessellator.setColorOpaque_F(cm, cm, cm);
    }
    addVecWithUV(verts[1], minU, minV);
    addVecWithUV(verts[0], maxU, minV);
    addVecWithUV(verts[3], maxU, maxV);
    addVecWithUV(verts[2], minU, maxV);

    tessellator.setNormal(0, 0, 1);
    if (tintSides) {
      float cm = RenderUtil.getColorMultiplierForFace(ForgeDirection.SOUTH);
      tessellator.setColorOpaque_F(cm, cm, cm);
    }
    addVecWithUV(verts[4], minU, minV);
    addVecWithUV(verts[5], maxU, minV);
    addVecWithUV(verts[6], maxU, maxV);
    addVecWithUV(verts[7], minU, maxV);

    tessellator.setNormal(0, 1, 0);
    if (tintSides) {
      float cm = RenderUtil.getColorMultiplierForFace(ForgeDirection.UP);
      tessellator.setColorOpaque_F(cm, cm, cm);
    }
    addVecWithUV(verts[6], minU, minV);
    addVecWithUV(verts[2], minU, maxV);
    addVecWithUV(verts[3], maxU, maxV);
    addVecWithUV(verts[7], maxU, minV);

    tessellator.setNormal(0, -1, 0);
    if (tintSides) {
      float cm = RenderUtil.getColorMultiplierForFace(ForgeDirection.DOWN);
      tessellator.setColorOpaque_F(cm, cm, cm);
    }
    addVecWithUV(verts[0], maxU, maxV);
    addVecWithUV(verts[1], minU, maxV);
    addVecWithUV(verts[5], minU, minV);
    addVecWithUV(verts[4], maxU, minV);

    tessellator.setNormal(1, 0, 0);
    if (tintSides) {
      float cm = RenderUtil.getColorMultiplierForFace(ForgeDirection.EAST);
      tessellator.setColorOpaque_F(cm, cm, cm);
    }
    addVecWithUV(verts[2], minU, maxV);
    addVecWithUV(verts[6], maxU, maxV);
    addVecWithUV(verts[5], maxU, minV);
    addVecWithUV(verts[1], minU, minV);

    tessellator.setNormal(-1, 0, 0);
    if (tintSides) {
      float cm = RenderUtil.getColorMultiplierForFace(ForgeDirection.WEST);
      tessellator.setColorOpaque_F(cm, cm, cm);
    }
    addVecWithUV(verts[0], minU, minV);
    addVecWithUV(verts[4], maxU, minV);
    addVecWithUV(verts[7], maxU, maxV);
    addVecWithUV(verts[3], minU, maxV);
  }

  public static void render(BoundingBox bb, String[] files, int[] indices, VertexTransform xForm) {
    setupVertices(bb, xForm);
    float minU;
    float maxU;
    float minV;
    float maxV;
    int index;

    Tessellator tessellator = Tessellator.instance;

    tessellator.setNormal(0, 0, -1);
    index = indices[0];
    minU = (index % 16 * 16 + 0) / 256.0F;
    minV = (index % 16 * 16 + 16) / 256.0F;
    maxU = (index / 16 * 16 + 0) / 256.0F;
    maxV = (index / 16 * 16 + 16) / 256.0F;
    bind(files[0]);
    addVecWithUV(verts[1], minU, minV);
    addVecWithUV(verts[0], maxU, minV);
    addVecWithUV(verts[3], maxU, maxV);
    addVecWithUV(verts[2], minU, maxV);

    tessellator.setNormal(0, 0, 1);
    index = indices[1];
    minU = (index % 16 * 16 + 0) / 256.0F;
    minV = (index % 16 * 16 + 16) / 256.0F;
    maxU = (index / 16 * 16 + 0) / 256.0F;
    maxV = (index / 16 * 16 + 16) / 256.0F;
    bind(files[1]);
    addVecWithUV(verts[4], minU, minV);
    addVecWithUV(verts[5], maxU, minV);
    addVecWithUV(verts[6], maxU, maxV);
    addVecWithUV(verts[7], minU, maxV);

    tessellator.setNormal(0, 1, 0);
    index = indices[2];
    minU = (index % 16 * 16 + 0) / 256.0F;
    minV = (index % 16 * 16 + 16) / 256.0F;
    maxU = (index / 16 * 16 + 0) / 256.0F;
    maxV = (index / 16 * 16 + 16) / 256.0F;
    bind(files[2]);
    addVecWithUV(verts[6], minU, minV);
    addVecWithUV(verts[2], minU, maxV);
    addVecWithUV(verts[3], maxU, maxV);
    addVecWithUV(verts[7], maxU, minV);

    tessellator.setNormal(0, -1, 0);
    index = indices[3];
    minU = (index % 16 * 16 + 0) / 256.0F;
    minV = (index % 16 * 16 + 16) / 256.0F;
    maxU = (index / 16 * 16 + 0) / 256.0F;
    maxV = (index / 16 * 16 + 16) / 256.0F;
    bind(files[3]);
    addVecWithUV(verts[0], maxU, maxV);
    addVecWithUV(verts[1], minU, maxV);
    addVecWithUV(verts[5], minU, minV);
    addVecWithUV(verts[4], maxU, minV);

    tessellator.setNormal(1, 0, 0);
    index = indices[4];
    minU = (index % 16 * 16 + 0) / 256.0F;
    minV = (index % 16 * 16 + 16) / 256.0F;
    maxU = (index / 16 * 16 + 0) / 256.0F;
    maxV = (index / 16 * 16 + 16) / 256.0F;
    bind(files[4]);
    addVecWithUV(verts[2], minU, maxV);
    addVecWithUV(verts[6], maxU, maxV);
    addVecWithUV(verts[5], maxU, minV);
    addVecWithUV(verts[1], minU, minV);

    tessellator.setNormal(-1, 0, 0);
    index = indices[5];
    minU = (index % 16 * 16 + 0) / 256.0F;
    minV = (index % 16 * 16 + 16) / 256.0F;
    maxU = (index / 16 * 16 + 0) / 256.0F;
    maxV = (index / 16 * 16 + 16) / 256.0F;
    bind(files[5]);
    addVecWithUV(verts[0], minU, minV);
    addVecWithUV(verts[4], maxU, minV);
    addVecWithUV(verts[7], maxU, maxV);
    addVecWithUV(verts[3], minU, maxV);
  }

  public static void setupVertices(BoundingBox bound) {
    setupVertices(bound, null);
  }

  public static void setupVertices(BoundingBox bound, VertexTransform xForm) {
    verts[0].set(bound.minX, bound.minY, bound.minZ);
    verts[1].set(bound.maxX, bound.minY, bound.minZ);
    verts[2].set(bound.maxX, bound.maxY, bound.minZ);
    verts[3].set(bound.minX, bound.maxY, bound.minZ);
    verts[4].set(bound.minX, bound.minY, bound.maxZ);
    verts[5].set(bound.maxX, bound.minY, bound.maxZ);
    verts[6].set(bound.maxX, bound.maxY, bound.maxZ);
    verts[7].set(bound.minX, bound.maxY, bound.maxZ);

    if (xForm != null) {
      for (Vector3d vec : verts) {
        xForm.apply(vec);
      }
    }
  }

  public static void addVecWithUV(Vector3d vec, double u, double v) {
    Tessellator.instance.addVertexWithUV(vec.x, vec.y, vec.z, u, v);
  }

  private CubeRenderer() {
  }

}
