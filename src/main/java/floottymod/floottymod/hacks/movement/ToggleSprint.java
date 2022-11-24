package floottymod.floottymod.hacks.movement;


import floottymod.floottymod.events.UpdateListener;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;

public class ToggleSprint extends Hack implements UpdateListener {
	public ToggleSprint() {
		super("ToggleSprint", Category.MOVEMENT);
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
		if(!MC.player.isBlocking() && !MC.player.isUsingItem() && !MC.player.isUsingSpyglass() && !MC.player.horizontalCollision) MC.player.setSprinting(true);
	}
}
