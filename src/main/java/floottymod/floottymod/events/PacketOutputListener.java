package floottymod.floottymod.events;

import floottymod.floottymod.event.CancellableEvent;
import floottymod.floottymod.event.Listener;
import net.minecraft.network.Packet;

import java.util.ArrayList;

public interface PacketOutputListener extends Listener {
    void onSentPacket(PacketOutputEvent event);

    public static class PacketOutputEvent extends CancellableEvent<PacketOutputListener> {
        private Packet<?> packet;

        public PacketOutputEvent(Packet<?> packet) {
            this.packet = packet;
        }

        public Packet<?> getPacket() {
            return packet;
        }

        public void setPacket(Packet<?> packet) {
            this.packet = packet;
        }

        @Override
        public void fire(ArrayList<PacketOutputListener> listeners) {
            for(PacketOutputListener listener : listeners) {
                listener.onSentPacket(this);

                if(isCancelled()) break;
            }
        }

        @Override
        public Class<PacketOutputListener> getListenerType() {
            return PacketOutputListener.class;
        }
    }
}
