package floottymod.floottymod.hacks.render;

import com.mojang.blaze3d.systems.RenderSystem;
import floottymod.floottymod.events.PacketInputListener;
import floottymod.floottymod.events.RenderListener;
import floottymod.floottymod.events.TickListener;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.settings.ModeSetting;
import floottymod.floottymod.settings.SliderSetting;
import floottymod.floottymod.util.*;
import floottymod.floottymod.util.search.ChunkSearcher;
import floottymod.floottymod.util.search.SearchArea;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.chunk.Chunk;
import org.lwjgl.opengl.GL11;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class OreEsp extends Hack implements TickListener, PacketInputListener, RenderListener {
    private final ModeSetting block = new ModeSetting("Block", "Diamond", "Coal", "Copper", "Lapis", "Iron", "Redstone", "Diamond", "Gold", "Emerald", "Quartz", "Ancient debris");
    private final ModeSetting area = new ModeSetting("Area", "5x5", "3x3", "5x5", "9x9", "11x11", "13x13", "15x15", "17x17", "19x19", "21x21", "23x23", "25x25", "27x27", "29x29", "31x31", "33x33");
    private final SliderSetting limit = new SliderSetting("Limit", 10000, 1000, 1000000, 1000);
    /*private final BoolSetting renderCoalOre = new BoolSetting("Coal", false);
    private final BoolSetting renderCopperOre = new BoolSetting("Copper", false);
    private final BoolSetting renderLapisOre = new BoolSetting("Lapis", false);
    private final BoolSetting renderIronOre = new BoolSetting("Iron", false);
    private final BoolSetting renderRedstoneOre = new BoolSetting("Redstone", false);
    private final BoolSetting renderDiamondOre = new BoolSetting("Diamond", false);
    private final BoolSetting renderGoldOre = new BoolSetting("Gold", false);
    private final BoolSetting renderEmeraldOre = new BoolSetting("Emerald", false);
    private final BoolSetting renderQuartzOre = new BoolSetting("Quartz", false);
    private final BoolSetting renderAncientDebris = new BoolSetting("Ancient debris", false);*/

    private int prevLimit;
    private boolean notify;

    private final HashMap<Chunk, ChunkSearcher> searchers = new HashMap<>();
    private final Set<Chunk> chunksToUpdate = Collections.synchronizedSet(new HashSet<>());
    private ExecutorService pool1;

    private ForkJoinPool pool2;
    private ForkJoinTask<HashSet<BlockPos>> getMatchingBlocksTask;
    private ForkJoinTask<ArrayList<int[]>> compileVerticesTask;

    private VertexBuffer vertexBuffer;
    private boolean bufferUpToDate;

    private List<Block> currentBlocks;

    public OreEsp() {
        super("OreEsp", Category.RENDER);
        //addSettings(area, limit, renderCoalOre, renderCopperOre, renderLapisOre, renderIronOre, renderRedstoneOre, renderDiamondOre, renderGoldOre, renderEmeraldOre, renderQuartzOre, renderAncientDebris);
        addSettings(block, area, limit);
    }

    @Override
    public void onEnable() {
        prevLimit = limit.getValueInt();
        notify = true;

        pool1 = MinPriorityThreadFactory.newFixedThreadPool();
        pool2 = new ForkJoinPool();

        bufferUpToDate = false;

        EVENTS.add(TickListener.class, this);
        EVENTS.add(PacketInputListener.class, this);
        EVENTS.add(RenderListener.class, this);
    }

    @Override
    public void onDisable() {
        EVENTS.remove(TickListener.class, this);
        EVENTS.remove(PacketInputListener.class, this);
        EVENTS.remove(RenderListener.class, this);

        if(pool1 == null) return;

        stopPool2Tasks();
        pool1.shutdownNow();
        pool2.shutdownNow();

        if(vertexBuffer != null) {
            vertexBuffer.close();
            vertexBuffer = null;
        }

        chunksToUpdate.clear();
    }

    @Override
    public void onReceivePacket(PacketInputEvent event) {
        ClientPlayerEntity player = MC.player;
        ClientWorld world = MC.world;
        if(player == null || world == null) return;

        Packet<?> packet = event.getPacket();
        Chunk chunk;

        if(packet instanceof BlockUpdateS2CPacket change) {
            BlockPos pos = change.getPos();
            chunk = world.getChunk(pos);

        } else if(packet instanceof ChunkDeltaUpdateS2CPacket change) {
            ArrayList<BlockPos> changedBlocks = new ArrayList<>();
            change.visitUpdates((pos, state) -> changedBlocks.add(pos));
            if(changedBlocks.isEmpty()) return;

            chunk = world.getChunk(changedBlocks.get(0));

        } else if(packet instanceof ChunkDataS2CPacket chunkData)
            chunk = world.getChunk(chunkData.getX(), chunkData.getZ());
        else return;

        chunksToUpdate.add(chunk);
    }

    @Override
    public void onTick() {
        if(MC.player == null) return;

        currentBlocks = getBlocks();
        BlockPos eyesPos = new BlockPos(RotationUtils.getEyesPos());

        ChunkPos center = MC.player.getChunkPos();
        int dimensionId = MC.world.getRegistryKey().toString().hashCode();

        addSearchersInRange(center, currentBlocks, dimensionId);
        removeSearchersOutOfRange(center);
        replaceSearchersWithDifferences(currentBlocks, dimensionId);
        replaceSearchersWithChunkUpdate(currentBlocks, dimensionId);

        if(!areAllChunkSearchersDone()) return;

        checkIfLimitChanged();

        if(getMatchingBlocksTask == null) startGetMatchingBlocksTask(eyesPos);

        if(!getMatchingBlocksTask.isDone()) return;

        if(compileVerticesTask == null) startCompileVerticesTask();

        if(!compileVerticesTask.isDone()) return;

        if(!bufferUpToDate) setBufferFromTask();
    }

    @Override
    public void onRender(MatrixStack matrixStack, float partialTicks) {
        if(MC.player == null) return;

        // GL settings
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        matrixStack.push();
        RenderUtils.applyRegionalRenderOffset(matrixStack);

        //float[] rainbow = RenderUtils.getRainbowColor();
        //RenderSystem.setShaderColor(rainbow[0], rainbow[1], rainbow[2], 0.5F);

        float[] color = getColor();
        RenderSystem.setShaderColor(color[0], color[1], color[2], .5f);

        RenderSystem.setShader(GameRenderer::getPositionShader);

        if(vertexBuffer != null) {
            Matrix4f viewMatrix = matrixStack.peek().getPositionMatrix();
            Matrix4f projMatrix = RenderSystem.getProjectionMatrix();
            Shader shader = RenderSystem.getShader();
            vertexBuffer.bind();
            vertexBuffer.draw(viewMatrix, projMatrix, shader);
            VertexBuffer.unbind();
        }

        matrixStack.pop();

        // GL resets
        RenderSystem.setShaderColor(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    private void addSearchersInRange(ChunkPos center, List<Block> blocks, int dimensionId) {
        ArrayList<Chunk> chunksInRange = getArea(area.getMode()).getChunksInRange(center);

        for(Chunk chunk : chunksInRange) {
            if(searchers.containsKey(chunk)) continue;

            addSearcher(chunk, blocks, dimensionId);
        }
    }

    private void removeSearchersOutOfRange(ChunkPos center) {
        for(ChunkSearcher searcher : new ArrayList<>(searchers.values())) {
            ChunkPos searcherPos = searcher.getChunk().getPos();
            if(getArea(area.getMode()).isInRange(searcherPos, center)) continue;

            removeSearcher(searcher);
        }
    }

    private void replaceSearchersWithDifferences(List<Block> currentBlocks, int dimensionId) {
        for(ChunkSearcher oldSearcher : new ArrayList<>(searchers.values())) {
            if(currentBlocks.equals(oldSearcher.getBlocks()) && dimensionId == oldSearcher.getDimensionId()) continue;

            removeSearcher(oldSearcher);
            addSearcher(oldSearcher.getChunk(), currentBlocks, dimensionId);
        }
    }

    private void replaceSearchersWithChunkUpdate(List<Block> currentBlocks, int dimensionId) {
        synchronized(chunksToUpdate) {
            if(chunksToUpdate.isEmpty()) return;

            for(Iterator<Chunk> itr = chunksToUpdate.iterator(); itr.hasNext();) {
                Chunk chunk = itr.next();

                ChunkSearcher oldSearcher = searchers.get(chunk);
                if(oldSearcher == null) continue;

                removeSearcher(oldSearcher);
                addSearcher(chunk, currentBlocks, dimensionId);
                itr.remove();
            }
        }
    }

    private void addSearcher(Chunk chunk, List<Block> blocks, int dimensionId) {
        stopPool2Tasks();

        ChunkSearcher searcher = new ChunkSearcher(chunk, blocks, dimensionId);
        searchers.put(chunk, searcher);
        searcher.startSearching(pool1);
    }

    private void removeSearcher(ChunkSearcher searcher) {
        stopPool2Tasks();

        searchers.remove(searcher.getChunk());
        searcher.cancelSearching();
    }

    private void stopPool2Tasks() {
        if(getMatchingBlocksTask != null) {
            getMatchingBlocksTask.cancel(true);
            getMatchingBlocksTask = null;
        }

        if(compileVerticesTask != null) {
            compileVerticesTask.cancel(true);
            compileVerticesTask = null;
        }

        bufferUpToDate = false;
    }

    private boolean areAllChunkSearchersDone() {
        for(ChunkSearcher searcher : searchers.values()) if(searcher.getStatus() != ChunkSearcher.Status.DONE) return false;

        return true;
    }

    private void checkIfLimitChanged() {
        if(limit.getValueInt() != prevLimit) {
            stopPool2Tasks();
            notify = true;
            prevLimit = limit.getValueInt();
        }
    }

    private void startGetMatchingBlocksTask(BlockPos eyesPos) {
        int maxBlocks = (int)Math.pow(10, limit.getValueInt());

        Callable<HashSet<BlockPos>> task = () -> searchers.values()
                .parallelStream()
                .flatMap(searcher -> searcher.getMatchingBlocks().stream())
                .sorted(Comparator.comparingInt(eyesPos::getManhattanDistance))
                .limit(maxBlocks).collect(Collectors.toCollection(HashSet::new));

        getMatchingBlocksTask = pool2.submit(task);
    }

    private HashSet<BlockPos> getMatchingBlocksFromTask() {
        HashSet<BlockPos> matchingBlocks = new HashSet<>();

        try {
            matchingBlocks = getMatchingBlocksTask.get();

        } catch(InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        int maxBlocks = (int)Math.pow(10, limit.getValueInt());

        if(matchingBlocks.size() < maxBlocks) notify = true;
        else if(notify) {
            ChatUtils.warning("Search found \u00a7lA LOT\u00a7r of blocks!" + " To prevent lag, it will only show the closest \u00a76" + String.valueOf(limit.getValue()) + "\u00a7r results.");
            notify = false;
        }

        return matchingBlocks;
    }

    private void startCompileVerticesTask() {
        HashSet<BlockPos> matchingBlocks = getMatchingBlocksFromTask();

        BlockPos camPos = RenderUtils.getCameraBlockPos();
        int regionX = (camPos.getX() >> 9) * 512;
        int regionZ = (camPos.getZ() >> 9) * 512;

        Callable<ArrayList<int[]>> task = BlockVertexCompiler.createTask(matchingBlocks, regionX, regionZ);

        compileVerticesTask = pool2.submit(task);
    }

    private void setBufferFromTask() {
        ArrayList<int[]> vertices = getVerticesFromTask();

        if(vertexBuffer != null) vertexBuffer.close();

        vertexBuffer = new VertexBuffer();

        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

        for(int[] vertex : vertices) bufferBuilder.vertex(vertex[0], vertex[1], vertex[2]).next();

        BufferBuilder.BuiltBuffer buffer = bufferBuilder.end();

        vertexBuffer.bind();
        vertexBuffer.upload(buffer);
        VertexBuffer.unbind();

        bufferUpToDate = true;
    }

    private ArrayList<int[]> getVerticesFromTask() {
        try {
            return compileVerticesTask.get();
        } catch(InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private SearchArea getArea(String areaStr) {
        if(Objects.equals(areaStr, "3x3")) return SearchArea.D3;
        else if(Objects.equals(areaStr, "5x5")) return SearchArea.D5;
        else if(Objects.equals(areaStr, "7x7")) return SearchArea.D7;
        else if(Objects.equals(areaStr, "9x9")) return SearchArea.D9;
        else if(Objects.equals(areaStr, "11x11")) return SearchArea.D11;
        else if(Objects.equals(areaStr, "13x13")) return SearchArea.D13;
        else if(Objects.equals(areaStr, "15x15")) return SearchArea.D15;
        else if(Objects.equals(areaStr, "17x17")) return SearchArea.D17;
        else if(Objects.equals(areaStr, "19x19")) return SearchArea.D19;
        else if(Objects.equals(areaStr, "21x21")) return SearchArea.D21;
        else if(Objects.equals(areaStr, "23x23")) return SearchArea.D23;
        else if(Objects.equals(areaStr, "25x25")) return SearchArea.D25;
        else if(Objects.equals(areaStr, "27x27")) return SearchArea.D27;
        else if(Objects.equals(areaStr, "29x29")) return SearchArea.D29;
        else if(Objects.equals(areaStr, "31x31")) return SearchArea.D31;
        else if(Objects.equals(areaStr, "33x33")) return SearchArea.D33;
        return null;
    }

    /*private List<Block> getBlocks() {
        List<Block> blocks = new ArrayList<>();
        if(renderCoalOre.isEnabled()) blocks.addAll(Arrays.asList(Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE));
        if(renderCopperOre.isEnabled()) blocks.addAll(Arrays.asList(Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE));
        if(renderLapisOre.isEnabled()) blocks.addAll(Arrays.asList(Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE));
        if(renderIronOre.isEnabled()) blocks.addAll(Arrays.asList(Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE));
        if(renderRedstoneOre.isEnabled()) blocks.addAll(Arrays.asList(Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE));
        if(renderDiamondOre.isEnabled()) blocks.addAll(Arrays.asList(Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE));
        if(renderGoldOre.isEnabled()) blocks.addAll(Arrays.asList(Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.NETHER_GOLD_ORE));
        if(renderEmeraldOre.isEnabled()) blocks.addAll(Arrays.asList(Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE));
        if(renderQuartzOre.isEnabled()) blocks.addAll(Arrays.asList(Blocks.NETHER_QUARTZ_ORE));
        if(renderAncientDebris.isEnabled()) blocks.addAll(Arrays.asList(Blocks.ANCIENT_DEBRIS));
        return blocks;
    }*/

    private List<Block> getBlocks() {
        if(Objects.equals(block.getMode(), "Coal")) return Arrays.asList(Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE);
        else if(Objects.equals(block.getMode(), "Copper")) return Arrays.asList(Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE);
        else if(Objects.equals(block.getMode(), "Lapis")) return Arrays.asList(Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE);
        else if(Objects.equals(block.getMode(), "Iron")) return Arrays.asList(Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE);
        else if(Objects.equals(block.getMode(), "Redstone")) return Arrays.asList(Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE);
        else if(Objects.equals(block.getMode(), "Diamond")) return Arrays.asList(Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE);
        else if(Objects.equals(block.getMode(), "Gold")) return Arrays.asList(Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.NETHER_GOLD_ORE);
        else if(Objects.equals(block.getMode(), "Emerald")) return Arrays.asList(Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE);
        else if(Objects.equals(block.getMode(), "Quartz")) return Collections.singletonList(Blocks.NETHER_QUARTZ_ORE);
        else if(Objects.equals(block.getMode(), "Ancient debris")) return Collections.singletonList(Blocks.ANCIENT_DEBRIS);
        return null;
    }

    private float[] getColor() {
        if(Objects.equals(block.getMode(), "Coal")) return new float[]{ 0, 0, 0, 1 };
        else if(Objects.equals(block.getMode(), "Copper")) return new float[]{ .72f, .41f, .13f, 1};
        else if(Objects.equals(block.getMode(), "Lapis")) return new float[]{ .15f, .38f, .61f, 1 };
        else if(Objects.equals(block.getMode(), "Iron")) return new float[]{ .63f, .62f, .58f, 1 };
        else if(Objects.equals(block.getMode(), "Redstone")) return new float[]{ .95f, .11f, 0, 1};
        else if(Objects.equals(block.getMode(), "Diamond")) return new float[]{ 0, 1, 1, 1};
        else if(Objects.equals(block.getMode(), "Gold")) return new float[]{ 1, .8f, .01f, 1 };
        else if(Objects.equals(block.getMode(), "Emerald")) return new float[]{0, 1, 0, 1};
        else if(Objects.equals(block.getMode(), "Quartz")) return new float[]{ .91f, .87f, .88f, 1};
        else if(Objects.equals(block.getMode(), "Ancient debris")) return new float[]{ 0, .87f, .84f, 1};
        return null;
    }
}
