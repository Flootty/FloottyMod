package floottymod.floottymod.events;

import floottymod.floottymod.event.Event;
import floottymod.floottymod.event.Listener;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;

public interface IngameHudRenderListener extends Listener {
    public void onIngameHudRender(MatrixStack matrixStack, float tickDelta);

    public static class IngameHudRenderEvent extends Event<IngameHudRenderListener> {
        private MatrixStack matrixStack;
        private float tickDelta;

        public IngameHudRenderEvent(MatrixStack matrixStack, float tickDelta) {
            this.matrixStack = matrixStack;
            this.tickDelta = tickDelta;
        }

        @Override
        public void fire(ArrayList<IngameHudRenderListener> listeners) {
            for(IngameHudRenderListener listener : listeners) listener.onIngameHudRender(matrixStack, tickDelta);
        }

        @Override
        public Class<IngameHudRenderListener> getListenerType() {
            return IngameHudRenderListener.class;
        }
    }

}
