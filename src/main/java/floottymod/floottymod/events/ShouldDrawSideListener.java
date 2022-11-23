package floottymod.floottymod.events;

import floottymod.floottymod.event.Event;
import floottymod.floottymod.event.Listener;
import net.minecraft.block.BlockState;

import java.util.ArrayList;

public interface ShouldDrawSideListener extends Listener {
    public void onShouldDrawSide(ShouldDrawSideEvent event);

    public static class ShouldDrawSideEvent extends Event<ShouldDrawSideListener> {
        private final BlockState state;
        private Boolean rendered;

        public ShouldDrawSideEvent(BlockState state) {
            this.state = state;
        }

        public BlockState getState() {
            return state;
        }

        public Boolean isRendered() {
            return rendered;
        }

        public void setRendered(boolean rendered) {
            this.rendered = rendered;
        }

        @Override
        public void fire(ArrayList<ShouldDrawSideListener> listeners) {
            for(ShouldDrawSideListener listener : listeners) listener.onShouldDrawSide(this);
        }

        @Override
        public Class<ShouldDrawSideListener> getListenerType() {
            return ShouldDrawSideListener.class;
        }
    }
}
