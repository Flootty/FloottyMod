package floottymod.floottymod.mixin;

import floottymod.floottymod.event.EventManager;
import floottymod.floottymod.events.RemoveStatusEffectListener.RemoveStatusEffectEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "clearStatusEffects", at = @At("HEAD"), cancellable = true)
    public void clearStatusEffects(CallbackInfoReturnable<Boolean> cir) {
        RemoveStatusEffectEvent event = new RemoveStatusEffectEvent(null);
        EventManager.fire(event);
    }
}
