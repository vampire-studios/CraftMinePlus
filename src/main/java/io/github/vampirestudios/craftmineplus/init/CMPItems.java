package io.github.vampirestudios.craftmineplus.init;

import io.github.vampirestudios.craftmineplus.CraftminePlus;
import io.github.vampirestudios.craftmineplus.util.ModItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class CMPItems {

	public static ModItem TRASH;

	public static void init() {
		TRASH = new ModItem("trash", new Item(new Item.Properties()));
	}
}
