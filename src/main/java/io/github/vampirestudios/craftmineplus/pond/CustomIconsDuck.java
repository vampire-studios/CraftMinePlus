package io.github.vampirestudios.craftmineplus.pond;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.mines.CustomIcons;

public class CustomIconsDuck {
    static ResourceLocation register(ResourceLocation resourceLocation) {
        CustomIcons.ICONS.add(resourceLocation);
        return resourceLocation;
    }
}
