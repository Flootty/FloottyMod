package floottymod.floottymod.util;

import floottymod.floottymod.FloottyMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public enum BlockBreaker {
	;
	private static final FloottyMod CLIENT = FloottyMod.INSTANCE;
	private static final MinecraftClient MC = MinecraftClient.getInstance();
	
	public static void breakBlocksWithPacketSpam(Iterable<BlockPos> blocks) {
		
		Vec3d eyesPos = RotationUtils.getEyesPos();
		ClientPlayNetworkHandler netHandler = MC.player.networkHandler;
		
		for(BlockPos pos : blocks) {
			Vec3d posVec = Vec3d.ofCenter(pos);
			double distanceSqPosVec = eyesPos.squaredDistanceTo(posVec);
			
			for(Direction side : Direction.values()) {
				Vec3d hitVec = posVec.add(Vec3d.of(side.getVector()).multiply(0.5));
				
				if(eyesPos.squaredDistanceTo(hitVec) >= distanceSqPosVec) continue;
				
				netHandler.sendPacket(new PlayerActionC2SPacket(Action.START_DESTROY_BLOCK, pos, side));
				netHandler.sendPacket(new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, pos, side));
				break;
			}
		}
	}
}
