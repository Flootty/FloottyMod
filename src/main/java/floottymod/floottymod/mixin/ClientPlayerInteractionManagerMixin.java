package floottymod.floottymod.mixin;

import floottymod.floottymod.event.EventManager;
import floottymod.floottymod.mixininterface.IClientPlayerInteractionManager;
import floottymod.floottymod.events.BlockBreakingProgressListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin implements IClientPlayerInteractionManager {
	@Shadow
	private MinecraftClient client;
	@Shadow
	private float currentBreakingProgress;
	@Shadow
	private boolean breakingBlock;
	@Shadow
	private int blockBreakingCooldown;
	
	@Inject(at = {@At(value = "INVOKE",
			target = "Lnet/minecraft/client/network/ClientPlayerEntity;getId()I",
			ordinal = 0)},
			method = {
				"updateBlockBreakingProgress(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z"})
	private void onPlayerDamageBlock(BlockPos blockPos_1, Direction direction_1, CallbackInfoReturnable<Boolean> cir) {
		BlockBreakingProgressListener.BlockBreakingProgressEvent event = new BlockBreakingProgressListener.BlockBreakingProgressEvent(blockPos_1, direction_1);
		EventManager.fire(event);
	}

	@Override
	public float getCurrentBreakingProgress() {
		return currentBreakingProgress;
	}

	@Override
	public void sendPlayerActionC2SPacket(Action action, BlockPos blockPos, Direction direction) {
		sendSequencedPacket(client.world, i -> new PlayerActionC2SPacket(action, blockPos, direction, i));
	}

	@Override
	public void setBlockHitDelay(int delay) {
		blockBreakingCooldown = delay;
	}
	
	@Shadow
	private void sendSequencedPacket(ClientWorld world, SequencedPacketCreator packetCreator) {
		
	}

	@Override
	public void windowClick_PICKUP(int slot) {
		clickSlot(0, slot, 0, SlotActionType.PICKUP, client.player);
	}

	@Override
	public void windowClick_QUICK_MOVE(int slot) {
		clickSlot(0, slot, 0, SlotActionType.QUICK_MOVE, client.player);
	}

	@Override
	public void windowClick_THROW(int slot) {
		clickSlot(0, slot, 0, SlotActionType.THROW, client.player);
	}
	
	@Shadow
	public abstract void clickSlot(int syncId, int slotId, int clickData, SlotActionType actionType, PlayerEntity playerEntity);
}
