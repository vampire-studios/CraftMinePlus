package io.github.vampirestudios.craftmineplus.mixin.unvanillify;

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
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(WorldEffect.Builder.class)
public abstract class WorldEffectBuilderMixin implements WorldEffectDuck.BuilderDuck {
    @Shadow
    protected abstract WorldEffect.Builder withItemModel(ResourceLocation resourceLocation);

    @Unique
    private boolean namespaced = false;

    @ModifyExpressionValue(
            method = "build",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/mines/WorldEffect$Builder;key:Ljava/lang/String;"
            ),
            slice = @Slice(
                    to = @At(
                            value = "INVOKE:FIRST",
                            target = "Ljava/util/List;isEmpty()Z"
                    )
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
    public WorldEffect.Builder craftMinePlus$withItemModel(ResourceLocation resourceLocation) {
        return this.withItemModel(resourceLocation);
    }

    @Override
    public WorldEffect.Builder craftMinePlus$markNamespaced() {
        this.namespaced = true;
        return (WorldEffect.Builder) (Object) this;
    }
}
