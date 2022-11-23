package floottymod.floottymod.modules.movement;


import floottymod.floottymod.events.UpdateListener;
import floottymod.floottymod.modules.Category;
import floottymod.floottymod.modules.Hack;

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
