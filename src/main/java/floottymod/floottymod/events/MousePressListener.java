package floottymod.floottymod.events;

import floottymod.floottymod.event.CancellableEvent;
import floottymod.floottymod.event.Listener;

import java.util.ArrayList;

public interface MousePressListener extends Listener {
    void onMousePress(MousePressEvent event);

    class MousePressEvent extends CancellableEvent<MousePressListener> {
        public int key, action;

        public MousePressEvent(int key, int action) {
            this.key = key;
            this.action = action;
        }

        @Override
        public void fire(ArrayList<MousePressListener> listeners) {
            for(MousePressListener listener : listeners) listener.onMousePress(this);
        }

        @Override
        public Class<MousePressListener> getListenerType() {
            return MousePressListener.class;
        }
    }
}
