package io.github.vampirestudios.craftmineplus.init;

import io.github.vampirestudios.craftmineplus.CraftminePlus;
import io.github.vampirestudios.craftmineplus.pond.PlayerUnlockDuck;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.Holder;
import net.minecraft.server.players.PlayerUnlock;
import net.minecraft.server.players.PlayerUnlocks;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.UnlockCondition;

import java.util.Optional;

import static net.minecraft.server.players.PlayerUnlocks.*;

public class CMPPlayerUnlocks {

    public static Holder<PlayerUnlock> SHORT;
    public static Holder<PlayerUnlock> SMOL;
    public static Holder<PlayerUnlock> NIGHT_VISION_GOGGLES;
    public static Holder<PlayerUnlock> ANVIL_MASTER;
    public static Holder<PlayerUnlock> FORAGERS_LUCK;
    public static Holder<PlayerUnlock> VAMPIRES_BLADE;
    public static Holder<PlayerUnlock> IRON_WILL;
    public static Holder<PlayerUnlock> ADVENTURER;
    public static Holder<PlayerUnlock> TREASURE_HUNTER;
    public static Holder<PlayerUnlock> HEAVY_BURDEN;
    public static Holder<PlayerUnlock> WEAKENED_RESOLVE;
    public static Holder<PlayerUnlock> CLUMSY_FIGHTER;
    public static Holder<PlayerUnlock> BLAZING_VULNERABILITY;
    public static Holder<PlayerUnlock> SHAKY_GROUND;
    public static Holder<PlayerUnlock> MOB_BEACON;

