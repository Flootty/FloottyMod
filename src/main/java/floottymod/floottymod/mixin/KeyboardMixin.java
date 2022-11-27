package floottymod.floottymod.mixin;

import floottymod.floottymod.event.EventManager;
import floottymod.floottymod.events.KeyPressListener.KeyPressEvent;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        KeyPressEvent event = new KeyPressEvent(key, action);
        EventManager.fire(event);
        if(event.isCancelled()) ci.cancel();
    }
}
