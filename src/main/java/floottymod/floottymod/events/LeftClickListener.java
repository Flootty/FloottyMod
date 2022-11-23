package floottymod.floottymod.events;

import floottymod.floottymod.event.CancellableEvent;
import floottymod.floottymod.event.Listener;

import java.util.ArrayList;

public interface LeftClickListener extends Listener {
	public void onLeftClick(LeftClickEvent event);
	
	public static class LeftClickEvent extends CancellableEvent<LeftClickListener> {
		@Override
		public void fire(ArrayList<LeftClickListener> listeners) {
			for(LeftClickListener listener : listeners) {
				listener.onLeftClick(this);
				if(isCancelled()) break;
			}
		}
		
		@Override
		public Class<LeftClickListener> getListenerType() {
			return LeftClickListener.class;
		}
	}
}
