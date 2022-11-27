package floottymod.floottymod.mixin;

import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.event.EventManager;
import floottymod.floottymod.events.MousePressListener.MousePressEvent;
import floottymod.floottymod.events.MouseScrollListener.MouseScrollEvent;
import floottymod.floottymod.hacks.render.FreeCam;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
    public void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        MousePressEvent event = new MousePressEvent(button, action);
        EventManager.fire(event);

        if(event.isCancelled()) ci.cancel();
    }

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        MouseScrollEvent event = new MouseScrollEvent(vertical);
        EventManager.fire(event);

        if(event.isCancelled()) ci.cancel();
    }

    @Redirect(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"))
    private void updateMouseChangeLookDirection(ClientPlayerEntity player, double cursorDeltaX, double cursorDeltaY) {
        FreeCam freeCam = FloottyMod.INSTANCE.getHackList().freeCam;

        if (freeCam.isEnabled()) freeCam.changeLookDirection(cursorDeltaX * 0.15, cursorDeltaY * 0.15);
        else player.changeLookDirection(cursorDeltaX, cursorDeltaY);
    }
}
