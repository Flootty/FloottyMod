package floottymod.floottymod.util;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public enum BlockVertexCompiler {
    ;
    public static Callable<ArrayList<int[]>> createTask(HashSet<BlockPos> blocks) {
        return () -> blocks.parallelStream()
                .flatMap(pos -> getVertices(pos, blocks).stream())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static Callable<ArrayList<int[]>> createTask(HashSet<BlockPos> blocks, int regionX, int regionZ) {
        return () -> blocks.parallelStream()
                .flatMap(pos -> getVertices(pos, blocks).stream())
                .map(v -> applyRegionOffset(v, regionX, regionZ))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static int[] applyRegionOffset(int[] vertex, int regionX, int regionZ) {
        vertex[0] -= regionX;
        vertex[2] -= regionZ;
        return vertex;
    }

    private static ArrayList<int[]> getVertices(BlockPos pos, HashSet<BlockPos> matchingBlocks) {
        ArrayList<int[]> vertices = new ArrayList<>();

        if(!matchingBlocks.contains(pos.down())) {
            vertices.add(getVertex(pos, 0, 0, 0));
            vertices.add(getVertex(pos, 1, 0, 0));
            vertices.add(getVertex(pos, 1, 0, 1));
            vertices.add(getVertex(pos, 0, 0, 1));
        }

        if(!matchingBlocks.contains(pos.up())) {
            vertices.add(getVertex(pos, 0, 1, 0));
            vertices.add(getVertex(pos, 0, 1, 1));
            vertices.add(getVertex(pos, 1, 1, 1));
            vertices.add(getVertex(pos, 1, 1, 0));
        }

        if(!matchingBlocks.contains(pos.north())) {
            vertices.add(getVertex(pos, 0, 0, 0));
            vertices.add(getVertex(pos, 0, 1, 0));
            vertices.add(getVertex(pos, 1, 1, 0));
            vertices.add(getVertex(pos, 1, 0, 0));
        }

        if(!matchingBlocks.contains(pos.east())) {
            vertices.add(getVertex(pos, 1, 0, 0));
            vertices.add(getVertex(pos, 1, 1, 0));
            vertices.add(getVertex(pos, 1, 1, 1));
            vertices.add(getVertex(pos, 1, 0, 1));
        }

        if(!matchingBlocks.contains(pos.south())) {
            vertices.add(getVertex(pos, 0, 0, 1));
            vertices.add(getVertex(pos, 1, 0, 1));
            vertices.add(getVertex(pos, 1, 1, 1));
            vertices.add(getVertex(pos, 0, 1, 1));
        }

        if(!matchingBlocks.contains(pos.west())) {
            vertices.add(getVertex(pos, 0, 0, 0));
            vertices.add(getVertex(pos, 0, 0, 1));
            vertices.add(getVertex(pos, 0, 1, 1));
            vertices.add(getVertex(pos, 0, 1, 0));
        }

        return vertices;
    }

    private static int[] getVertex(BlockPos pos, int x, int y, int z) {
        return new int[]{pos.getX() + x, pos.getY() + y, pos.getZ() + z};
    }
}
