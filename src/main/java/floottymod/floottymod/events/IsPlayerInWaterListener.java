package floottymod.floottymod.events;

import floottymod.floottymod.event.Event;
import floottymod.floottymod.event.Listener;

import java.util.ArrayList;

public interface IsPlayerInWaterListener extends Listener {
    void onIsPlayerInWater(IsPlayerInWaterEvent event);

    class IsPlayerInWaterEvent extends Event<IsPlayerInWaterListener> {
        private boolean inWater;
        private final boolean normallyInWater;

        public IsPlayerInWaterEvent(boolean inWater) {
            this.inWater = inWater;
            normallyInWater = inWater;
        }

        public boolean isInWater() {
            return inWater;
        }

        public void setInWater(boolean inWater) {
            this.inWater = inWater;
        }

        public boolean isNormallyInWater() {
            return normallyInWater;
        }

        @Override
        public void fire(ArrayList<IsPlayerInWaterListener> listeners) {
            for(IsPlayerInWaterListener listener : listeners) listener.onIsPlayerInWater(this);
        }

        @Override
        public Class<IsPlayerInWaterListener> getListenerType() {
            return IsPlayerInWaterListener.class;
        }
    }
}
