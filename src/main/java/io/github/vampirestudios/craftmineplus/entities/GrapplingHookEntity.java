package io.github.vampirestudios.craftmineplus.entities;


import io.github.vampirestudios.craftmineplus.init.CMPEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class GrapplingHookEntity extends net.minecraft.world.entity.Entity {
    private BlockPos targetPos;
    private Player owner;

    public GrapplingHookEntity(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    public GrapplingHookEntity(Level world, Player owner) {
        this(CMPEntities.GRAPPLING_HOOK, world);
        this.owner = owner;
        this.setPos(owner.getX(), owner.getEyeY() - 0.4, owner.getZ());
    }

    public void setTarget(BlockPos targetPos) {
        this.targetPos = targetPos;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.owner == null || !this.owner.isAlive()) {
            this.discard();
            return;
        }

        // Pull the player toward the target.
        Vec3 targetVec = Vec3.atCenterOf(this.targetPos);
        Vec3 playerVec = this.owner.position();
        Vec3 motion = targetVec.subtract(playerVec).normalize().scale(0.2); // Adjust speed here.

        this.owner.setDeltaMovement(motion.x, motion.y - 0.05, motion.z); // Simulate gravity.

        // Check if the player is close enough to the target.
        if (playerVec.distanceTo(targetVec) < 1.5) {
            this.discard();
        }
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float f) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        this.targetPos = new BlockPos(compoundTag.getIntOr("TargetX", 0), compoundTag.getIntOr("TargetY", 0), compoundTag.getIntOr("TargetZ", 0));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putInt("TargetX", this.targetPos.getX());
        compoundTag.putInt("TargetY", this.targetPos.getY());
        compoundTag.putInt("TargetZ", this.targetPos.getZ());
    }
}