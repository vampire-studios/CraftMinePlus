package io.github.vampirestudios.craftmineplus.mixin;

import io.github.vampirestudios.craftmineplus.event.PlayerMineTickCallback;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void craftmine_plus$tick(CallbackInfo ci) {
        Player player = ((Player)(Object)this);
        Level level = player.level();

        //Get ServerLevel
        if(level instanceof ServerLevel serverLevel) {

            //If ServerLevel is Mine handle event.
            if(serverLevel.isMine()) {

                InteractionResult result = PlayerMineTickCallback.EVENT.invoker().tick(player);
            }
        }
    }
}
