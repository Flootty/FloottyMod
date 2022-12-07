package floottymod.floottymod.hacks.movement;

import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.event.EventManager;
import floottymod.floottymod.events.IsPlayerInWaterListener;
import floottymod.floottymod.events.TickListener;
import floottymod.floottymod.events.UpdateListener;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.settings.BoolSetting;
import floottymod.floottymod.settings.ModeSetting;
import floottymod.floottymod.settings.SliderSetting;
import floottymod.floottymod.ui.screens.clickgui.setting.Slider;
import floottymod.floottymod.util.ChatUtils;
import floottymod.floottymod.util.PacketUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.message.SentMessage;
import net.minecraft.util.math.Vec3d;

public class Flight extends Hack implements TickListener, IsPlayerInWaterListener {
	private SliderSetting horizontalSpeed = new SliderSetting("Horizontal Speed", 1, 0.05, 10, 0.05);
	private SliderSetting verticalSpeed = new SliderSetting("Vertical Speed", 1, 0.05, 5, 0.05);
	private BoolSetting slowSneaking = new BoolSetting("Slow sneaking", true);
	private BoolSetting antiKick = new BoolSetting("Anti kick", false);
	private SliderSetting antiKickInterval = new SliderSetting("Anti kick interval", 30, 5, 80, 1);

	private int tickCounter = 0;

	public Flight() {
		super("Flight", Category.MOVEMENT);
		addSettings(horizontalSpeed, verticalSpeed, slowSneaking, antiKick, antiKickInterval);
	}
	
	@Override
	public void onEnable() {
		tickCounter = 0;
		FloottyMod.INSTANCE.getHackList().creativeFlight.setEnabled(false, true);
		EVENTS.add(TickListener.class, this);
		EVENTS.add(IsPlayerInWaterListener.class, this);
	}
	
	@Override
	public void onDisable() {
		EVENTS.remove(TickListener.class, this);
		EVENTS.remove(IsPlayerInWaterListener.class, this);
	}

	@Override
	public void onTick() {
		if(MC.player == null) return;

		ClientPlayerEntity player = MC.player;

		player.getAbilities().flying = false;
		player.airStrafingSpeed = horizontalSpeed.getValueFloat();

		player.setVelocity(0, 0, 0);
		Vec3d velocity = player.getVelocity();

		if(MC.options.jumpKey.isPressed()) player.setVelocity(velocity.x, verticalSpeed.getValue(), velocity.z);
		if(MC.options.sneakKey.isPressed()) {
			player.airStrafingSpeed = Math.min(horizontalSpeed.getValueFloat(), 0.85f);
			player.setVelocity(velocity.x, -verticalSpeed.getValue(),velocity.z);
		}

		if(antiKick.isEnabled()) doAntiKick(velocity);
	}

	private void doAntiKick(Vec3d velocity) {
		if(tickCounter > antiKickInterval.getValueInt()) tickCounter = 0;

		switch(tickCounter) {
			case 0 -> {
				if(MC.options.sneakKey.isPressed()) tickCounter = 2;
				else MC.player.setVelocity(velocity.x, -0.07, velocity.z);
			}
			case 1 -> MC.player.setVelocity(velocity.x, 0.07, velocity.z);
		}

		tickCounter++;
	}

	@Override
	public void onIsPlayerInWater(IsPlayerInWaterEvent event) {
		event.setInWater(false);
	}
}
