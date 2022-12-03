package floottymod.floottymod.hacks.render;

import com.mojang.blaze3d.systems.RenderSystem;
import floottymod.floottymod.events.RenderListener;
import floottymod.floottymod.events.TickListener;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.settings.BoolSetting;
import floottymod.floottymod.settings.SliderSetting;
import floottymod.floottymod.util.ChunkUtils;
import floottymod.floottymod.util.RenderUtils;
import floottymod.floottymod.util.chestesp.ChestEspBlockGroup;
import floottymod.floottymod.util.chestesp.ChestEspEntityGroup;
import floottymod.floottymod.util.chestesp.ChestEspGroup;
import floottymod.floottymod.util.chestesp.ChestEspRenderer;
import net.minecraft.block.entity.*;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.entity.vehicle.HopperMinecartEntity;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChestEsp extends Hack implements TickListener, RenderListener {
    private SliderSetting range = new SliderSetting("Range", 12, 0, 32, 1);
    private BoolSetting renderChests = new BoolSetting("Render chests", true);
    private BoolSetting renderTrapChests = new BoolSetting("Render trapped chests", true);
    private BoolSetting renderEnderChests = new BoolSetting("Render ender chests", true);
    private BoolSetting renderBarrels = new BoolSetting("Render barrels", true);
    private BoolSetting renderShulkers = new BoolSetting("Render shulkers", true);
    private BoolSetting renderHoppers = new BoolSetting("Render hoppers", true);
    private BoolSetting renderDroppers = new BoolSetting("Render droppers", true);
    private BoolSetting renderDispensers = new BoolSetting("Render dispensers", true);
    private BoolSetting renderFurnaces = new BoolSetting("Render furnaces", true);
    private BoolSetting renderChestCarts = new BoolSetting("Render chest carts", true);
    private BoolSetting renderHopperCarts = new BoolSetting("Render hopper carts", true);
    private BoolSetting renderChestBoats = new BoolSetting("Render chest boats", true);

    private final ChestEspBlockGroup basicChests = new ChestEspBlockGroup(new float[] { .98f, .5f, .11f, 1 }, renderChests);
    private final ChestEspBlockGroup trapChests = new ChestEspBlockGroup(new float[] { 1, .07f, .07f, 1 }, renderTrapChests);
    private final ChestEspBlockGroup enderChests = new ChestEspBlockGroup(new float[] { 0, .31f, .22f, 1 }, renderEnderChests);
    private final ChestEspBlockGroup barrels = new ChestEspBlockGroup(new float[] { .31f, .16f, 0, 1 }, renderBarrels);
    private final ChestEspBlockGroup shulkerBoxes = new ChestEspBlockGroup(new float[] { .76f, 0, 1, 1 }, renderShulkers);
    private final ChestEspBlockGroup hoppers = new ChestEspBlockGroup(new float[] { .31f, .31f, .31f, 1 }, renderHoppers);
    private final ChestEspBlockGroup droppers = new ChestEspBlockGroup(new float[] { .31f, .31f, .31f, 1 }, renderDroppers);
    private final ChestEspBlockGroup dispensers = new ChestEspBlockGroup(new float[] { .31f, .31f, .31f, 1 }, renderDispensers);
    private final ChestEspBlockGroup furnaces = new ChestEspBlockGroup(new float[] { .31f, .31f, .31f, 1 }, renderFurnaces);
    private final ChestEspEntityGroup chestCarts = new ChestEspEntityGroup(new float[]{ .98f, .5f, .11f, 1 }, renderChestCarts);
    private final ChestEspEntityGroup hopperCarts = new ChestEspEntityGroup(new float[]{ .31f, .31f, .31f, 1 }, renderHopperCarts);
    private final ChestEspEntityGroup chestBoats = new ChestEspEntityGroup(new float[]{ .98f, .5f, .11f, 1 }, renderChestBoats);

    private final List<ChestEspGroup> groups = Arrays.asList(basicChests, trapChests, enderChests, barrels, shulkerBoxes, hoppers, droppers, dispensers, furnaces);
    private final List<ChestEspEntityGroup> entityGroups = Arrays.asList(chestCarts, chestBoats, hopperCarts);

    public ChestEsp() {
        super("ChestEsp", Category.RENDER);
        addSettings(range);
        for(ChestEspGroup g : groups) addSetting(g.getSetting());
        for(ChestEspGroup g : entityGroups) addSetting(g.getSetting());
    }

    @Override
    public void onEnable() {
        EVENTS.add(RenderListener.class, this);
        EVENTS.add(TickListener.class, this);

        ChestEspRenderer.prepareBuffers();
    }

    @Override
    public void onDisable() {
        EVENTS.remove(RenderListener.class, this);
        EVENTS.remove(TickListener.class, this);

        groups.forEach(ChestEspGroup::clear);
        ChestEspRenderer.closeBuffers();
    }

    @Override
    public void onTick() {
        if(MC.player == null) return;

        groups.forEach(ChestEspGroup::clear);
        entityGroups.forEach(ChestEspGroup::clear);

        ArrayList<BlockEntity> blockEntities = ChunkUtils.getLoadedBlockEntities(range.getValueInt() == 0 ? MC.options.getClampedViewDistance() : range.getValueInt()).collect(Collectors.toCollection(ArrayList::new));

        for(BlockEntity blockEntity : blockEntities) {
            if(blockEntity instanceof TrappedChestBlockEntity) trapChests.add(blockEntity);
            else if(blockEntity instanceof ChestBlockEntity) basicChests.add(blockEntity);
            else if(blockEntity instanceof EnderChestBlockEntity) enderChests.add(blockEntity);
            else if(blockEntity instanceof ShulkerBoxBlockEntity) shulkerBoxes.add(blockEntity);
            else if(blockEntity instanceof BarrelBlockEntity) barrels.add(blockEntity);
            else if(blockEntity instanceof HopperBlockEntity) hoppers.add(blockEntity);
            else if(blockEntity instanceof DropperBlockEntity) droppers.add(blockEntity);
            else if(blockEntity instanceof DispenserBlockEntity) dispensers.add(blockEntity);
            else if(blockEntity instanceof AbstractFurnaceBlockEntity) furnaces.add(blockEntity);
        }

        for(Entity entity : MC.world.getEntities()) {
            if(entity instanceof ChestMinecartEntity) chestCarts.add(entity);
            else if(entity instanceof HopperMinecartEntity) hopperCarts.add(entity);
            else if(entity instanceof ChestBoatEntity) chestBoats.add(entity);
        }
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

        entityGroups.stream().filter(ChestEspGroup::isEnabled).forEach(g -> g.updateBoxes(partialTicks));

        ChestEspRenderer espRenderer = new ChestEspRenderer(matrixStack);

        RenderSystem.setShader(GameRenderer::getPositionShader);
        groups.stream().filter(ChestEspGroup::isEnabled).forEach(espRenderer::renderBoxes);
        entityGroups.stream().filter(ChestEspGroup::isEnabled).forEach(espRenderer::renderBoxes);

        matrixStack.pop();

        // GL resets
        RenderSystem.setShaderColor(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }
}
