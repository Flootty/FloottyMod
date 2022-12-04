package floottymod.floottymod.mixin;

import com.mojang.authlib.GameProfile;
import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.event.EventManager;
import floottymod.floottymod.events.DamageListener.DamageEvent;
import floottymod.floottymod.events.IsPlayerInWaterListener.IsPlayerInWaterEvent;
import floottymod.floottymod.events.KnockbackListener.KnockbackEvent;
import floottymod.floottymod.events.PostMotionListener.PostMotionEvent;
import floottymod.floottymod.events.PreMotionListener.PreMotionEvent;
import floottymod.floottymod.events.UpdateListener.UpdateEvent;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
	public ClientPlayerEntityMixin(FloottyMod floottyHack, ClientWorld clientWorld_1, GameProfile gameProfile_1) {
		super(clientWorld_1, gameProfile_1, null);
	}

	@Inject(method = "tick", at = @At("INVOKE"))
	private void onTick(CallbackInfo ci) {
		EventManager.fire(UpdateEvent.INSTANCE);
	}
	
	@Inject(at = {@At("HEAD")}, method = {"sendMovementPackets()V"})
	private void onSendMovementPacketsHEAD(CallbackInfo ci) {
		EventManager.fire(PreMotionEvent.INSTANCE);
	}
	
	@Inject(at = {@At("TAIL")}, method = {"sendMovementPackets()V"})
	private void onSendMovementPacketsTAIL(CallbackInfo ci) {
		EventManager.fire(PostMotionEvent.INSTANCE);
	}

	@Inject(method = "applyDamage", at = @At("HEAD"), cancellable = true)
	private void applyDamage(DamageSource source, float amount, CallbackInfo ci) {
		DamageEvent event = new DamageEvent(source);
		EventManager.fire(event);
	}

	@Override
	public boolean isTouchingWater() {
		boolean inWater = super.isTouchingWater();
		IsPlayerInWaterEvent event = new IsPlayerInWaterEvent(inWater);
		EventManager.fire(event);
		return event.isInWater();
	}
	
	@Override
	public void setVelocityClient(double x, double y, double z) {
		KnockbackEvent event = new KnockbackEvent(x, y, z);
		EventManager.fire(event);
		super.setVelocityClient(event.getX(), event.getY(), event.getZ());
	}
}
