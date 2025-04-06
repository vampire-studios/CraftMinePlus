package io.github.vampirestudios.craftmineplus.mixin.unvanillify;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.vampirestudios.craftmineplus.pond.SpecialMineDuck;
import net.minecraft.world.level.mines.SpecialMine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(SpecialMine.Builder.class)
public class SpecialMineBuilderMixin implements SpecialMineDuck.BuilderDuck {
    @Unique
    private boolean namespaced = false;

    @Override
    public SpecialMine.Builder craftMinePlus$namespaced() {
        this.namespaced = true;
        return (SpecialMine.Builder) (Object) this;
    }

    @ModifyExpressionValue(
            method = "build",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/mines/SpecialMine$Builder;key:Ljava/lang/String;"
            ),
            slice = @Slice(
                    to = @At(
                            value = "FIELD",
                            target = "Lnet/minecraft/world/level/mines/SpecialMine$Builder;key:Ljava/lang/String;",
                            ordinal = 1 // first two only
                    )
            )
    )
    private String fixTranslationKeys(String original) {
        if (namespaced)
            return original.replace(":", "_");
        return original;
    }
}
