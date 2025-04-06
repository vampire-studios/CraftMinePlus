package io.github.vampirestudios.craftmineplus.mixin.unvanillify;

import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DisplayInfo.Builder.class)
public interface DisplayInfoBuilderAccessor {
    @Accessor("title")
    @Nullable
    Component craftmine_plus$title();

    @Accessor("description")
    @Nullable
    Component craftmine_plus$description();

    @Accessor("hint")
    @Nullable
    Component craftmine_plus$hint();
}
