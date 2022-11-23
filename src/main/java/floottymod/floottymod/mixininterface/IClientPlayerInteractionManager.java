package floottymod.floottymod.mixininterface;

import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public interface IClientPlayerInteractionManager {
	public float getCurrentBreakingProgress();
	public void sendPlayerActionC2SPacket(PlayerActionC2SPacket.Action action, BlockPos blockPos, Direction direction);
	public void setBlockHitDelay(int delay);
	
	public void windowClick_PICKUP(int slot);
	public void windowClick_QUICK_MOVE(int slot);
	public void windowClick_THROW(int slot);
}