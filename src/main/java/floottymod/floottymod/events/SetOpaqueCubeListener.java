package floottymod.floottymod.events;

import floottymod.floottymod.event.CancellableEvent;
import floottymod.floottymod.event.Event;
import floottymod.floottymod.event.Listener;

import java.util.ArrayList;

public interface SetOpaqueCubeListener extends Listener {
    public void onSetOpaqueCubeListener(SetOpaqueCubeEvent event);

    public static class SetOpaqueCubeEvent extends CancellableEvent<SetOpaqueCubeListener> {
        @Override
        public void fire(ArrayList<SetOpaqueCubeListener> listeners) {
            for(SetOpaqueCubeListener listener : listeners) {
                listener.onSetOpaqueCubeListener(this);
                if(isCancelled()) break;
            }
        }

        @Override
        public Class<SetOpaqueCubeListener> getListenerType() {
            return SetOpaqueCubeListener.class;
        }
    }
}
