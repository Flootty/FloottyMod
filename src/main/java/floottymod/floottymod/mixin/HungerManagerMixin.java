package floottymod.floottymod.mixin;

import floottymod.floottymod.FloottyMod;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public abstract class HungerManagerMixin {
    @Shadow private int foodTickTimer;

    @Shadow public abstract void setFoodLevel(int foodLevel);

    @Shadow public abstract void setSaturationLevel(float saturationLevel);

    @Shadow public abstract void setExhaustion(float exhaustion);

    @Inject(at = @At("HEAD"), method = "update", cancellable = true)
    public void update(PlayerEntity player, CallbackInfo ci) {
        if(FloottyMod.INSTANCE.getHackList().antiHunger.isEnabled()) {
            setFoodLevel(20);
            setSaturationLevel(5);
            setExhaustion(0);

            boolean bl = player.world.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION);
            if(bl && player.canFoodHeal()) {
                ++this.foodTickTimer;
                if (this.foodTickTimer >= 80) {
                    player.heal(1.0F);
                    this.foodTickTimer = 0;
                }
            }
            ci.cancel();
        }
    }
}
