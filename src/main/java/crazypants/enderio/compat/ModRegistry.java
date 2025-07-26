package crazypants.enderio.compat;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public abstract class ModRegistry {

    private static final Map<String, ModRegistry> REGISTRIES = new HashMap<String, ModRegistry>();

    protected final String domain;

    public ModRegistry(String domain) {
        this.domain = domain.toLowerCase();
        REGISTRIES.put(this.domain, this);
    }

    public abstract Item resolveItem(String path);

    public abstract Block resolveBlock(String path);

    public static ModRegistry getModRegistry(String domain) {
        return REGISTRIES.get(domain.toLowerCase());
    }

    public static Item findItem(String domain, String path) {
        ModRegistry reg = getModRegistry(domain);
        return reg == null ? null : reg.resolveItem(path);
    }

    public static Block findBlock(String domain, String path) {
        ModRegistry reg = getModRegistry(domain);
        return reg == null ? null : reg.resolveBlock(path);
    }

    public static Item findItem(String location) {
        String[] parsed = parseLocation(location);
        return findItem(parsed[0], parsed[1]);
    }

    public static Block findBlock(String location) {
        String[] parsed = parseLocation(location);
        return findBlock(parsed[0], parsed[1]);
    }

    public static String[] parseLocation(String location) {
        String[] split = location.split(":", 2);
        if (split.length == 0) {
            return new String[]{"minecraft", "air"};
        } else if (split.length == 1) {
            return new String[]{"minecraft", split[0]};
        } else {
            return split;
        }
    }

    public static class VanillaRegistry extends ModRegistry {

        public static final ModRegistry VANILLA_REGISTRY = new ModRegistry.VanillaRegistry();

        private final Map<String, Field> itemFields = new HashMap<String, Field>();
        private final Map<String, Field> blockFields = new HashMap<String, Field>();

        public VanillaRegistry() {
            super("minecraft");

            for (Field field : Item.class.getFields()) {
                try {
                    field.setAccessible(true);
                    if (Modifier.isStatic(field.getModifiers()) && field.getType() == Item.class) {
                        Item item = (Item) field.get(null);
                        itemFields.put(item.getItemName().replaceFirst("^item\\.", ""), field);
                    }
                } catch (Throwable ignored) {
                }
            }

            for (Field field : Block.class.getFields()) {
                try {
                    field.setAccessible(true);
                    if (Modifier.isStatic(field.getModifiers()) && field.getType() == Block.class) {
                        Block block = (Block) field.get(null);
                        blockFields.put(block.getBlockName().replaceFirst("^tile\\.", ""), field);
                    }
                } catch (Throwable ignored) {
                }
            }
        }

        @Override
        public Item resolveItem(String path) {
            try {
                return (Item) itemFields.get(path).get(null);
            } catch (Throwable ignored) {
            }
            return null;
        }

        @Override
        public Block resolveBlock(String path) {
            try {
                return (Block) blockFields.get(path).get(null);
            } catch (Throwable ignored) {
            }
            return null;
        }

        public static void poke() {
        }
    }

    public static void poke() {
        VanillaRegistry.poke();
    }

}
