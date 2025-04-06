package io.github.vampirestudios.craftmineplus.pond;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.mines.SpecialMine;

public interface SpecialMineDuck {
    static SpecialMine.Builder namespacedBuilder(ResourceLocation resourceLocation) {
        return new SpecialMine.Builder(resourceLocation.toString())
                .craftMinePlus$namespaced();
    }

    interface BuilderDuck {
        default SpecialMine.Builder craftMinePlus$namespaced() {
            throw new AssertionError("Implemented Via Mixin");
        }
    }
}
