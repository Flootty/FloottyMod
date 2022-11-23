package floottymod.floottymod.events;

import floottymod.floottymod.event.Event;
import floottymod.floottymod.event.Listener;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public interface BlockBreakListener extends Listener {
    public void onBlockBreak(BlockPos pos, BlockState state, PlayerEntity player);

    public static class BlockBreakEvent extends Event<BlockBreakListener> {
        private BlockPos pos;
        private BlockState state;
        private PlayerEntity player;

        public BlockBreakEvent(BlockPos pos, BlockState state, PlayerEntity player) {
            this.pos = pos;
            this.state = state;
            this.player = player;
        }

        @Override
        public void fire(ArrayList<BlockBreakListener> listeners) {
            for(BlockBreakListener listener : listeners) listener.onBlockBreak(pos, state, player);
        }

        @Override
        public Class<BlockBreakListener> getListenerType() {
            return BlockBreakListener.class;
        }
    }
}
