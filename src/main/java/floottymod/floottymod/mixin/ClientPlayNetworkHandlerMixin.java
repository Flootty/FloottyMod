package floottymod.floottymod.mixin;

import floottymod.floottymod.event.EventManager;
import floottymod.floottymod.events.PacketOutputListener.PacketOutputEvent;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(at = @At("HEAD"), method = "sendPacket", cancellable = true)
    private void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        PacketOutputEvent event = new PacketOutputEvent(packet);
        EventManager.fire(event);

        if(event.isCancelled()) ci.cancel();
    }
}
