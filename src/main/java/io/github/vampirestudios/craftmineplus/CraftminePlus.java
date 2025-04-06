package io.github.vampirestudios.craftmineplus;

import io.github.vampirestudios.craftmineplus.init.CMPEvents;
import io.github.vampirestudios.craftmineplus.init.CMPEntities;
import io.github.vampirestudios.craftmineplus.init.CMPItems;
import io.github.vampirestudios.craftmineplus.init.CMPPlayerUnlocks;
import io.github.vampirestudios.craftmineplus.init.CMPWorldEffects;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.LevelCommand;
import net.minecraft.server.commands.RoomCommand;
import net.minecraft.server.commands.UnlockCommand;
import net.minecraft.server.commands.UnlockWorldEffectCommand;

public class CraftminePlus implements ModInitializer {

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath("craftmine_plus", path);
	}

	@Override
	public void onInitialize() {

		//Adds objects needed for other stuff
		CMPItems.init();
		CMPEntities.init();

		//Does things with that
		CMPWorldEffects.init();
		CMPPlayerUnlocks.init();
		CMPEvents.init();

		CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> {
			LevelCommand.register(commandDispatcher, commandBuildContext);
			UnlockCommand.register(commandDispatcher, commandBuildContext);
			UnlockWorldEffectCommand.register(commandDispatcher, commandBuildContext);
			RoomCommand.register(commandDispatcher);
		});
	}
}
