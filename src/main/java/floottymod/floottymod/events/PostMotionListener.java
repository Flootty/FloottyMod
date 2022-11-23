package floottymod.floottymod.events;

import floottymod.floottymod.event.Event;
import floottymod.floottymod.event.Listener;

import java.util.ArrayList;

public interface PostMotionListener extends Listener {
	public void onPostMotion();
	
	public static class PostMotionEvent extends Event<PostMotionListener> {
		public static final PostMotionEvent INSTANCE = new PostMotionEvent();
		
		@Override
		public void fire(ArrayList<PostMotionListener> listeners) {
			for(PostMotionListener listener : listeners) listener.onPostMotion();
		}

		@Override
		public Class<PostMotionListener> getListenerType() {
			return PostMotionListener.class;
		}
		
	}
}
