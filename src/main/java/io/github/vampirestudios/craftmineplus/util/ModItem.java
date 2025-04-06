package io.github.vampirestudios.craftmineplus.util;

import io.github.vampirestudios.craftmineplus.CraftminePlus;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

public class ModItem implements ItemLike {

    private final String name;
    private final ResourceLocation resourceLocation;
    private final Item item;

    public ModItem(String name, Item item) {
        this.name = name;
        this.resourceLocation = CraftminePlus.id(name);
        this.item = item;

        Registry.register(BuiltInRegistries.ITEM, this.resourceLocation, this.item);
    }

    @Override
    public @NotNull Item asItem() {
        return this.item;
    }

    public String getName() {
        return this.name;
    }

    public ResourceLocation getResourceLocation() {
        return this.resourceLocation;
    }
}
