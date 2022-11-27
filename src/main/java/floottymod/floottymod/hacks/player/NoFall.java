package floottymod.floottymod.hacks.player;

import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.events.TickListener;
import floottymod.floottymod.events.UpdateListener;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.hacks.movement.Flight;
import floottymod.floottymod.mixin.ClientConnectionInvoker;
import floottymod.floottymod.util.PacketUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.OnGroundOnly;

public class NoFall extends Hack implements TickListener {
	public NoFall() {
		super("NoFall", Category.PLAYER);
	}
	
	@Override
	public void onEnable() {
		EVENTS.add(TickListener.class, this);
	}
	
	@Override
	public void onDisable() {
		EVENTS.remove(TickListener.class, this);
	}

	@Override
	public void onTick() {
		if(MC.player.fallDistance <= (MC.player.isFallFlying() ? 1 : 2)) return;
		if(MC.player.isFallFlying() && MC.player.isSneaking() && !isFallingFastEnoughToCauseDamage(MC.player)) return;
		
		if(MC.player.fallDistance > 2) PacketUtils.sendOnGround(true);
	}
	
	private boolean isFallingFastEnoughToCauseDamage(ClientPlayerEntity player) {
		return player.getVelocity().y < -0.5;
	}
}
