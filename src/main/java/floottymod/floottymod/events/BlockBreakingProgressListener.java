package floottymod.floottymod.events;

import floottymod.floottymod.event.Event;
import floottymod.floottymod.event.Listener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;

public interface BlockBreakingProgressListener extends Listener {
	public void onBlocKBreakingProgress(BlockBreakingProgressEvent event);
	
	public static class BlockBreakingProgressEvent extends Event<BlockBreakingProgressListener> {
		private final BlockPos blockPos;
		private final Direction direction;
		
			public BlockBreakingProgressEvent(BlockPos blockPos, Direction direction) {
				this.blockPos = blockPos;
				this.direction = direction;
			}

			@Override
			public void fire(ArrayList<BlockBreakingProgressListener> listeners) {
				for(BlockBreakingProgressListener listener : listeners) listener.onBlocKBreakingProgress(this);
			}

			@Override
			public Class<BlockBreakingProgressListener> getListenerType() {
				return BlockBreakingProgressListener.class;
			}

			public BlockPos getBlockPos() {
				return blockPos;
			}

			public Direction getDirection() {
				return direction;
			}
	}
}
