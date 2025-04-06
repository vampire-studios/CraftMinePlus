package io.github.vampirestudios.craftmineplus.init;

import io.github.vampirestudios.craftmineplus.CraftminePlus;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class CMPItems {

	public static Item TRASH;

	public static void init() {
		TRASH = register("trash", new Item.Properties());
	}

	public static Item register(String name, Item item) {
		return Registry.register(BuiltInRegistries.ITEM, CraftminePlus.id(name), item);
	}

	public static Item register(String name, Item.Properties properties) {
		return register(name, Item::new, properties);
	}

	public static Item register(String name, Function<Item.Properties, Item> function) {
		return register(name, function, new Item.Properties());
	}

	public static Item register(String name, Function<Item.Properties, Item> function, Item.Properties properties) {
		return Registry.register(BuiltInRegistries.ITEM, CraftminePlus.id(name), function.apply(properties));
	}

}
