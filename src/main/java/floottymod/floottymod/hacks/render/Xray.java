package floottymod.floottymod.hacks.render;

import floottymod.floottymod.events.*;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.settings.BoolSetting;
import floottymod.floottymod.util.BlockUtils;
import net.minecraft.block.*;

import java.util.Objects;

public class Xray extends Hack implements SetOpaqueCubeListener, GetAmbientOcclusionLightLevelListener, ShouldDrawSideListener, TesselateBlockListener, RenderBlockEntityListener {
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
    private BoolSetting bedrock = new BoolSetting("Bedrock", true);

    public Xray() {
        super("Xray", Category.RENDER);
        addSettings(coal, copper, lapis, iron, redstone, diamond, gold, emerald, quartz, ancient_debris, obsidian, bedrock);
    }

    @Override
    public void onEnable() {
        EVENTS.add(SetOpaqueCubeListener.class, this);
        EVENTS.add(GetAmbientOcclusionLightLevelListener.class, this);
        EVENTS.add(ShouldDrawSideListener.class, this);
        EVENTS.add(TesselateBlockListener.class, this);
        EVENTS.add(RenderBlockEntityListener.class, this);
        MC.worldRenderer.reload();
    }

    @Override
    public void onDisable() {
        EVENTS.remove(SetOpaqueCubeListener.class, this);
        EVENTS.remove(GetAmbientOcclusionLightLevelListener.class, this);
        EVENTS.remove(ShouldDrawSideListener.class, this);
        EVENTS.remove(TesselateBlockListener.class, this);
        EVENTS.remove(RenderBlockEntityListener.class, this);
        MC.worldRenderer.reload();
    }

    @Override
    public void onSetOpaqueCubeListener(SetOpaqueCubeEvent event) {
        event.cancel();
    }

    @Override
    public void onGetAmbientOcclusionLightLevel(GetAmbientOcclusionLightLevelEvent event) {
        event.setLightLevel(1);
    }

    @Override
    public void onShouldDrawSide(ShouldDrawSideEvent event) {
        event.setRendered(isVisible(event.getState().getBlock()));
    }

    @Override
    public void onTesselateBlock(TesselateBlockEvent event) {
        if(!isVisible(event.getState().getBlock())) event.cancel();
    }

    @Override
    public void onRenderBlockEntity(RenderBlockEntityEvent event) {
        if(!isVisible(BlockUtils.getBlock(event.getBlockEntity().getPos()))) event.cancel();
    }

    private boolean isVisible(Block block) {
        if(coal.isEnabled() && (Objects.equals(block, Blocks.COAL_ORE) || Objects.equals(block, Blocks.DEEPSLATE_COAL_ORE))) return true;
        else if(copper.isEnabled() && (Objects.equals(block, Blocks.COPPER_ORE) || Objects.equals(block, Blocks.DEEPSLATE_COPPER_ORE))) return true;
        else if(lapis.isEnabled() && (Objects.equals(block, Blocks.LAPIS_ORE) || Objects.equals(block, Blocks.DEEPSLATE_LAPIS_ORE))) return true;
        else if(iron.isEnabled() && (Objects.equals(block, Blocks.IRON_ORE) || Objects.equals(block, Blocks.DEEPSLATE_IRON_ORE))) return true;
        else if(redstone.isEnabled() && (Objects.equals(block, Blocks.REDSTONE_ORE) || Objects.equals(block, Blocks.DEEPSLATE_REDSTONE_ORE))) return true;
        else if(diamond.isEnabled() && (Objects.equals(block, Blocks.DIAMOND_ORE) || Objects.equals(block, Blocks.DEEPSLATE_DIAMOND_ORE))) return true;
        else if(gold.isEnabled() && (Objects.equals(block, Blocks.GOLD_ORE) || Objects.equals(block, Blocks.DEEPSLATE_GOLD_ORE) || Objects.equals(block, Blocks.NETHER_GOLD_ORE))) return true;
        else if(emerald.isEnabled() && (Objects.equals(block, Blocks.EMERALD_ORE) || Objects.equals(block, Blocks.DEEPSLATE_EMERALD_ORE))) return true;
        else if(quartz.isEnabled() && Objects.equals(block, Blocks.NETHER_QUARTZ_ORE)) return true;
        else if(ancient_debris.isEnabled() && Objects.equals(block, Blocks.ANCIENT_DEBRIS)) return true;
        else if(obsidian.isEnabled() && Objects.equals(block, Blocks.OBSIDIAN)) return true;
        else if(bedrock.isEnabled() && Objects.equals(block, Blocks.BEDROCK)) return true;
        return false;
    }
}
