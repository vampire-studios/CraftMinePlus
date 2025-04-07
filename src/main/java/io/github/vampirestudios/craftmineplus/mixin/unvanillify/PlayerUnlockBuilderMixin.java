package io.github.vampirestudios.craftmineplus.mixin.unvanillify;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.vampirestudios.craftmineplus.pond.PlayerUnlockDuck;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.players.PlayerUnlock;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(PlayerUnlock.Builder.class)
public class PlayerUnlockBuilderMixin implements PlayerUnlockDuck.BuilderDuck {
    @Shadow
    @Final
    private String key;
    @Shadow
    @Final
    private DisplayInfo.Builder display;
    @Unique
    private boolean namespaced = false;

    @WrapOperation(
            method = "method_69220",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/resources/ResourceLocation;withDefaultNamespace(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;"
            )
    )
    private ResourceLocation addModNamespacesToAttributeModifiers(String string, Operation<ResourceLocation> original) {
        if (this.namespaced) {
            return Objects.requireNonNull(ResourceLocation.bySeparator(this.key, ':'), "invalid resource location").withPrefix("unlock_");
        }
        return original.call(string);
    }

    @WrapOperation(
            method = "register",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/resources/ResourceLocation;withDefaultNamespace(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;"
            )
    )
    private ResourceLocation addModNamespacesToRegistration(String string, Operation<ResourceLocation> original) {
        if (this.namespaced) {
            return Objects.requireNonNull(ResourceLocation.bySeparator(this.key, ':'), "invalid resource location");
        }
        return original.call(string);
    }

    @Override
    public PlayerUnlock.Builder craftMinePlus$namespaced() {
        this.namespaced = true;
        var displayAccessor = (DisplayInfo.Builder & DisplayInfoBuilderAccessor) this.display;
        var description = displayAccessor.craftmine_plus$description();
        if (description != null && description.getContents() instanceof TranslatableContents translatableContents && translatableContents.getKey().contains(this.key)) {
            displayAccessor.withDescription(MutableComponent.create(new TranslatableContents(translatableContents.getKey().replace(":", "_"), translatableContents.getFallback(), translatableContents.getArgs())));
        }
        var title = displayAccessor.craftmine_plus$title();
        if (title != null && title.getContents() instanceof TranslatableContents translatableContents && translatableContents.getKey().contains(this.key)) {
            displayAccessor.withTitle(MutableComponent.create(new TranslatableContents(translatableContents.getKey().replace(":", "_"), translatableContents.getFallback(), translatableContents.getArgs())));
        }
        var hint = displayAccessor.craftmine_plus$hint();
        if (hint != null && hint.getContents() instanceof TranslatableContents translatableContents && translatableContents.getKey().contains(this.key)) {
            displayAccessor.withHint(MutableComponent.create(new TranslatableContents(translatableContents.getKey().replace(":", "_"), translatableContents.getFallback(), translatableContents.getArgs())));
        }

        return (PlayerUnlock.Builder) (Object) this;
    }
}
