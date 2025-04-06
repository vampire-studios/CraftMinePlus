package io.github.vampirestudios.craftmineplus.init;

import io.github.vampirestudios.craftmineplus.CraftminePlus;
import io.github.vampirestudios.craftmineplus.pond.WorldEffectDuck;
import io.github.vampirestudios.craftmineplus.world.MineShaftChunkGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.UnlockCondition;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.mines.Battle;
import net.minecraft.world.level.mines.MineSpawnStrategy;
import net.minecraft.world.level.mines.WorldEffect;
import net.minecraft.world.level.mines.WorldEffects;

public class CMPWorldEffects {

	public static WorldEffect MINESHAFT;
	public static WorldEffect NETHER_WORLD;
	public static WorldEffect END_WORLD;
	public static WorldEffect MAGMA_BOSS_FIGHT;

	public static void init() {
		MINESHAFT = WorldEffectDuck.builder(CraftminePlus.id("mine_shaft"))
				.modifyingWorldGen(worldGenBuilder -> {
					worldGenBuilder
							.changeNoiseGeneration(builder -> builder.defaultBlock(Blocks.OAK_PLANKS.defaultBlockState()))
							.withCustomChunkGenerator((provider, biomeSource, holder) ->
									new MineShaftChunkGenerator(biomeSource, holder, 32, 6, 8)
							);
				})
				.craftMinePlus$withCustomIcon(CraftminePlus.id("mine_shaft"))
				.inSet(WorldEffects.WORLD_TYPE)
				.unlockedBy(UnlockCondition.usedItem((level, player, itemStack) -> itemStack.is(Items.STONE)))
				.register();
		NETHER_WORLD = WorldEffectDuck.builder(CraftminePlus.id("nether_world"))
				.modifyingWorldGen(
						worldGenBuilder -> worldGenBuilder.setNoiseGenerationBase(NoiseGeneratorSettings.NETHER)
								.withSpawnStrategy(MineSpawnStrategy.CAVE)
								.changeDimensionType(DimensionType::withAmbientLight)
				)
				.withCustomIcon("nether_world")
				.inSet(WorldEffects.WORLD_TYPE)
				.unlockedAfter(WorldEffects.SURFACE_WORLD)
				.unlockedBy(UnlockCondition.playerKilledEntity(EntityType.WITHER))
				.register();
		END_WORLD = WorldEffectDuck.builder(CraftminePlus.id("end_world"))
				.modifyingWorldGen(
						worldGenBuilder -> worldGenBuilder.setNoiseGenerationBase(NoiseGeneratorSettings.END)
								.withSpawnStrategy(MineSpawnStrategy.SURFACE)
								.changeDimensionType(DimensionType::withAmbientLight)
				)
				.withCustomIcon("end_world")
				.inSet(WorldEffects.WORLD_TYPE)
				.unlockedAfter(NETHER_WORLD)
				.unlockedBy(UnlockCondition.playerKilledEntity(EntityType.ENDERMAN))
				.register();
		MAGMA_BOSS_FIGHT = WorldEffect.builder("magma_boss")
				.withItemModelOf(Items.MAGMA_CREAM)
				.onMineEnter(serverLevel -> serverLevel.startEvent(
						Battle.builder(serverLevel, "magma_boss")
								.withWave(builder -> builder
										.spawns(builderx -> builderx
												.type(EntityType.MAGMA_CUBE)
												.count(8)
												.withSpawnStrategy(builderxx -> builderxx
														.type(Battle.SpawnType.NEAR_PLAYER)
														.range(30)
														.offset(new BlockPos(0, 2, 0))
												)
										).ticksDelay(600)
								)
								.withWave(builder -> builder
										.spawns(builderx -> builderx
												.type(EntityType.BLAZE)
												.count(6)
												.withSpawnStrategy(builderxx -> builderxx
														.type(Battle.SpawnType.NEAR_PLAYER)
														.range(30)
														.offset(new BlockPos(0, 2, 0))
												)
										).ticksDelay(600)
								)
								.withWave(builder -> builder
										.spawns(builderx -> builderx
												.type(EntityType.MAGMA_CUBE)
												.count(12)
												.withSpawnStrategy(builderxx -> builderxx
														.type(Battle.SpawnType.NEAR_PLAYER)
														.range(30)
														.offset(new BlockPos(0, 2, 0))
												)
										).spawns(builderx -> builderx
												.type(EntityType.BLAZE)
												.count(8)
												.withSpawnStrategy(builderxx -> builderxx
														.type(Battle.SpawnType.NEAR_PLAYER)
														.range(30)
														.offset(new BlockPos(0, 2, 0))
												)
										).ticksDelay(600)
								).build()
				))
				.neverUnlocked()
				.notRandomizable()
				.register();
	}
}
