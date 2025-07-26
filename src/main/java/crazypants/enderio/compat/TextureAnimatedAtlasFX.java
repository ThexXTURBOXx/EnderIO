package crazypants.enderio.compat;

import cpw.mods.fml.client.FMLTextureFX;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraftforge.client.ForgeHooksClient;

@SideOnly(Side.CLIENT)
public class TextureAnimatedAtlasFX extends FMLTextureFX {

    private final Minecraft mc = Minecraft.getMinecraft();

    private final String atlas;
    private final int[] allTextures;
    private final int speed;

    private Map<Integer, int[]> allImageData;
    private int subCounter = 0;
    private int idxCounter = 0;

    public TextureAnimatedAtlasFX(int baseTexture, String atlas, int... allTextures) {
        this(1, baseTexture, atlas, allTextures);
    }

    public TextureAnimatedAtlasFX(int speed, int baseTexture, String atlas, int... allTextures) {
        super(baseTexture);
        this.speed = speed;
        this.atlas = atlas;
        this.allTextures = allTextures;
        this.setup();
    }

    @Override
    public void bindImage(RenderEngine renderEngine) {
        ForgeHooksClient.bindTexture(atlas, 0);
    }

    @Override
    public void setup() {
        super.setup();

        this.allImageData = new HashMap<Integer, int[]>();

        try {
            BufferedImage img = ImageIO.read(
                    mc.texturePackList.getSelectedTexturePack().getResourceAsStream(atlas));

            for (int j = 0; j < allTextures.length; j++) {
                int idx = allTextures[j];

                int u = idx % 16 * tileSizeBase;
                int v = idx / 16 * tileSizeBase;

                int[] temp = new int[tileSizeSquare];
                img.getRGB(u, v, tileSizeBase, tileSizeBase, temp, 0, tileSizeBase);

                this.allImageData.put(idx, temp);
            }
        } catch (Throwable t) {
            t.printStackTrace();
            log.log(Level.WARNING, "Could not load animated EnderIO texture", t);
            setErrored(true);
        }
    }

    @Override
    public void onTick() {
        subCounter = (subCounter + 1) % speed;
        if (subCounter == 0) idxCounter = (idxCounter + 1) % allTextures.length;

        int[] cur = allImageData.get(allTextures[idxCounter]);
        for (int i = 0; i < this.tileSizeSquare; ++i) {
            imageData[i * 4 + 0] = (byte) ((cur[i] >> 16) & 0xFF);
            imageData[i * 4 + 1] = (byte) ((cur[i] >>  8) & 0xFF);
            imageData[i * 4 + 2] = (byte) ((cur[i] >>  0) & 0xFF);
            imageData[i * 4 + 3] = (byte) ((cur[i] >> 24) & 0xFF);
        }
    }

    public static void init() {
        int[] anim = new int[32];
        for (int i = 0; i < anim.length; i++)
            anim[i] = AtlasResolver.getLocationIndex("enderio:tesseractPortal" + i);
        Minecraft.getMinecraft().renderEngine.registerTextureFX(new TextureAnimatedAtlasFX(
                1, anim[0], AtlasResolver.getTextureFile("enderio"), anim));

        anim = new int[]{0, 1, 2, 3, 2, 1};
        for (int i = 0; i < anim.length; i++)
            anim[i] = AtlasResolver.getLocationIndex("enderio:alloySmelterFrontOn" + anim[i]);
        Minecraft.getMinecraft().renderEngine.registerTextureFX(new TextureAnimatedAtlasFX(
                2, anim[0], AtlasResolver.getTextureFile("enderio"), anim));

        anim = new int[]{0, 1, 1, 1, 1, 1};
        for (int i = 0; i < anim.length; i++)
            anim[i] = AtlasResolver.getLocationIndex("enderio:stirlingGenFrontOn" + anim[i]);
        Minecraft.getMinecraft().renderEngine.registerTextureFX(new TextureAnimatedAtlasFX(
                2, anim[0], AtlasResolver.getTextureFile("enderio"), anim));

        anim = new int[]{0, 1, 2, 3, 4, 5};
        for (int i = 0; i < anim.length; i++)
            anim[i] = AtlasResolver.getLocationIndex("enderio:painterFrontOn" + anim[i]);
        Minecraft.getMinecraft().renderEngine.registerTextureFX(new TextureAnimatedAtlasFX(
                7, anim[0], AtlasResolver.getTextureFile("enderio"), anim));
    }

}
