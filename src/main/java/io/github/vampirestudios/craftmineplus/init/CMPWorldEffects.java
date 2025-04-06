package io.github.vampirestudios.craftmineplus.init;

import io.github.vampirestudios.craftmineplus.CraftminePlus;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.players.PlayerUnlocks;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.UnlockCondition;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.GridChunkGenerator;
import net.minecraft.world.level.mines.WorldEffect;
import net.minecraft.world.level.mines.WorldEffects;

public class CMPWorldEffects {

	public static final WorldEffect REVERSE_GRAVITY = WorldEffect.builder("reverse_gravity")
			.withItemModelOf(Items.FEATHER)
			.withNameStyle(Style.EMPTY.withColor(ChatFormatting.AQUA))
			.onPlayerMineEnter(player -> player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 60, 1)))
			.xpModifier(3.0F)
			.unlockedBy(UnlockCondition.blockBreak((serverLevel, blockState) ->
					blockState.is(Blocks.SLIME_BLOCK) && serverLevel.getRandom().nextFloat() < 0.1F
			))
			.register();
	public static final WorldEffect GOLDEN_TOUCH = WorldEffect.builder("golden_touch")
			.withItemModelOf(Items.GOLD_INGOT)
			.withNameStyle(Style.EMPTY.withColor(ChatFormatting.GOLD))
			.onPlayerMineEnter(player -> player.addEffect(new MobEffectInstance(MobEffects.LUCK, 200, 0)))
			.xpModifier(2.5F)
			.unlockedBy(UnlockCondition.playerKilledEntity((level, player, entity) ->
					entity.getType() == EntityType.PIGLIN && player.getMainHandItem().is(Items.GOLDEN_SWORD)
			))
			.register();
	public static final WorldEffect MIRRORED_WORLD = WorldEffect.builder("mirrored_world")
			.withItemModelOf(Items.GLASS)
			.withNameStyle(Style.EMPTY.withItalic(true).withColor(ChatFormatting.GRAY))
			.onPlayerMineEnter(player -> player.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 100, 0)))
			.xpModifier(2.0F)
			.unlockedBy(UnlockCondition.mineCompletedWith(true, WorldEffects.BOUNCY))
			.register();
	public static final WorldEffect DIMENSIONAL_GLITCH = WorldEffect.builder("dimensional_glitch")
			.withItemModelOf(Items.DEBUG_STICK)
			.withNameStyle(Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE))
			.onPlayerMineEnter(player -> player.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 200, 1)))
			.xpModifier(3.5F)
			.unlockedBy(UnlockCondition.craftedItem((level, player, itemStack) ->
					itemStack.is(Items.DEBUG_STICK) || itemStack.is(Items.BARRIER)
			))
			.register();
	public static final WorldEffect LOW_GRAVITY = WorldEffect.builder("low_gravity")
			.withItemModelOf(Items.RABBIT_FOOT)
			.withNameStyle(Style.EMPTY.withColor(ChatFormatting.WHITE))
			.onPlayerMineEnter(player -> player.addEffect(new MobEffectInstance(MobEffects.JUMP_BOOST, 300, 2)))
			.xpModifier(1.5F)
			.unlockedBy(UnlockCondition.playerTookDamage((level, player, source, damage) ->
					damage > 15.0F && source.is(DamageTypes.FALL)
			))
			.register();
	public static final WorldEffect WARPED_WORLD = WorldEffect.builder("warped_world")
			.withItemModelOf(Items.CHORUS_FRUIT)
			.withNameStyle(Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE))
			.modifyingWorldGen(worldGenBuilder ->
					worldGenBuilder.changeNoiseGeneration(gen ->
							gen.seaLevel(80).defaultBlock(Blocks.CHORUS_FLOWER.defaultBlockState())
					)
			)
			.xpModifier(3.0F)
			.unlockedBy(UnlockCondition.usedItem((level, player, itemStack) ->
					itemStack.is(Items.CHORUS_FRUIT)
			))
			.register();

	public static WorldEffect parkour;
	public static WorldEffect parkour1;
	public static WorldEffect parkour2;

	public static void init() {
		parkour = register(WorldEffect.builder(CraftminePlus.id("parkour").toString()).modifyingWorldGen((worldGenBuilder) -> {
			worldGenBuilder.withCustomChunkGenerator((provider, biomeSource, holder) -> new GridChunkGenerator(biomeSource, holder, 2, 1, 64, false));
		}).withCustomIcon("grid_world").inSet(WorldEffects.WORLD_TYPE).unlockedBy(UnlockCondition.unlocked(PlayerUnlocks.JUMPING_10)).build());
		parkour1 = register(WorldEffect.builder(CraftminePlus.id("parkour1").toString()).modifyingWorldGen((worldGenBuilder) -> {
			worldGenBuilder.withCustomChunkGenerator((provider, biomeSource, holder) -> new GridChunkGenerator(biomeSource, holder, 4, 1, 64, false));
		}).withCustomIcon("grid_world").inSet(WorldEffects.WORLD_TYPE).unlockedBy(UnlockCondition.unlocked(parkour)).build());
		parkour2 = register(WorldEffect.builder(CraftminePlus.id("parkour2").toString()).modifyingWorldGen((worldGenBuilder) -> {
			worldGenBuilder.withCustomChunkGenerator((provider, biomeSource, holder) -> new GridChunkGenerator(biomeSource, holder, 6, 1, 64, false));
		}).withCustomIcon("grid_world").inSet(WorldEffects.WORLD_TYPE).unlockedBy(UnlockCondition.unlocked(parkour1)).build());
	}

	public static WorldEffect register(WorldEffect worldEffect) {
		return Registry.register(BuiltInRegistries.WORLD_EFFECT, ResourceLocation.bySeparator(worldEffect.key().replaceFirst("-", ":"), ':'), worldEffect);
	}

}
