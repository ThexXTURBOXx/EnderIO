package crazypants.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

import crazypants.render.RenderUtil;

public class IconButton extends GuiButton {

  public static final int DEFAULT_WIDTH = 24;
  public static final int HWIDTH = DEFAULT_WIDTH / 2;
  public static final int DEFAULT_HEIGHT = 24;
  public static final int HHEIGHT = DEFAULT_HEIGHT / 2;

  protected int hwidth;
  protected int hheight;

  protected int icon;
  protected String texture;

  public IconButton(FontRenderer fr, int id, int x, int y, int icon, String texture) {
    super(id, x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, "");
    hwidth = HWIDTH;
    hheight = HHEIGHT;
    this.icon = icon;
    this.texture = texture;
  }

  public void setSize(int width, int height) {
    this.width = width;
    this.height = height;
    hwidth = width / 2;
    hheight = height / 2;
  }

  public int getIcon() {
    return icon;
  }

  public void setIcon(int icon) {
    this.icon = icon;
  }

  public String getTexture() {
    return texture;
  }

  public void setTexture(String textureName) {
    this.texture = textureName;
  }

  /**
   * Draws this button to the screen.
   */
  @SuppressWarnings("synthetic-access")
  @Override
  public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
    if (drawButton) {

      RenderUtil.bindTexture("/gui/gui.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_82253_i = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + width
          && par3 < this.yPosition + height;
      int hoverState = getHoverState(this.field_82253_i);

      // x, y, u, v, width, height

      // top half
      drawTexturedModalRect(xPosition, yPosition, 0, 46 + hoverState * 20, hwidth, hheight);
      drawTexturedModalRect(xPosition + hwidth, yPosition, 200 - hwidth, 46 + hoverState * 20, hwidth, hheight);

      // bottom half
      drawTexturedModalRect(xPosition, yPosition + hheight, 0, 66 - hheight + (hoverState * 20), hwidth, hheight);
      drawTexturedModalRect(xPosition + hwidth, yPosition + hheight, 200 - hwidth, 66 - hheight + (hoverState * 20), hwidth, hheight);

      mouseDragged(par1Minecraft, par2, par3);

      GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

      RenderUtil.bindTexture(texture);
      int xLoc = xPosition + 2;
      int yLoc = yPosition + 2;
      drawStretchedTexturedModalRect(xLoc, yLoc, width - 4, height - 4, (icon % 16) * 16, (icon / 16) * 16, 16, 16);

      GL11.glPopAttrib();

    }
  }

  public void drawStretchedTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6, int par7, int par8) {
    float var7 = 0.00390625F;
    float var8 = 0.00390625F;
    Tessellator var9 = Tessellator.instance;
    var9.startDrawingQuads();
    var9.addVertexWithUV(par1 + 0, par2 + par4, this.zLevel, (par5 + 0) * var7, (par6 + par8) * var8);
    var9.addVertexWithUV(par1 + par3, par2 + par4, this.zLevel, (par5 + par7) * var7, (par6 + par8) * var8);
    var9.addVertexWithUV(par1 + par3, par2 + 0, this.zLevel, (par5 + par7) * var7, (par6 + 0) * var8);
    var9.addVertexWithUV(par1 + 0, par2 + 0, this.zLevel, (par5 + 0) * var7, (par6 + 0) * var8);
    var9.draw();
  }
}
