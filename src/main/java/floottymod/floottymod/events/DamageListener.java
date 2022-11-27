package floottymod.floottymod.events;

import floottymod.floottymod.event.Event;
import floottymod.floottymod.event.Listener;
import net.minecraft.entity.damage.DamageSource;

import java.util.ArrayList;

public interface DamageListener extends Listener {
    void onDamage(DamageEvent event);

    class DamageEvent extends Event<DamageListener> {
        public DamageSource source;

        public DamageEvent(DamageSource source) {
            this.source = source;
        }

        @Override
        public void fire(ArrayList<DamageListener> listeners) {
            for(DamageListener listener : listeners) listener.onDamage(this);
        }

        @Override
        public Class<DamageListener> getListenerType() {
            return DamageListener.class;
        }
    }
}
