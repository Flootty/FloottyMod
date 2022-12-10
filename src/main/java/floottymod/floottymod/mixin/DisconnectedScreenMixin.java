package floottymod.floottymod.mixin;

import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.util.LastServerRememberer;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DisconnectedScreen.class)
public class DisconnectedScreenMixin extends Screen {
    private int autoReconnectTimer;

    @Shadow
    @Final
    private Screen parent;

    protected DisconnectedScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void onInit(CallbackInfo ci) {
        autoReconnectTimer = FloottyMod.INSTANCE.getHackList().autoReconnect.getWaitTicks();
    }

    @Override
    public void tick() {
        if(autoReconnectTimer > 0)  {
            autoReconnectTimer--;
            return;
        }

        LastServerRememberer.reconnect(parent);
    }
}
