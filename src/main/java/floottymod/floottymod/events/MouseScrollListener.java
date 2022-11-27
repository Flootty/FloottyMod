package floottymod.floottymod.events;

import floottymod.floottymod.event.CancellableEvent;
import floottymod.floottymod.event.Event;
import floottymod.floottymod.event.Listener;

import java.util.ArrayList;

public interface MouseScrollListener extends Listener {
    void onMouseScroll(MouseScrollEvent event);

    class MouseScrollEvent extends CancellableEvent<MouseScrollListener> {
        public double value;

        public MouseScrollEvent(double value) {
            this.value = value;
        }

        @Override
        public void fire(ArrayList<MouseScrollListener> listeners) {
            for(MouseScrollListener listener : listeners) listener.onMouseScroll(this);
        }

        @Override
        public Class<MouseScrollListener> getListenerType() {
            return MouseScrollListener.class;
        }
    }
}
