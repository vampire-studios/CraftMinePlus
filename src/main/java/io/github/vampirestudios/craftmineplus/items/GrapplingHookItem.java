package io.github.vampirestudios.craftmineplus.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class GrapplingHookItem extends Item {
    private static final int COOLDOWN_TICKS = 20; // 1 second cooldown

    public GrapplingHookItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (player.fishing != null) {
            if (!level.isClientSide) {
                int i = player.fishing.retrieve(itemStack);
                itemStack.hurtAndBreak(i, player, LivingEntity.getSlotForHand(interactionHand));
            }

            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
        } else {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            if (level instanceof ServerLevel serverLevel) {
//                Projectile.spawnProjectile(new FishingHook(player, level, k, j), serverLevel, itemStack);
            }

            player.awardStat(Stats.ITEM_USED.get(this));
            player.gameEvent(GameEvent.ITEM_INTERACT_START);
        }

        return InteractionResult.SUCCESS;
    }

    /**
     * Pulls the player toward the target position.
     */
    private void pullPlayerToPosition(Player player, BlockPos targetPos, Direction face) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        Vec3 targetVec = Vec3.atCenterOf(targetPos).add(face.getStepX() * 0.5, face.getStepY() * 0.5, face.getStepZ() * 0.5);
        Vec3 playerVec = player.position();
        Vec3 motion = targetVec.subtract(playerVec).normalize().scale(1.5); // Adjust speed here.

        // Apply motion to the player.
        serverPlayer.setDeltaMovement(motion);
        serverPlayer.hurtMarked = true; // Ensure the motion is applied immediately.
    }
}