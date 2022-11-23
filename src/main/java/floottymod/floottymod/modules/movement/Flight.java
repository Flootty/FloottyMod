package floottymod.floottymod.modules.movement;

import floottymod.floottymod.events.UpdateListener;
import floottymod.floottymod.modules.Category;
import floottymod.floottymod.modules.Hack;
import floottymod.floottymod.modules.settings.BoolSetting;
import floottymod.floottymod.modules.settings.ModeSetting;
import floottymod.floottymod.modules.settings.SliderSetting;
import floottymod.floottymod.util.ChatUtils;
import floottymod.floottymod.util.PacketUtils;

//anti cheat version wierd when on ground

public class Flight extends Hack implements UpdateListener {
	public SliderSetting speed = new SliderSetting("Speed", 5, 0, 100, 0.1);
	public ModeSetting type = new ModeSetting("Type", "Permanent", "Vanilla", "Permanent");
	public BoolSetting antiCheat = new BoolSetting("Anticheat", false);

	private float timer;

	public Flight() {
		super("Flight", Category.MOVEMENT);
		addSettings(speed, type, antiCheat);
	}
	
	@Override
	public void onEnable() {
		timer = 40;

		EVENTS.add(UpdateListener.class, this);
		if(type.isMode("Vanilla")) {
			MC.player.getAbilities().allowFlying = true;
		}
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		EVENTS.remove(UpdateListener.class, this);
		MC.player.getAbilities().flying = false;
		if(!MC.player.isCreative()) MC.player.getAbilities().allowFlying = false;
		super.onDisable();
	}

	@Override
	public void onUpdate() {
		if(timer <= 0) {
			if(!MC.player.isOnGround() && antiCheat.isEnabled()) PacketUtils.sendPosition(MC.player.getPos().subtract(0, 0.0433D, 0));
			timer = 40;
		}
		timer--;

		MC.player.getAbilities().setFlySpeed(speed.getValueFloat() / 100);
		if(type.isMode("Permanent")) MC.player.getAbilities().flying = true;
	}
}
