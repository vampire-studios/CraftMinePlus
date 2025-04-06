package io.github.vampirestudios.craftmineplus.pond;

import io.github.vampirestudios.craftmineplus.mixin.unvanillify.WorldEffectBuilderAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.mines.WorldEffect;

public interface WorldEffectDuck {
    static WorldEffect.Builder namespacedBuilder(ResourceLocation resourceLocation) {
        return new WorldEffect.Builder(resourceLocation.toString()).craftMinePlus$namespaced();
    }

    interface BuilderDuck {
        default WorldEffect.Builder craftMinePlus$namespaced() {
            throw new AssertionError("Implemented Via Mixin");
        }

        default WorldEffect.Builder craftMinePlus$withCustomIcon(ResourceLocation resourceLocation) {
            return ((WorldEffectBuilderAccessor) this).craftMinePlus$withItemModel(CustomIconsDuck.registerNamespaced(resourceLocation));
        }
    }
}
