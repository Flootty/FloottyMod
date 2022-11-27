package floottymod.floottymod.hacks.movement;


import floottymod.floottymod.events.TickListener;
import floottymod.floottymod.events.UpdateListener;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;

public class ToggleSprint extends Hack implements TickListener {
	public ToggleSprint() {
		super("ToggleSprint", Category.MOVEMENT);
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
		if(!MC.player.isBlocking() && !MC.player.isUsingItem() && !MC.player.isUsingSpyglass() && !MC.player.horizontalCollision) MC.player.setSprinting(true);
	}
}
