package floottymod.floottymod.events;

import floottymod.floottymod.event.CancellableEvent;
import floottymod.floottymod.event.Listener;
import net.minecraft.block.BlockState;

import java.util.ArrayList;

public interface TesselateBlockListener extends Listener {
    public void onTesselateBlock(TesselateBlockEvent event);

    public static class TesselateBlockEvent extends CancellableEvent<TesselateBlockListener> {
        private final BlockState state;

        public TesselateBlockEvent(BlockState state) {
            this.state = state;
        }

        public BlockState getState() {
            return state;
        }

        @Override
        public void fire(ArrayList<TesselateBlockListener> listeners) {
            for(TesselateBlockListener listener : listeners) {
                listener.onTesselateBlock(this);

                if(isCancelled()) break;
            }
        }

        @Override
        public Class<TesselateBlockListener> getListenerType() {
            return TesselateBlockListener.class;
        }
    }
}
