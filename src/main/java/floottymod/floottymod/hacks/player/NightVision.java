package floottymod.floottymod.hacks.player;

import floottymod.floottymod.events.PacketInputListener;
import floottymod.floottymod.events.PacketOutputListener;
import floottymod.floottymod.events.RemoveStatusEffectListener;
import floottymod.floottymod.events.TickListener;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.util.ChatUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.s2c.play.DeathMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;

public class NightVision extends Hack implements TickListener {
    StatusEffectInstance night_vision;

    public NightVision() {
        super("NightVision", Category.PLAYER);

        night_vision = new StatusEffectInstance(StatusEffects.NIGHT_VISION, (int) Double.POSITIVE_INFINITY, 99, true, false);
        night_vision.setPermanent(true);
    }

    @Override
    public void onEnable() {
        EVENTS.add(TickListener.class, this);
        MC.player.addStatusEffect(night_vision);
    }

    @Override
    public void onDisable() {
        EVENTS.remove(TickListener.class, this);
        MC.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
    }

    @Override
    public void onTick() {
        StatusEffectInstance effect = MC.player.getStatusEffect(StatusEffects.NIGHT_VISION);
        if(effect == null) MC.player.addStatusEffect(night_vision);
    }
}
