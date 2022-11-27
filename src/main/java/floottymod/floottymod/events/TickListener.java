package floottymod.floottymod.events;

import floottymod.floottymod.event.Event;
import floottymod.floottymod.event.Listener;

import java.util.ArrayList;

public interface TickListener extends Listener {
    public void onTick();

    public static class TickEvent extends Event<TickListener> {
        @Override
        public void fire(ArrayList<TickListener> listeners) {
            for(TickListener listener : listeners) listener.onTick();
        }

        @Override
        public Class<TickListener> getListenerType() {
            return TickListener.class;
        }
    }
}
