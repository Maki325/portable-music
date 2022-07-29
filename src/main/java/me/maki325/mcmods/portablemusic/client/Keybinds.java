package me.maki325.mcmods.portablemusic.client;

import me.maki325.mcmods.portablemusic.PortableMusic;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class Keybinds {

    public static List<KeyMapping> keyMappings = new ArrayList<>();

    public static KeyMapping openBoombox = registerKey("open_boombox", "portablemusic", GLFW.GLFW_KEY_B);

    private static KeyMapping registerKey(String name, String category, int keycode) {
        var key = new KeyMapping("key." + PortableMusic.MODID + "." + name, keycode, "key.categories." + category);
        keyMappings.add(key);
        return key;
    }

}