    public static void init() {
        SHORT = PlayerUnlockDuck.child(CraftminePlus.id("short"), PlayerUnlocks.SCHOOL_OF_HARD_KNOCKS)
                .withIcon(Items.DRAGON_BREATH)
                .withPrice(5)
                .givesAttributeModifier(Attributes.SCALE, -0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .register();
        SMOL = PlayerUnlockDuck.child(CraftminePlus.id("smol"), SHORT)
                .withIcon(Items.DRAGON_BREATH)
                .withPrice(10)
                .givesAttributeModifier(Attributes.SCALE, -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .register();

        HEAVY_BURDEN = PlayerUnlockDuck.child(CraftminePlus.id("heavy_burden"), SMOL)
                .withIcon(Items.IRON_BLOCK)
                .givesEffectInMine(MobEffects.SLOWNESS, 1200, 0) // 1-minute Slowness effect, level 0
                .withPrice(15)
                .withVisibility(PlayerUnlock.UnlockVisibility.VISIBLE)
                .register();

        WEAKENED_RESOLVE = PlayerUnlockDuck.child(CraftminePlus.id("weakened_resolve"), HEAVY_BURDEN)
                .withIcon(Items.WITHER_ROSE)
                .givesAttributeModifier(Attributes.MAX_HEALTH, -4.0, AttributeModifier.Operation.ADD_VALUE)
                .withPrice(20)
                .withVisibility(PlayerUnlock.UnlockVisibility.VISIBLE)
                .register();

        CLUMSY_FIGHTER = PlayerUnlockDuck.child(CraftminePlus.id("clumsy_fighter"), WEAKENED_RESOLVE)
                .withIcon(Items.WOODEN_SWORD)
                .givesAttributeModifier(Attributes.ATTACK_SPEED, -1.0, AttributeModifier.Operation.ADD_VALUE)
                .givesAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, -0.3, AttributeModifier.Operation.ADD_VALUE)
                .withPrice(25)
                .withVisibility(PlayerUnlock.UnlockVisibility.VISIBLE)
                .register();

        BLAZING_VULNERABILITY = PlayerUnlockDuck.child(CraftminePlus.id("blazing_vulnerability"), CLUMSY_FIGHTER)
                .withIcon(Items.BLAZE_POWDER)
                .givesAttributeModifier(Attributes.BURNING_TIME, 1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .givesAttributeModifier(Attributes.EXPERIENCE_GAIN_MODIFIER, -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .withPrice(30)
                .withVisibility(PlayerUnlock.UnlockVisibility.VISIBLE)
                .register();

        SHAKY_GROUND = PlayerUnlockDuck.child(CraftminePlus.id("shaky_ground"), BLAZING_VULNERABILITY)
                .withIcon(Items.SLIME_BLOCK)
                .givesEffectInMine(MobEffects.LEVITATION, 40, 0) // 2-second Levitation to simulate a tremor
                .givesAttributeModifier(Attributes.EXPERIENCE_GAIN_MODIFIER, -0.3, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .withPrice(35)
                .withVisibility(PlayerUnlock.UnlockVisibility.VISIBLE)
                .register();

        MOB_BEACON = PlayerUnlockDuck.child(CraftminePlus.id("mob_beacon"), SHAKY_GROUND)
                .withIcon(Items.CREEPER_HEAD)
                .givesEffectInMine(MobEffects.GLOWING, 1200, 0) // 1-minute Glowing effect
                .givesAttributeModifier(Attributes.EXPERIENCE_GAIN_MODIFIER, -0.4, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .withPrice(40)
                .withVisibility(PlayerUnlock.UnlockVisibility.VISIBLE)
                .register();

        NIGHT_VISION_GOGGLES = PlayerUnlockDuck.child(CraftminePlus.id("night_vision_goggles"), MOVEMENT_SPEED_3)
                .withIcon(Items.SPYGLASS)
                .givesEffectInMine(MobEffects.NIGHT_VISION, 1200, 0)
                .withPrice(30)
                .withVisibility(PlayerUnlock.UnlockVisibility.INVISIBLE)
                .becomesVisibleWhen(UnlockCondition.unlocked(MOVEMENT_SPEED_3))
                .register();

        ANVIL_MASTER = PlayerUnlockDuck.child(CraftminePlus.id("anvil_master"), SMELT_VALUE_2)
                .withIcon(Items.ANVIL)
                .givesItemStackInMine(
                        Items.ANVIL.getDefaultInstance(),
                        Items.IRON_INGOT.getDefaultInstance().copyWithCount(10)
                )
                .withPrice(20)
                .withVisibility(PlayerUnlock.UnlockVisibility.INVISIBLE)
                .becomesVisibleWhen(UnlockCondition.unlocked(SMELT_VALUE_2))
                .register();

        FORAGERS_LUCK = PlayerUnlockDuck.child(CraftminePlus.id("foragers_luck"), FISHING)
                .withIcon(Items.WHEAT_SEEDS)
                .givesAttributeModifier(Attributes.LUCK, 2.0, AttributeModifier.Operation.ADD_VALUE)
                .withPrice(15)
                .withVisibility(PlayerUnlock.UnlockVisibility.INVISIBLE)
                .becomesVisibleWhen(UnlockCondition.unlocked(FISHING))
                .register();

        VAMPIRES_BLADE = PlayerUnlockDuck.child(CraftminePlus.id("vampires_blade"), BEST_STARTER_SWORD)
                .withIcon(Items.NETHERITE_SWORD)
                .givesEnchantedItemInMine(
                        Items.NETHERITE_SWORD,
                        Pair.of(Enchantments.SHARPNESS, 5),
                        Pair.of(Enchantments.LOOTING, 3),
                        Pair.of(Enchantments.UNBREAKING, 3)
                )
                .withPrice(60)
                .withVisibility(PlayerUnlock.UnlockVisibility.MYSTERY)
                .becomesVisibleWhen(UnlockCondition.playerKilledEntity((serverLevel, serverPlayer, entity) -> entity.getType() == EntityType.WITHER))
                .register();

        IRON_WILL = PlayerUnlockDuck.child(CraftminePlus.id("iron_will"), THORNS_PLUS_PLUS)
                .withIcon(Items.IRON_BLOCK)
                .givesAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, 0.5, AttributeModifier.Operation.ADD_VALUE)
                .withPrice(50)
                .withVisibility(PlayerUnlock.UnlockVisibility.VISIBLE)
                .register();

        ADVENTURER = PlayerUnlockDuck.createNamespaced(CraftminePlus.id("adventurer"), Optional.empty(), Optional.of(new ClientAsset(CraftminePlus.id("unlock_backgrounds/adventurer"))))
                .withIcon(Items.MAP)
                .givesAttributeModifier(Attributes.MAX_HEALTH, 2.0, AttributeModifier.Operation.ADD_VALUE)
                .withPrice(0)
                .register();

        TREASURE_HUNTER = PlayerUnlockDuck.child(CraftminePlus.id("treasure_hunter"), ADVENTURER)
                .withIcon(Items.FILLED_MAP)
                .givesAttributeModifier(Attributes.LUCK, 3.0, AttributeModifier.Operation.ADD_VALUE)
                .withPrice(25)
                .withVisibility(PlayerUnlock.UnlockVisibility.VISIBLE)
                .register();
    }
}