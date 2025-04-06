package io.github.vampirestudios.craftmineplus.datagen;

import io.github.vampirestudios.craftmineplus.init.CMPItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CMPDataGen implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(BlockStateDefinitionProvider::new);
		pack.addProvider(TranslationProvider::new);
		pack.addProvider(RecipeProvider::new);
		BlockTagProvider blockTagsProvider = pack.addProvider(BlockTagProvider::new);
		pack.addProvider((output, registriesFuture) -> new ItemTagProvider(output, registriesFuture, blockTagsProvider));
		pack.addProvider(BlockLootTableProvider::new);
	}

	private static class BlockStateDefinitionProvider extends FabricModelProvider {

		public BlockStateDefinitionProvider(FabricDataOutput output) {
			super(output);
		}

		@Override
		public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {

		}

		@Override
		public void generateItemModels(ItemModelGenerators itemModelGenerators) {
			itemModelGenerators.generateFlatItem(CMPItems.TRASH, ModelTemplates.FLAT_ITEM);
			itemModelGenerators.generateFlatItem(CMPItems.GRAPPLING_HOOK, ModelTemplates.FLAT_ITEM);
		}
	}

	private static class BlockTagProvider extends FabricTagProvider.BlockTagProvider {

		public BlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
			super(output, registriesFuture);
		}

		@Override
		protected void addTags(HolderLookup.Provider provider) {

		}
	}

	private static class ItemTagProvider extends FabricTagProvider.ItemTagProvider {

		public ItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable BlockTagProvider blockTagProvider) {
			super(output, completableFuture, blockTagProvider);
		}

		@Override
		protected void addTags(HolderLookup.Provider provider) {

		}
	}

	private static class BlockLootTableProvider extends FabricBlockLootTableProvider {

		protected BlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
			super(dataOutput, registryLookup);
		}

		@Override
		public void generate() {

		}
	}

	private static class RecipeProvider extends FabricRecipeProvider {

		public RecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
			super(output, registriesFuture);
		}

		@Override
		protected net.minecraft.data.recipes.RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
			return new net.minecraft.data.recipes.RecipeProvider(provider, recipeOutput) {
				@Override
				public void buildRecipes() {

				}
			};
		}

		@Override
		public String getName() {
			return "CMP Recipe Provider";
		}
	}

	private static class TranslationProvider extends FabricLanguageProvider {

		protected TranslationProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
			super(dataOutput, registryLookup);
		}

		@Override
		public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder translationBuilder) {
			translationBuilder.add(CMPItems.TRASH, "Trash");
			translationBuilder.add(CMPItems.GRAPPLING_HOOK, "Grappling Hook");
			translationBuilder.add("unlocks.unlock.short.name", "Short");
			translationBuilder.add("unlocks.unlock.short.description", "Become short.");
			translationBuilder.add("unlocks.unlock.smol.name", "Smol");
			translationBuilder.add("unlocks.unlock.smol.description", "Become very smol UwU.");
		}
	}
}
