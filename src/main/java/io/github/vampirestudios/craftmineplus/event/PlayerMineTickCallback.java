package io.github.vampirestudios.craftmineplus.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;

public interface PlayerMineTickCallback {

    Event<PlayerMineTickCallback> EVENT = EventFactory.createArrayBacked(PlayerMineTickCallback.class,
            (listeners) -> (player) -> {
                for (PlayerMineTickCallback listener : listeners) {
                    InteractionResult result = listener.tick(player);

                    if(result != InteractionResult.PASS) {
                        return result;
                    }
                }

                return InteractionResult.PASS;
            });

    InteractionResult tick(Player player);
}
