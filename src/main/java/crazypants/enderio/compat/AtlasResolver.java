package crazypants.enderio.compat;

import cpw.mods.fml.common.FMLCommonHandler;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.MinecraftForgeClient;

public class AtlasResolver {

    private static final Map<String, AtlasResolver> domain2Resolver = new HashMap<String, AtlasResolver>();

    private final Map<String, Integer> img2Idx = new HashMap<String, Integer>();
    private final String domain;
    private final String textureFile;
    private final int atlasId;

    public AtlasResolver(String domain, String atlasFile, Class<?> resourceClass) {
        this.domain = domain;
        textureFile = atlasFile + ".png";

        if (FMLCommonHandler.instance().getSide().isClient()) {
            atlasId = Minecraft.getMinecraft().renderEngine.getTexture(atlasFile);

            MinecraftForgeClient.preloadTexture(textureFile);

            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(
                        new BufferedInputStream(resourceClass.getResourceAsStream(atlasFile + ".txt"))));

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] split = line.split(":\\s*");
                    img2Idx.put(split[1], Integer.parseInt(split[0]));
                }
            } catch (Throwable t) {
                throw new RuntimeException(t);
            } finally {
                try {
                    if (reader != null) reader.close();
                } catch (Throwable ignored) {
                }
            }
        } else {
            atlasId = -1;
        }

        domain2Resolver.put(domain, this);
    }

    public static AtlasResolver get(String domain) {
        return domain2Resolver.get(domain);
    }

    public static int getLocationIndex(String location) {
        String[] split = location.split(":", 2);
        return get(split[0]).getIndexInAtlas((split.length < 2 ? location : split[1]));
    }

    public static String getTextureFile(String location) {
        String[] split = location.split(":", 2);
        return get(split[0]).getTextureFile();
    }

    public String getDomain() {
        return domain;
    }

    public String getTextureFile() {
        return textureFile;
    }

    public int getAtlasId() {
        return atlasId;
    }

    public int getIndexInAtlas(String path) {
        Integer index = img2Idx.get(path);
        return index != null ? index : -1;
    }

}
