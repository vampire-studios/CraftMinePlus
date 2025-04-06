package io.github.vampirestudios.craftmineplus.init;

import io.github.vampirestudios.craftmineplus.CraftminePlus;
import io.github.vampirestudios.craftmineplus.items.GrapplingHookItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class CMPItems {

	public static Item TRASH;
	public static Item GRAPPLING_HOOK;

	public static void init() {
		TRASH = register("trash", new Item.Properties());
		GRAPPLING_HOOK = register("grappling_hook", GrapplingHookItem::new, new Item.Properties().stacksTo(1));
	}

	public static Item register(String name, Item item) {
		return Registry.register(BuiltInRegistries.ITEM, CraftminePlus.id(name), item);
	}

	public static Item register(String name, Item.Properties properties) {
		return register(name, Item::new, properties.setId(ResourceKey.create(Registries.ITEM, CraftminePlus.id(name))));
	}

	public static Item register(String name, Function<Item.Properties, Item> function) {
		return register(name, function, new Item.Properties());
	}

	public static Item register(String name, Function<Item.Properties, Item> function, Item.Properties properties) {
		return Registry.register(BuiltInRegistries.ITEM, CraftminePlus.id(name), function.apply(
				properties.setId(ResourceKey.create(Registries.ITEM, CraftminePlus.id(name)))
		));
	}

}
