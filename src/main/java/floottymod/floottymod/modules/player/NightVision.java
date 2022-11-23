package floottymod.floottymod.modules.player;

import floottymod.floottymod.modules.Category;
import floottymod.floottymod.modules.Hack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class NightVision extends Hack {
    StatusEffectInstance night_vision;

    public NightVision() {
        super("NightVision", Category.PLAYER);

        night_vision = new StatusEffectInstance(StatusEffects.NIGHT_VISION, (int) Double.POSITIVE_INFINITY, 99, true, true);
        night_vision.setPermanent(true);
    }

    @Override
    public void onEnable() {
        MC.player.addStatusEffect(night_vision);
    }

    @Override
    public void onDisable() {
        MC.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
    }
}
