package floottymod.floottymod.hacks.render;

import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.events.IngameHudRenderListener;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Collection;
import java.util.Comparator;

public class Hud extends Hack implements IngameHudRenderListener {
    public Hud() {
        super("Hud", Category.RENDER);
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
        //MC.textRenderer.drawWithShadow(matrixStack, FloottyMod.name + " v" + FloottyMod.version, 10, 10, -1);
        renderArrayList(matrixStack);
    }

    public static void renderArrayList(MatrixStack matrixStack) {
        int index = 0;
        int sWidth = MC.getWindow().getScaledWidth();

        Collection<Hack> enabled = FloottyMod.INSTANCE.getHackList().getHacksEnabled();
        enabled.stream().filter(h -> !(h instanceof Hud));
        enabled.stream().sorted(Comparator.comparingInt(h -> MC.textRenderer.getWidth(((Hack) h).getName())).reversed());

        for(Hack h : enabled) {
            MC.textRenderer.draw(matrixStack, h.getName(), (sWidth - 4) - MC.textRenderer.getWidth(h.getName()), 10 + index * MC.textRenderer.fontHeight, -1);
            index++;
        }
    }
}
