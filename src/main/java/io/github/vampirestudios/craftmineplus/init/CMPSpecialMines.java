package io.github.vampirestudios.craftmineplus.init;

import io.github.vampirestudios.craftmineplus.CraftminePlus;
import io.github.vampirestudios.craftmineplus.pond.SpecialMineDuck;
import net.minecraft.world.level.UnlockCondition;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.mines.SpecialMine;
import net.minecraft.world.level.mines.WorldEffects;

public class CMPSpecialMines {
    public static final SpecialMine MAGMA_BOSS = SpecialMineDuck.namespacedBuilder(CraftminePlus.id("magma_boss"))
            .withRequiredEffects(
                    CMPWorldEffects.NETHER_WORLD, CMPWorldEffects.MAGMA_BOSS_FIGHT, WorldEffects.EVENT_EXIT
            )
            .withOneOf(WorldEffects.BIOMES)
            .addUnlockedBy(UnlockCondition.blockBreak((serverLevel, blockState) ->
                    blockState.is(Blocks.MAGMA_BLOCK) || blockState.is(Blocks.NETHERRACK)
            ))
            .register();

    public static void init() {
    }
}
