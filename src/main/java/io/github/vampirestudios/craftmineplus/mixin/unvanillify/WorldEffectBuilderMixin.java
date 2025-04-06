package io.github.vampirestudios.craftmineplus.mixin.unvanillify;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.vampirestudios.craftmineplus.pond.WorldEffectDuck;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.mines.WorldEffect;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.List;
import java.util.function.Consumer;

@Mixin(WorldEffect.Builder.class)
public abstract class WorldEffectBuilderMixin implements WorldEffectDuck.BuilderDuck {
    @Shadow @Final
    private List<Consumer<ServerLevel>> onMineTick;

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

    /**
     * @author Olivia
     * @reason a
     */
    @Overwrite
    public WorldEffect.Builder multiplayerOnly() {
        return (WorldEffect.Builder) (Object) this;
    }

    @WrapOperation(
            method = "build",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/mines/WorldEffect$Builder;onMineEnter:Ljava/util/List;",
                    ordinal = 2
            )
    )
    private List<Consumer<ServerLevel>> build(WorldEffect.Builder instance, Operation<List<Consumer<ServerLevel>>> original) {
        return onMineTick;
    }
}
