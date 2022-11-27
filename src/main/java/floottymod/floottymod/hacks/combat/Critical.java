package floottymod.floottymod.hacks.combat;

import floottymod.floottymod.events.LeftClickListener;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.settings.ModeSetting;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class Critical extends Hack implements LeftClickListener {
	ModeSetting critType = new ModeSetting("Type", "Packet", "Packet", "Mini", "Full");
	
	public Critical() {
		super("Critical", Category.COMBAT);
		addSettings(critType);
	}

	@Override
	public void onEnable() {
		EVENTS.add(LeftClickListener.class, this);
	}
	
	@Override
	public void onDisable() {
		EVENTS.remove(LeftClickListener.class, this);
	}
	
	@Override
	public void onLeftClick(LeftClickEvent event) {
		if(MC.player == null) return;

		System.out.println("test");
		if(MC.crosshairTarget == null || MC.crosshairTarget.getType() != HitResult.Type.ENTITY || !(((EntityHitResult)MC.crosshairTarget).getEntity() instanceof LivingEntity)) return;
		doCritical();
	}

	
	public void doCritical() {
		if(!isEnabled()) return;
		if(!MC.player.isOnGround()) return;
		if(MC.player.isTouchingWater() || MC.player.isInLava()) return;
		switch (critType.getMode()) {
		case "Packet":
			doPacketJump();
			break;
		case "Mini":
			doMiniJump();
			break;
		case "Full":
			doFullJump();
			break;
		}
	}
	private void doPacketJump() {
		double posX = MC.player.getX();
		double posY = MC.player.getY();
		double posZ = MC.player.getZ();
		
		sendPos(posX, posY + 0.0625D, posZ, true);
		sendPos(posX, posY, posZ, false);
		sendPos(posX, posY + 1.1E-5D, posZ, false);
		sendPos(posX, posY, posZ, false);
	}

	private void doMiniJump() {
		MC.player.addVelocity(0, 0.1, 0);
		MC.player.fallDistance = 0.1F;
		MC.player.setOnGround(false);
	}

	private void doFullJump() {
		MC.player.jump();
	}
	
	private void sendPos(double x, double y, double z, boolean onGround) {
		MC.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, onGround));
	}
}
