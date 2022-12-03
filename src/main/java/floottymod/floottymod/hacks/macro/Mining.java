package floottymod.floottymod.hacks.macro;

import com.mojang.blaze3d.systems.RenderSystem;
import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.events.RenderListener;
import floottymod.floottymod.events.TickListener;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.settings.BoolSetting;
import floottymod.floottymod.util.BlockUtils;
import floottymod.floottymod.util.RenderUtils;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Mining extends Hack implements TickListener, RenderListener {
    private BoolSetting coal = new BoolSetting("Coal", true);
    private BoolSetting copper = new BoolSetting("Copper", true);
    private BoolSetting lapis = new BoolSetting("Lapis", true);
    private BoolSetting iron = new BoolSetting("Iron", true);
    private BoolSetting redstone = new BoolSetting("Redstone", true);
    private BoolSetting diamond = new BoolSetting("Diamond", true);
    private BoolSetting gold = new BoolSetting("Gold", true);
    private BoolSetting emerald = new BoolSetting("Emerald", true);
    private BoolSetting quartz = new BoolSetting("Quartz", true);
    private BoolSetting ancient_debris = new BoolSetting("Ancient Debris", true);
    private BoolSetting obsidian = new BoolSetting("Obsidian", true);

    private BlockPos target;
    private boolean breaking;

    public Mining() {
        super("Mining", Category.MACRO);
        addSettings(coal, copper, lapis, iron, redstone, diamond, gold, emerald, quartz, ancient_debris, obsidian);
    }

    @Override
    public void onEnable() {
        EVENTS.add(TickListener.class, this);
        EVENTS.add(RenderListener.class, this);
        breaking = false;
    }

    @Override
    public void onDisable() {
        EVENTS.remove(TickListener.class, this);
        EVENTS.remove(RenderListener.class, this);
    }

    @Override
    public void onTick() {
        if(MC.player == null) return;

        if(!breaking) {
            List<BlockPos> blocks = BlockUtils.getAllInBox(new BlockPos(MC.player.getEyePos().subtract(new Vec3d(5, 5, 5))), new BlockPos(MC.player.getEyePos().add(5, 5, 5)));
            List<BlockPos> ores = new ArrayList<>();
            for(BlockPos b : blocks) {
                if(b.getSquaredDistance(MC.player.getEyePos()) > 20.25) continue;
                if(coal.isEnabled() && (Objects.equals(BlockUtils.getBlock(b), Blocks.COAL_ORE) || Objects.equals(BlockUtils.getBlock(b), Blocks.DEEPSLATE_COAL_ORE))) ores.add(b);
                else if(copper.isEnabled() && (Objects.equals(BlockUtils.getBlock(b), Blocks.COPPER_ORE) || Objects.equals(BlockUtils.getBlock(b), Blocks.DEEPSLATE_COPPER_ORE))) ores.add(b);
                else if(lapis.isEnabled() && (Objects.equals(BlockUtils.getBlock(b), Blocks.LAPIS_ORE) || Objects.equals(BlockUtils.getBlock(b), Blocks.DEEPSLATE_LAPIS_ORE))) ores.add(b);
                else if(iron.isEnabled() && (Objects.equals(BlockUtils.getBlock(b), Blocks.IRON_ORE) || Objects.equals(BlockUtils.getBlock(b), Blocks.DEEPSLATE_IRON_ORE))) ores.add(b);
                else if(redstone.isEnabled() && (Objects.equals(BlockUtils.getBlock(b), Blocks.REDSTONE_ORE) || Objects.equals(BlockUtils.getBlock(b), Blocks.DEEPSLATE_REDSTONE_ORE))) ores.add(b);
                else if(diamond.isEnabled() && (Objects.equals(BlockUtils.getBlock(b), Blocks.DIAMOND_ORE) || Objects.equals(BlockUtils.getBlock(b), Blocks.DEEPSLATE_DIAMOND_ORE))) ores.add(b);
                else if(gold.isEnabled() && (Objects.equals(BlockUtils.getBlock(b), Blocks.GOLD_ORE) || Objects.equals(BlockUtils.getBlock(b), Blocks.DEEPSLATE_GOLD_ORE) || Objects.equals(BlockUtils.getBlock(b), Blocks.NETHER_GOLD_ORE))) ores.add(b);
                else if(emerald.isEnabled() && (Objects.equals(BlockUtils.getBlock(b), Blocks.EMERALD_ORE) || Objects.equals(BlockUtils.getBlock(b), Blocks.DEEPSLATE_EMERALD_ORE))) ores.add(b);
                else if(quartz.isEnabled() && Objects.equals(BlockUtils.getBlock(b), Blocks.NETHER_QUARTZ_ORE)) ores.add(b);
                else if(ancient_debris.isEnabled() && Objects.equals(BlockUtils.getBlock(b), Blocks.ANCIENT_DEBRIS)) ores.add(b);
                else if(obsidian.isEnabled() && Objects.equals(BlockUtils.getBlock(b), Blocks.OBSIDIAN)) ores.add(b);
            }

            target = ores.stream().min(Comparator.comparingDouble(b -> b.getSquaredDistance(MC.player.getEyePos()))).orElse(null);
            if(target != null) breaking = true;
        }

        if(breaking && target != null) {
            MC.interactionManager.updateBlockBreakingProgress(target, Direction.UP);
            MC.player.swingHand(Hand.MAIN_HAND);
            FloottyMod.INSTANCE.getRotationFaker().faceVectorPacket(new Vec3d(target.getX(), target.getY(), target.getZ()));
            if(BlockUtils.getBlock(target) instanceof AirBlock || target.getSquaredDistance(MC.player.getEyePos()) > 20) {
                breaking = false;
                target = null;
            }
        }
    }

    @Override
    public void onRender(MatrixStack matrixStack, float partialTicks) {
        if(target == null) return;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        matrixStack.push();
        RenderUtils.applyRegionalRenderOffset(matrixStack);

        BlockPos camPos = RenderUtils.getCameraBlockPos();
        int regionX = (camPos.getX() >> 9) * 512;
        int regionZ = (camPos.getZ() >> 9) * 512;

        Box box = new Box(BlockPos.ORIGIN);

        matrixStack.translate(target.getX() - regionX, target.getY(), target.getZ() - regionZ);
        matrixStack.scale(1, 1, 1);

        RenderSystem.setShader(GameRenderer::getPositionShader);

        RenderSystem.setShaderColor(1f, 0.67f, 0f, .25f);
        RenderUtils.drawSolidBox(box, matrixStack);

        RenderSystem.setShaderColor(1f, 0.67f, 0f, 1f);
        RenderUtils.drawOutlinedBox(box, matrixStack);

        matrixStack.pop();

        // GL resets
        RenderSystem.setShaderColor(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }
}
