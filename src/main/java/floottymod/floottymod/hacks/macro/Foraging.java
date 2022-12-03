package floottymod.floottymod.hacks.macro;

import com.mojang.blaze3d.systems.RenderSystem;
import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.events.RenderListener;
import floottymod.floottymod.events.TickListener;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.settings.BoolSetting;
import floottymod.floottymod.util.BlockBreaker;
import floottymod.floottymod.util.BlockUtils;
import floottymod.floottymod.util.ChatUtils;
import floottymod.floottymod.util.RenderUtils;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.*;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents.AFTER;

public class Foraging extends Hack implements TickListener, RenderListener {
    private BoolSetting oak = new BoolSetting("Oak", true);
    private BoolSetting spruce = new BoolSetting("Spruce", true);
    private BoolSetting birch = new BoolSetting("Birch", true);
    private BoolSetting jungle = new BoolSetting("Jungle", true);
    private BoolSetting acacia = new BoolSetting("Acacia", true);
    private BoolSetting dark_oak = new BoolSetting("Dark Oak", true);
    private BoolSetting mangrove = new BoolSetting("Mangrove", true);
    private BoolSetting crimson = new BoolSetting("Crimson", true);
    private BoolSetting warped = new BoolSetting("Warped", true);


    private BlockPos target;
    private boolean breaking;

    public Foraging() {
        super("Foraging", Category.MACRO);
        addSettings(oak, spruce, birch, jungle, acacia, dark_oak, mangrove, crimson, warped);
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
            List<BlockPos> logs = new ArrayList<>();
            for(BlockPos b : blocks) {
                if(b.getSquaredDistance(MC.player.getEyePos()) > 20.25) continue;
                if(oak.isEnabled() && Objects.equals(BlockUtils.getBlock(b), Blocks.OAK_LOG)) logs.add(b);
                else if(spruce.isEnabled() && Objects.equals(BlockUtils.getBlock(b), Blocks.SPRUCE_LOG)) logs.add(b);
                else if(birch.isEnabled() && Objects.equals(BlockUtils.getBlock(b), Blocks.BIRCH_LOG)) logs.add(b);
                else if(jungle.isEnabled() && Objects.equals(BlockUtils.getBlock(b), Blocks.JUNGLE_LOG)) logs.add(b);
                else if(acacia.isEnabled() && Objects.equals(BlockUtils.getBlock(b), Blocks.ACACIA_LOG)) logs.add(b);
                else if(dark_oak.isEnabled() && Objects.equals(BlockUtils.getBlock(b), Blocks.DARK_OAK_LOG)) logs.add(b);
                else if(mangrove.isEnabled() && Objects.equals(BlockUtils.getBlock(b), Blocks.MANGROVE_LOG)) logs.add(b);
                else if(crimson.isEnabled() && Objects.equals(BlockUtils.getBlock(b), Blocks.CRIMSON_STEM)) logs.add(b);
                else if(warped.isEnabled() && Objects.equals(BlockUtils.getBlock(b), Blocks.WARPED_STEM)) logs.add(b);
            }

            target = logs.stream().min(Comparator.comparingDouble(b -> b.getSquaredDistance(MC.player.getEyePos()))).orElse(null);
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
