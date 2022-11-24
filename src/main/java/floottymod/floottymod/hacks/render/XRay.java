package floottymod.floottymod.hacks.render;

import floottymod.floottymod.events.*;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.util.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;

public class XRay extends Hack implements SetOpaqueCubeListener, GetAmbientOcclusionLightLevelListener, ShouldDrawSideListener, TesselateBlockListener, RenderBlockEntityListener {
    public XRay() {
        super("XRay", Category.RENDER);
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
        return (block instanceof OreBlock);
    }
}
