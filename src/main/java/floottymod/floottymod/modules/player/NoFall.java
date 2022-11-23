package floottymod.floottymod.modules.player;

import floottymod.floottymod.events.UpdateListener;
import floottymod.floottymod.modules.Category;
import floottymod.floottymod.modules.Hack;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.OnGroundOnly;

public class NoFall extends Hack implements UpdateListener {
	public NoFall() {
		super("No Fall", Category.PLAYER);
	}
	
	@Override
	public void onEnable() {
		EVENTS.add(UpdateListener.class, this);
	}
	
	@Override
	public void onDisable() {
		EVENTS.remove(UpdateListener.class, this);
	}

	@Override
	public void onUpdate() {
		if(MC.player.fallDistance <= (MC.player.isFallFlying() ? 1 : 2)) return;
		if(MC.player.isFallFlying() && MC.player.isSneaking() && !isFallingFastEnoughToCauseDamage(MC.player)) return;
		
		if(MC.player.fallDistance > 2) MC.player.networkHandler.sendPacket(new OnGroundOnly(true));
	}
	
	private boolean isFallingFastEnoughToCauseDamage(ClientPlayerEntity player) {
		return player.getVelocity().y < -0.5;
	}
}
