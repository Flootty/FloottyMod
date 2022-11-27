package floottymod.floottymod.hacks.movement;

import floottymod.floottymod.events.TickListener;
import floottymod.floottymod.events.UpdateListener;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.settings.BoolSetting;
import floottymod.floottymod.settings.ModeSetting;
import floottymod.floottymod.settings.SliderSetting;
import floottymod.floottymod.util.ChatUtils;
import floottymod.floottymod.util.PacketUtils;
import net.minecraft.network.message.SentMessage;

//anti cheat version wierd when on ground

public class Flight extends Hack implements TickListener {
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

		EVENTS.add(TickListener.class, this);
		if(type.isMode("Vanilla")) MC.player.getAbilities().allowFlying = true;
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		EVENTS.remove(TickListener.class, this);
		MC.player.getAbilities().flying = false;
		if(!MC.player.isCreative()) MC.player.getAbilities().allowFlying = false;
		super.onDisable();
	}

	@Override
	public void onTick() {
		if(timer <= 0) {
			if(!MC.player.isOnGround() && antiCheat.isEnabled()) PacketUtils.sendPosition(MC.player.getPos().subtract(0, 0.0433D, 0));
			timer = 40;
		}
		timer--;

		MC.player.getAbilities().setFlySpeed(speed.getValueFloat() / 100);
		if(type.isMode("Permanent")) MC.player.getAbilities().flying = true;
	}
}
