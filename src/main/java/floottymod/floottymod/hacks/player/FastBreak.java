package floottymod.floottymod.hacks.player;


import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.events.BlockBreakingProgressListener;
import floottymod.floottymod.events.TickListener;
import floottymod.floottymod.events.UpdateListener;
import floottymod.floottymod.mixininterface.IClientPlayerInteractionManager;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.settings.BoolSetting;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

//long delay

public class FastBreak extends Hack implements TickListener, BlockBreakingProgressListener {
	BoolSetting legit = new BoolSetting("Legit", false);
	
	public FastBreak() {
		super("FastBreak", Category.PLAYER);
		addSetting(legit);
	}
	
	@Override
	public void onEnable() {
		EVENTS.add(TickListener.class, this);
		EVENTS.add(BlockBreakingProgressListener.class, this);
	}
	
	@Override
	public void onDisable() {
		EVENTS.remove(TickListener.class, this);
		EVENTS.remove(BlockBreakingProgressListener.class, this);
	}

	@Override
	public void onTick() {
		if(MC.player == null) return;
		FloottyMod.IMC.getInteractionManager().setBlockHitDelay(0);
	}
	
	@Override
	public void onBlocKBreakingProgress(BlockBreakingProgressEvent event) {
		if(legit.isEnabled()) return;
		
		IClientPlayerInteractionManager im = FloottyMod.IMC.getInteractionManager();
		
		if(im.getCurrentBreakingProgress() >= 1) return;
		
		Action action = Action.STOP_DESTROY_BLOCK;
		BlockPos blockPos = event.getBlockPos();
		Direction direction = event.getDirection();
		im.sendPlayerActionC2SPacket(action, blockPos, direction);
	}
}
