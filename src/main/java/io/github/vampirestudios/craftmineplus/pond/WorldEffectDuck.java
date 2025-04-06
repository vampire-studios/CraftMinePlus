package io.github.vampirestudios.craftmineplus.pond;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.mines.WorldEffect;

public interface WorldEffectDuck {
    static WorldEffect.Builder builder(ResourceLocation resourceLocation) {
        return new WorldEffect.Builder(resourceLocation.toString()).craftMinePlus$markNamespaced();
    }

    interface BuilderDuck {
        WorldEffect.Builder craftMinePlus$withItemModel(ResourceLocation resourceLocation);

        default WorldEffect.Builder craftMinePlus$markNamespaced() {
            throw new AssertionError("Implemented Via Mixin");
        }

        default WorldEffect.Builder craftMinePlus$withCustomIcon(ResourceLocation resourceLocation) {
            return this.craftMinePlus$withItemModel(CustomIconsDuck.register(resourceLocation));
        }
    }
}
