package io.github.vampirestudios.craftmineplus.init;

import io.github.vampirestudios.craftmineplus.event.PlayerMineTickCallback;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;

public class CMPEvents {

    public static void init() {

        //Trash Event
        PlayerMineTickCallback.EVENT.register((player) -> {

            if(player.isActive(CMPPlayerUnlocks.TRASHY) && player.getRandom().nextInt(100) == 0) {
                player.addOrDropItem(CMPItems.TRASH.getDefaultInstance().copyWithCount(1));
            }

            return InteractionResult.PASS;
        });
    }
}
