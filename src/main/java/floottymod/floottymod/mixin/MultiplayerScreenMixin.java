package floottymod.floottymod.mixin;

import floottymod.floottymod.util.LastServerRememberer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin extends Screen {
    protected MultiplayerScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "connect(Lnet/minecraft/client/network/ServerInfo;)V", at = @At("HEAD"))
    private void onConnect(ServerInfo entry, CallbackInfo ci) {
        LastServerRememberer.setLastServer(entry);
    }
}
