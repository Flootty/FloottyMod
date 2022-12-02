package floottymod.floottymod.events;

import floottymod.floottymod.event.Event;
import floottymod.floottymod.event.Listener;
import net.minecraft.entity.effect.StatusEffect;

import java.util.ArrayList;

public interface RemoveStatusEffectListener extends Listener {
    void onRemoveStatusEffect(StatusEffect statusEffect);

    class RemoveStatusEffectEvent extends Event<RemoveStatusEffectListener> {
        StatusEffect statusEffect;

        public RemoveStatusEffectEvent(StatusEffect statusEffect) {
            this.statusEffect = statusEffect;
        }

        @Override
        public void fire(ArrayList<RemoveStatusEffectListener> listeners) {
            for(RemoveStatusEffectListener listener : listeners) listener.onRemoveStatusEffect(statusEffect);
        }

        @Override
        public Class<RemoveStatusEffectListener> getListenerType() {
            return RemoveStatusEffectListener.class;
        }
    }
}
