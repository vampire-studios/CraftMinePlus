package io.github.vampirestudios.craftmineplus.datagen;

import io.github.vampirestudios.craftmineplus.init.CMPItems;
import io.github.vampirestudios.craftmineplus.init.CMPPlayerUnlocks;
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
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.server.players.PlayerUnlock;
import net.minecraft.world.level.mines.SpecialMine;
import net.minecraft.world.level.mines.WorldEffect;
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
            addUnlock(translationBuilder, CMPPlayerUnlocks.SHORT, "Short", "Your short now.");
            addUnlock(translationBuilder, CMPPlayerUnlocks.SMOL, "Smol :3", "U ar verwy smol now UwU. <3");
        }

        private void addUnlock(TranslationBuilder builder, Holder<PlayerUnlock> unlock, String name, String description) {
            var key = unlock.value().key().replace(":", "_");
            builder.add("unlocks.unlock." + key + ".name", name);
            builder.add("unlocks.unlock." + key + ".description", description);
        }

        private void addWorldEffect(TranslationBuilder builder, WorldEffect effect, String name, String description, String hint) {
            var key = effect.key().replace(":", "_");
            builder.add("world.effect." + key + ".name", name);
            builder.add("world.effect." + key + ".description", description);
            builder.add("world.effect." + key + ".hint", hint);
        }

        private void addSpecialMine(TranslationBuilder builder, SpecialMine mine, String name, String description) {
            var key = mine.key().replace(":", "_");
            builder.add("mine." + key + ".name", name);
            builder.add("mine." + key + ".description", description);
        }
    }
}
