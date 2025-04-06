package io.github.vampirestudios.craftmineplus.init;

import net.minecraft.core.Holder;
import net.minecraft.server.players.PlayerUnlock;
import net.minecraft.server.players.PlayerUnlocks;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class CMPPlayerUnlocks {

    public static Holder<PlayerUnlock> SHORT;
    public static Holder<PlayerUnlock> SMOL;

    public static void init() {
        SHORT = PlayerUnlock.child("short", PlayerUnlocks.SCHOOL_OF_HARD_KNOCKS).withIcon(Items.DRAGON_BREATH).withPrice(5).givesAttributeModifier(Attributes.SCALE, -0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE).register();
        SMOL = PlayerUnlock.child("smol", SHORT).withIcon(Items.DRAGON_BREATH).withPrice(10).givesAttributeModifier(Attributes.SCALE, -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE).register();
    }
}
