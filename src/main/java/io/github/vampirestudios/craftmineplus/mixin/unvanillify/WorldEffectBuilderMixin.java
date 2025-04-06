package io.github.vampirestudios.craftmineplus.mixin.unvanillify;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.vampirestudios.craftmineplus.pond.WorldEffectDuck;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.mines.WorldEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldEffect.Builder.class)
public abstract class WorldEffectBuilderMixin implements WorldEffectDuck.BuilderDuck {
    @Shadow
    protected abstract WorldEffect.Builder withItemModel(ResourceLocation resourceLocation);

    @Unique
    private boolean namespaced = false;

    @Definition(id = "translatable", method = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;")
    @Definition(id = "key", field = "Lnet/minecraft/world/level/mines/WorldEffect$Builder;key:Ljava/lang/String;")
    @Expression("translatable(? + @(this.key) + ?)")
    @ModifyExpressionValue(
            method = "build",
            at = @At(
                    value = "MIXINEXTRAS:EXPRESSION"
            )
    )
    private String modifyTranslationKeyToRemoveColon(String key) {
        if (this.namespaced) {
            return key.replace(":", "_");
        }
        return key;
    }

    @WrapOperation(
            method = "register",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/resources/ResourceLocation;withDefaultNamespace(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;"
            )
    )
    private ResourceLocation registerUnderModNamespace(String string, Operation<ResourceLocation> original) {
        if (this.namespaced)
            return ResourceLocation.bySeparator(string, ':');
        return original.call(string);
    }

    @Override
    public WorldEffect.Builder craftMinePlus$namespaced() {
        this.namespaced = true;
        return (WorldEffect.Builder) (Object) this;
    }
}
