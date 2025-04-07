package io.github.vampirestudios.craftmineplus.mixin.unvanillify;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.mines.WorldEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WorldEffect.Builder.class)
public interface WorldEffectBuilderAccessor {
    @Invoker("withItemModel")
    WorldEffect.Builder craftMinePlus$withItemModel(ResourceLocation resourceLocation);
}
