package floottymod.floottymod.events;

import floottymod.floottymod.event.CancellableEvent;
import floottymod.floottymod.event.Listener;

import java.util.ArrayList;

public interface KeyPressListener extends Listener {
    void onKey(KeyPressEvent event);

    class KeyPressEvent extends CancellableEvent<KeyPressListener> {
        public int key, action;

        public KeyPressEvent(int key, int action) {
            this.key = key;
            this.action = action;
        }

        @Override
        public void fire(ArrayList<KeyPressListener> listeners) {
            for(KeyPressListener listener : listeners) listener.onKey(this);
        }

        @Override
        public Class<KeyPressListener> getListenerType() {
            return KeyPressListener.class;
        }
    }
}
