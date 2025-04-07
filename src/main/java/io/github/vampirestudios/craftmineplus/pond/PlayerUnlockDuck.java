package io.github.vampirestudios.craftmineplus.pond;

import net.minecraft.core.ClientAsset;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.players.PlayerUnlock;

import java.util.Optional;

public interface PlayerUnlockDuck {
    static PlayerUnlock.Builder namespacedChild(ResourceLocation rl, Holder<PlayerUnlock> parent) {
        return PlayerUnlock.child(rl.toString(), parent).craftMinePlus$namespaced();
    }

    static PlayerUnlock.Builder namespacedRoot(ResourceLocation rl) {
        return new PlayerUnlock.Builder(rl.toString(), Optional.empty(), Optional.of(new ClientAsset(rl.withPrefix("unlock_backgrounds/")))).craftMinePlus$namespaced();
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static PlayerUnlock.Builder namespacedBuilder(ResourceLocation resourceLocation, Optional<Holder<PlayerUnlock>> parent, Optional<ClientAsset> background) {
        return new PlayerUnlock.Builder(resourceLocation.toString(), parent, background).craftMinePlus$namespaced();
    }

    interface BuilderDuck {
        default PlayerUnlock.Builder craftMinePlus$namespaced() {
            throw new AssertionError("Implemented Via Mixin");
        }
    }
}
