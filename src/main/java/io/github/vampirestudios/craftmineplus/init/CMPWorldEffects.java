package io.github.vampirestudios.craftmineplus.init;

import io.github.vampirestudios.craftmineplus.CraftminePlus;
import io.github.vampirestudios.craftmineplus.MineShaftChunkGenerator;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.UnlockCondition;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.mines.WorldEffect;
import net.minecraft.world.level.mines.WorldEffects;

public class CMPWorldEffects {
	public static WorldEffect MINESHAFT;

	public static void init() {
		MINESHAFT = register(WorldEffect.builder(CraftminePlus.id("mine_shaft").toString())
				.modifyingWorldGen(worldGenBuilder -> {
					worldGenBuilder
							.changeNoiseGeneration(builder -> builder.defaultBlock(Blocks.OAK_PLANKS.defaultBlockState()))
							.withCustomChunkGenerator((provider, biomeSource, holder) ->
									new MineShaftChunkGenerator(biomeSource, holder, 32, 6, 8)
							);
				})
				.withCustomIcon("mine_shaft")
				.inSet(WorldEffects.WORLD_TYPE)
				.unlockedBy(UnlockCondition.usedItem((level, player, itemStack) -> itemStack.is(Items.STONE)))
				.build()
		);
	}

	public static WorldEffect register(WorldEffect worldEffect) {
		return Registry.register(BuiltInRegistries.WORLD_EFFECT, ResourceLocation.bySeparator(worldEffect.key().replaceFirst("-", ":"), ':'), worldEffect);
	}

}
