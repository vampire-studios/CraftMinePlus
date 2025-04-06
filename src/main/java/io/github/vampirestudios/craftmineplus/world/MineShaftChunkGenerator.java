package io.github.vampirestudios.craftmineplus;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class MineShaftChunkGenerator extends ChunkGenerator {
    // Codec definition for serialization
    public static final MapCodec<MineShaftChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BiomeSource.CODEC.fieldOf("biome_source").forGetter(gen -> gen.biomeSource),
                    NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(gen -> gen.settings),
                    Codec.INT.fieldOf("corridor_y").forGetter(gen -> gen.corridorY),
                    Codec.INT.fieldOf("corridor_width").forGetter(gen -> gen.corridorWidth),
                    Codec.INT.fieldOf("corridor_height").forGetter(gen -> gen.corridorHeight)
            ).apply(instance, instance.stable(MineShaftChunkGenerator::new))
    );

    private final Holder<NoiseGeneratorSettings> settings;
    private final int corridorY;
    private final int corridorWidth;
    private final int corridorHeight;

    public MineShaftChunkGenerator(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings,
                                   int corridorY, int corridorWidth, int corridorHeight) {
        super(biomeSource);
        this.settings = settings;
        this.corridorY = corridorY;
        this.corridorWidth = corridorWidth;
        this.corridorHeight = corridorHeight;
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public NoiseGeneratorSettings getNoiseGeneratorSettings() {
        return this.settings.value();
    }

    @Override
    public void applyCarvers(WorldGenRegion region, long seed, RandomState randomState,
                             net.minecraft.world.level.biome.BiomeManager biomeManager,
                             StructureManager structureManager, ChunkAccess chunk) {
        // No carvers needed; custom logic handles everything
    }

    @Override
    public void buildSurface(WorldGenRegion region, StructureManager structureManager,
                             RandomState randomState, ChunkAccess chunk) {
        fillFromNoise(Blender.of(region), randomState, structureManager, chunk);

    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {
        // Mob spawning handled via spawners
    }

    @Override
    public int getGenDepth() {
        return this.settings.value().noiseSettings().height();
    }

    @Override
    public int getMinY() {
        return this.settings.value().noiseSettings().minY();
    }

    @Override
    public int getSeaLevel() {
        return 63; // Default sea level
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState,
                                                        StructureManager structureManager, ChunkAccess chunk) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        BlockState air = Blocks.AIR.defaultBlockState();
        Random random = new Random(chunk.getPos().x ^ chunk.getPos().z);

        fillWithStone(chunk, pos, random);
        generateOreVeins(chunk, pos, random);
        generateMainCorridor(chunk, pos, air);
        placeWoodenSupports(chunk, pos);
        List<BlockPos> branchEnds = new ArrayList<>();
        generateBranches(chunk, pos, air, random, branchEnds);
        generateRoom(chunk, pos, air, random);
        placeTorches(chunk, pos, random);
        placeBranchEndFeatures(chunk, branchEnds, random);
        placeDecorations(chunk, pos, random);
        addSurfaceLayer(chunk, pos);

        return CompletableFuture.completedFuture(chunk);
    }

    private void fillWithStone(ChunkAccess chunk, BlockPos.MutableBlockPos pos, Random random) {
        BlockState stone = Blocks.BLACKSTONE.defaultBlockState();
        BlockState cobble = Blocks.COBBLESTONE.defaultBlockState();
        BlockState mossy = Blocks.MOSSY_COBBLESTONE.defaultBlockState();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = getMinY(); y < getMinY() + getGenDepth(); y++) {
                    float r = random.nextFloat();
                    BlockState state = r < 0.05f ? cobble : r < 0.1f ? mossy : stone;
                    chunk.setBlockState(pos.set(x, y, z), state, 0); // No updates during fill
                }
            }
        }
    }

    private void generateOreVeins(ChunkAccess chunk, BlockPos.MutableBlockPos pos, Random random) {
        int numVeins = 2 + random.nextInt(4);
        for (int i = 0; i < numVeins; i++) {
            int x = random.nextInt(16);
            int z = random.nextInt(16);
            int y = random.nextInt(getGenDepth()) + getMinY();
            pos.set(x, y, z);
            if (chunk.getBlockState(pos).is(Blocks.STONE)) {
                BlockState ore = getRandomOre(random);
                int veinSize = 3 + random.nextInt(3);
                List<BlockPos> toReplace = new ArrayList<>();
                toReplace.add(pos.immutable());
                for (int j = 0; j < veinSize - 1; j++) {
                    BlockPos current = toReplace.get(random.nextInt(toReplace.size()));
                    Direction dir = Direction.values()[random.nextInt(6)];
                    BlockPos neighbor = current.relative(dir);
                    if (neighbor.getY() >= getMinY() && neighbor.getY() < getMinY() + getGenDepth() &&
                            chunk.getBlockState(neighbor).is(Blocks.STONE)) {
                        toReplace.add(neighbor);
                    }
                }
                for (BlockPos orePos : toReplace) {
                    chunk.setBlockState(orePos, ore, 0);
                }
            }
        }
    }

    private BlockState getRandomOre(Random random) {
        float r = random.nextFloat();
        if (r < 0.5f) return Blocks.COAL_ORE.defaultBlockState();
        else if (r < 0.8f) return Blocks.IRON_ORE.defaultBlockState();
        else if (r < 0.95f) return Blocks.GOLD_ORE.defaultBlockState();
        else return Blocks.DIAMOND_ORE.defaultBlockState();
    }

    private void generateMainCorridor(ChunkAccess chunk, BlockPos.MutableBlockPos pos, BlockState air) {
        int centerX = 8;
        int centerZ = 8;
        int halfWidth = corridorWidth / 2;
        int xStart = centerX - halfWidth;
        int xEnd = xStart + corridorWidth;
        int zStart = centerZ - halfWidth;
        int zEnd = zStart + corridorWidth;
        BlockState rail = Blocks.RAIL.defaultBlockState();
        Random random = new Random(); // For floor variation

        // Carve corridor air and set floor
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                if ((x >= xStart && x < xEnd) || (z >= zStart && z < zEnd)) {
                    for (int y = corridorY; y < corridorY + corridorHeight; y++) {
                        chunk.setBlockState(pos.set(x, y, z), air, 0);
                    }
                    // Vary the floor
                    float r = random.nextFloat();
                    BlockState floorState = r < 0.05f ? Blocks.COBBLESTONE.defaultBlockState() :
                            r < 0.1f ? Blocks.MOSSY_COBBLESTONE.defaultBlockState() :
                                    Blocks.BLACKSTONE.defaultBlockState();
                    chunk.setBlockState(pos.set(x, corridorY - 1, z), floorState, 0);
                }
            }
        }

        // Place rails on floor
        for (int x = xStart; x < xEnd; x++) {
            // Ensure solid block beneath rails
            chunk.setBlockState(pos.set(x, corridorY - 1, centerZ), Blocks.BLACKSTONE.defaultBlockState(), 0);
            chunk.setBlockState(pos.set(x, corridorY, centerZ), rail, 0);
        }
        for (int z = zStart; z < zEnd; z++) {
            if (z != centerZ) { // Avoid overwriting intersection
                chunk.setBlockState(pos.set(centerX, corridorY - 1, z), Blocks.BLACKSTONE.defaultBlockState(), 0);
                chunk.setBlockState(pos.set(centerX, corridorY, z), rail, 0);
            }
        }
    }

    private void placeWoodenSupports(ChunkAccess chunk, BlockPos.MutableBlockPos pos) {
        BlockState fence = Blocks.OAK_FENCE.defaultBlockState();
        int halfWidth = corridorWidth / 2;
        int zMin = 8 - halfWidth; // e.g., 7 for width=3
        int zMax = 8 + halfWidth - 1; // e.g., 9 for width=3
        int xMin = 8 - halfWidth; // e.g., 7 for width=3
        int xMax = 8 + halfWidth - 1; // e.g., 9 for width=3

        // Place supports every 4 blocks along corridors, ensuring adjacency to walls
        for (int x = 0; x < 16; x += 4) {
            for (int y = corridorY; y < corridorY + corridorHeight; y++) {
                pos.set(x, y, zMin);
                if (chunk.getBlockState(pos.north()).isSolid() && chunk.getBlockState(pos).isAir()) {
                    chunk.setBlockState(pos, fence, 0);
                }
                pos.set(x, y, zMax);
                if (chunk.getBlockState(pos.south()).isSolid() && chunk.getBlockState(pos).isAir()) {
                    chunk.setBlockState(pos, fence, 0);
                }
            }
        }
        for (int z = 0; z < 16; z += 4) {
            for (int y = corridorY; y < corridorY + corridorHeight; y++) {
                pos.set(xMin, y, z);
                if (chunk.getBlockState(pos.west()).isSolid() && chunk.getBlockState(pos).isAir()) {
                    chunk.setBlockState(pos, fence, 0);
                }
                pos.set(xMax, y, z);
                if (chunk.getBlockState(pos.east()).isSolid() && chunk.getBlockState(pos).isAir()) {
                    chunk.setBlockState(pos, fence, 0);
                }
            }
        }
    }

    private void generateBranches(ChunkAccess chunk, BlockPos.MutableBlockPos pos, BlockState air, Random random, List<BlockPos> branchEnds) {
        int centerX = 8;
        int centerZ = 8;
        BlockState rail = Blocks.RAIL.defaultBlockState();

        for (int i = 0; i < 2; i++) {
            int branchX = centerX + (random.nextBoolean() ? -random.nextInt(corridorWidth / 2) : random.nextInt(corridorWidth / 2));
            int branchZ = centerZ + (random.nextBoolean() ? -random.nextInt(corridorWidth / 2) : random.nextInt(corridorWidth / 2));
            int branchY = corridorY; // Fixed to main corridor floor
            boolean isHorizontal = random.nextBoolean();
            boolean goPositive = random.nextBoolean();
            int branchLength = 5 + random.nextInt(6);

            for (int j = 0; j < branchLength; j++) {
                int offset = goPositive ? j : -j;
                int offsetX = isHorizontal ? offset : 0;
                int offsetZ = isHorizontal ? 0 : offset;
                int x = branchX + offsetX;
                int z = branchZ + offsetZ;
                if (x >= 0 && x < 16 && z >= 0 && z < 16) {
                    for (int y = branchY; y < branchY + 3; y++) {
                        chunk.setBlockState(pos.set(x, y, z), air, 0);
                    }
                    chunk.setBlockState(pos.set(x, branchY, z), rail, 0);
                }
            }
            int endX = branchX + (isHorizontal ? (goPositive ? branchLength - 1 : -(branchLength - 1)) : 0);
            int endZ = branchZ + (isHorizontal ? 0 : (goPositive ? branchLength - 1 : -(branchLength - 1)));
            if (endX >= 0 && endX < 16 && endZ >= 0 && endZ < 16) {
                branchEnds.add(new BlockPos(endX, branchY, endZ));
            }
        }
    }

    private void generateRoom(ChunkAccess chunk, BlockPos.MutableBlockPos pos, BlockState air, Random random) {
        int roomSize = 5;
        int halfSize = roomSize / 2;
        for (int dx = -halfSize; dx <= halfSize; dx++) {
            for (int dz = -halfSize; dz <= halfSize; dz++) {
                for (int dy = 0; dy < 3; dy++) {
                    int x = 8 + dx;
                    int z = 8 + dz;
                    int y = corridorY + dy;
                    if (x >= 0 && x < 16 && z >= 0 && z < 16) {
                        chunk.setBlockState(pos.set(x, y, z), air, 0);
                    }
                }
            }
        }
    }

    private void placeTorches(ChunkAccess chunk, BlockPos.MutableBlockPos pos, Random random) {
        BlockState torchNorth = Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.NORTH);
        BlockState torchSouth = Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.SOUTH);
        BlockState torchEast = Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.EAST);
        BlockState torchWest = Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.WEST);
        int y = corridorY + 1;
        int[] torchX = {2, 6, 10, 14};
        int zMinWall = 8 - corridorWidth / 2 - 1;
        int zMaxWall = 8 + corridorWidth / 2;
        int xMinWall = 8 - corridorWidth / 2 - 1;
        int xMaxWall = 8 + corridorWidth / 2;

        for (int x : torchX) {
            if (chunk.getBlockState(pos.set(x, y, zMinWall)).isSolid()) {
                chunk.setBlockState(pos.set(x, y, zMinWall), torchSouth, 3); // Update for torch
            }
            if (chunk.getBlockState(pos.set(x, y, zMaxWall)).isSolid()) {
                chunk.setBlockState(pos.set(x, y, zMaxWall), torchNorth, 3); // Update for torch
            }
        }
        for (int z : torchX) {
            if (chunk.getBlockState(pos.set(xMinWall, y, z)).isSolid()) {
                chunk.setBlockState(pos.set(xMinWall, y, z), torchEast, 3); // Update for torch
            }
            if (chunk.getBlockState(pos.set(xMaxWall, y, z)).isSolid()) {
                chunk.setBlockState(pos.set(xMaxWall, y, z), torchWest, 3); // Update for torch
            }
        }
    }

    private void placeBranchEndFeatures(ChunkAccess chunk, List<BlockPos> branchEnds, Random random) {
        for (BlockPos endPos : branchEnds) {
            float r = random.nextFloat();
            if (r < 0.1f) {
                chunk.setBlockState(endPos, Blocks.WATER.defaultBlockState(), 3); // Update for water flow
            }
        }
    }

    private void placeDecorations(ChunkAccess chunk, BlockPos.MutableBlockPos pos, Random random) {
        BlockState cobweb = Blocks.COBWEB.defaultBlockState();
        BlockState mushroom = Blocks.BROWN_MUSHROOM.defaultBlockState();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = corridorY; y < corridorY + corridorHeight; y++) {
                    pos.set(x, y, z);
                    if (chunk.getBlockState(pos).isAir()) {
                        // Place cobwebs near walls or ceiling
                        boolean nearWall = false;
                        for (Direction dir : Direction.values()) {
                            if (chunk.getBlockState(pos.move(dir)).isSolid()) {
                                nearWall = true;
                                pos.move(dir.getOpposite()); // Reset position
                                break;
                            }
                            pos.move(dir.getOpposite()); // Reset position
                        }
                        if (nearWall && y > corridorY && random.nextFloat() < 0.03f) { // Reduced to ceiling/wall only
                            chunk.setBlockState(pos, cobweb, 3);
                        }
                        // Mushrooms only on floor
                        else if (y == corridorY && random.nextFloat() < 0.02f) {
                            chunk.setBlockState(pos, mushroom, 3);
                        }
                    }
                }
            }
        }
    }

    private void addSurfaceLayer(ChunkAccess chunk, BlockPos.MutableBlockPos pos) {
        BlockState grass = Blocks.GRASS_BLOCK.defaultBlockState();
        BlockState dirt = Blocks.DIRT.defaultBlockState();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int surfaceY = getMinY() + getGenDepth() - 1;
                chunk.setBlockState(pos.set(x, surfaceY, z), grass, 3);
                chunk.setBlockState(pos.set(x, surfaceY - 1, z), dirt, 3);
            }
        }
    }

    @Override
    public int getBaseHeight(int x, int z, net.minecraft.world.level.levelgen.Heightmap.Types type,
                             LevelHeightAccessor accessor, RandomState randomState) {
        return corridorY;
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor accessor, RandomState randomState) {
        BlockState[] states = new BlockState[getGenDepth()];
        int minY = getMinY();
        Random random = new Random(x ^ z);
        for (int y = 0; y < getGenDepth(); y++) {
            int worldY = minY + y;
            if (worldY >= corridorY && worldY < corridorY + corridorHeight) {
                states[y] = Blocks.AIR.defaultBlockState();
            } else {
                float r = random.nextFloat();
                states[y] = r < 0.05f ? Blocks.COBBLESTONE.defaultBlockState() :
                        r < 0.1f ? Blocks.MOSSY_COBBLESTONE.defaultBlockState() :
                                this.settings.value().defaultBlock();
            }
        }
        return new NoiseColumn(minY, states);
    }

    @Override
    public void addDebugScreenInfo(List<String> list, RandomState randomState, BlockPos pos) {
        list.add("MineShaft Generator");
        list.add("Corridor Y: " + corridorY);
        list.add("Corridor Width: " + corridorWidth);
        list.add("Corridor Height: " + corridorHeight);
        list.add("Chunk Pos: " + pos.getX() / 16 + ", " + pos.getZ() / 16);
    }
}