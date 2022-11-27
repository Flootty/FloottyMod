package floottymod.floottymod.hacks.movement;


import floottymod.floottymod.events.TickListener;
import floottymod.floottymod.events.UpdateListener;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;
import net.minecraft.util.math.Vec3d;

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
		if(MC.player.isBlocking()) return;
		if(MC.player.isUsingItem()) return;
		if(MC.player.isUsingSpyglass()) return;
		if(MC.player.horizontalCollision) return;
		if(MC.currentScreen != null) return;
		MC.player.setSprinting(true);
	}
}
