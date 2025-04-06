package io.github.vampirestudios.craftmineplus.init;

import io.github.vampirestudios.craftmineplus.CraftminePlus;
import io.github.vampirestudios.craftmineplus.entities.GrapplingHookEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class CMPEntities {

	public static final EntityType<GrapplingHookEntity> GRAPPLING_HOOK = Registry.register(
			BuiltInRegistries.ENTITY_TYPE,
			CraftminePlus.id("grappling_hook"),
			FabricEntityTypeBuilder.<GrapplingHookEntity>create(MobCategory.MISC, GrapplingHookEntity::new)
					.dimensions(EntityDimensions.fixed(0.25F, 0.25F))
					.trackRangeBlocks(64)
					.trackedUpdateRate(1)
					.build(ResourceKey.create(Registries.ENTITY_TYPE, CraftminePlus.id("grappling_hook")))
	);

	public static void init() {}

}
