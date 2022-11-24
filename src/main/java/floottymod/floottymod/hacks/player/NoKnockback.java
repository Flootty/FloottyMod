package floottymod.floottymod.hacks.player;


import floottymod.floottymod.events.KnockbackListener;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.settings.SliderSetting;

public class NoKnockback extends Hack implements KnockbackListener {
	SliderSetting vStrength = new SliderSetting("Vertical", 1, 0, 1, .01);
	SliderSetting hStrength = new SliderSetting("Horizontal", 1, 0, 1, .01);
	
	public NoKnockback() {
		super("NoKnockback", Category.PLAYER);
		addSettings(vStrength, hStrength);
	}
	
	@Override
	public void onEnable() {
		EVENTS.add(KnockbackListener.class, this);
	}
	
	@Override
	public void onDisable() {
		EVENTS.remove(KnockbackListener.class, this);
	}

	@Override
	public void onKnockback(KnockbackEvent event) {
		double verticalMultiplier = 1 - vStrength.getValue();
		double horizontalMultiplier = 1 - hStrength.getValue();
		
		event.setX(event.getDefaultX() * horizontalMultiplier);
		event.setY(event.getDefaultY() * verticalMultiplier);
		event.setZ(event.getDefaultZ() * horizontalMultiplier);
	}
}
