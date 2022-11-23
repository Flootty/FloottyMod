package floottymod.floottymod.modules.render;

import floottymod.floottymod.events.IngameHudRenderListener;
import floottymod.floottymod.modules.Category;
import floottymod.floottymod.modules.Hack;
import net.minecraft.client.util.math.MatrixStack;

public class Coordinates extends Hack implements IngameHudRenderListener {
    public Coordinates() {
        super("Coordinates", Category.RENDER);
    }

    @Override
    public void onEnable() {
        EVENTS.add(IngameHudRenderListener.class, this);
    }

    @Override
    public void onDisable() {
        EVENTS.remove(IngameHudRenderListener.class, this);
    }

    @Override
    public void onIngameHudRender(MatrixStack matrixStack, float tickDelta) {
        String coords = "\u00a78[\u00a7fX\u00a78]\u00a7f " + String.valueOf(Math.round(MC.player.getX())) + " \u00a78[\u00a7fY\u00a78]\u00a7f " + String.valueOf(Math.round(MC.player.getY())) + " \u00a78[\u00a7fZ\u00a78]\u00a7f " + String.valueOf(Math.round(MC.player.getZ()));
        MC.textRenderer.drawWithShadow(matrixStack, coords, 4, 4, -1);
    }
}
