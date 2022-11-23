package floottymod.floottymod.mixin;

import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.event.EventManager;
import floottymod.floottymod.events.LeftClickListener;
import floottymod.floottymod.mixininterface.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.client.util.Session;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements IMinecraftClient {
	@Shadow
	@Final
	public File runDirectory;
	@Shadow
	private int itemUseCooldown;
	@Shadow
	private ClientPlayerInteractionManager interactionManager;
	@Shadow
	@Final
	private LanguageManager languageManager;
	@Shadow
	private ClientPlayerEntity player;
	@Shadow
	public ClientWorld world;

	private MinecraftClientMixin(FloottyMod wurst, String string_1) {
		super();
	}
	
	@Inject(at = @At(value = "HEAD"), method = "doAttack", cancellable = true)
	private void onDoAttack(CallbackInfoReturnable<Boolean> cir){
		LeftClickListener.LeftClickEvent event = new LeftClickListener.LeftClickEvent();
		EventManager.fire(event);
		
		if(event.isCancelled())
			cir.cancel();
		}
	
	@Inject(method = "updateWindowTitle", at = @At("HEAD"), cancellable = true)
	public void updateTitle(CallbackInfo ci) {
		MinecraftClient.getInstance().getWindow().setTitle(FloottyMod.name + " v" + FloottyMod.version);
		ci.cancel();
	}

	@Override
	public void rightClick() {
		doItemUse();
	}
	
	@Override
	public int getItemUseCooldown() {
		return itemUseCooldown;
	}
	
	@Override
	public void setItemUseCooldown(int itemUseCooldown) {
		this.itemUseCooldown = itemUseCooldown;
	}
	
	@Override
	public IClientPlayerEntity getPlayer() {
		return (IClientPlayerEntity)player;
	}
	
	@Override
	public IWorld getWorld() {
		return (IWorld)world;
	}
	
	@Override
	public IClientPlayerInteractionManager getInteractionManager() {
		return (IClientPlayerInteractionManager)interactionManager;
	}
	
	@Override
	public ILanguageManager getLanguageManager() {
		return (ILanguageManager)languageManager;
	}
	
	@Shadow
	private void doItemUse() {
		
	}

	@Override
	public void setSession(Session session) {
		
	}
}
