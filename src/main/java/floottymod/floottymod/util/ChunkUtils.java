package floottymod.floottymod.util;

import floottymod.floottymod.FloottyMod;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.Objects;
import java.util.stream.Stream;

public enum ChunkUtils {
    ;
    private static final MinecraftClient MC = FloottyMod.MC;

    public static Stream<BlockEntity> getLoadedBlockEntities(int maxDistance) {
        return getLoadedChunks(maxDistance).flatMap(chunk -> chunk.getBlockEntities().values().stream());
    }

    public static Stream<WorldChunk> getLoadedChunks(int maxDistance) {
        int radius = Math.max(2, maxDistance);
        int diameter = radius * 2 + 1;

        ChunkPos center = MC.player.getChunkPos();
        ChunkPos min = new ChunkPos(center.x - radius, center.z - radius);
        ChunkPos max = new ChunkPos(center.x + radius, center.z + radius);

        Stream<WorldChunk> stream = Stream.<ChunkPos> iterate(min, pos -> {
            int x = pos.x;
            int z = pos.z;

            x++;

            if(x > max.x) {
                x = min.x;
                z++;
            }

            if(z > max.z) throw new IllegalStateException("Stream limit didn't work.");

            return new ChunkPos(x, z);

        }).limit(diameter * diameter)
          .filter(c -> MC.world.isChunkLoaded(c.x, c.z))
          .map(c -> MC.world.getChunk(c.x, c.z))
          .filter(Objects::nonNull);

        return stream;
    }
}
