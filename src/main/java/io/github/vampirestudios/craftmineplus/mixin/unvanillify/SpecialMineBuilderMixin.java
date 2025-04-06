package io.github.vampirestudios.craftmineplus.mixin.unvanillify;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.vampirestudios.craftmineplus.pond.SpecialMineDuck;
import net.minecraft.world.level.mines.SpecialMine;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Debug(export = true)
@Mixin(SpecialMine.Builder.class)
public class SpecialMineBuilderMixin implements SpecialMineDuck.BuilderDuck {
    @Unique
    private boolean namespaced = false;

    @Override
    public SpecialMine.Builder craftMinePlus$namespaced() {
        this.namespaced = true;
        return (SpecialMine.Builder) (Object) this;
    }

    @Definition(
            id = "translatable",
            method = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;"
    )
    @Definition(
            id = "key",
            field = "Lnet/minecraft/world/level/mines/SpecialMine$Builder;key:Ljava/lang/String;"
    )
    @Expression("translatable(? + @(this.key) + ?)")
    @ModifyExpressionValue(
            method = "build",
            at = @At(
                    value = "MIXINEXTRAS:EXPRESSION"
            )
    )
    private String fixTranslationKeys(String original) {
        if (namespaced)
            return original.replace(":", "_");
        return original;
    }
}
