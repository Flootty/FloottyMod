package floottymod.floottymod.events;

import floottymod.floottymod.event.CancellableEvent;
import floottymod.floottymod.event.Listener;
import net.minecraft.network.Packet;

import java.util.ArrayList;

public interface PacketInputListener extends Listener {
	public void onReceivePacket(PacketInputEvent event);
	
	public static class PacketInputEvent extends CancellableEvent<PacketInputListener> {
		private final Packet<?> packet;
		
		public PacketInputEvent(Packet<?> packet) {
			this.packet = packet;
		}
		
		public Packet<?> getPacket() {
			return packet;
		}
		
		@Override
		public void fire(ArrayList<PacketInputListener> listeners) {
			for(PacketInputListener listener : listeners) {
				listener.onReceivePacket(this);
				
				if(isCancelled()) break;
			}
		}

		@Override
		public Class<PacketInputListener> getListenerType() {
			return PacketInputListener.class;
		}
		
	}
}
