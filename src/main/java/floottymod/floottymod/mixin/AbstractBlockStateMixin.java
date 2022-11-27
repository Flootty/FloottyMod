package floottymod.floottymod.mixin;

import floottymod.floottymod.event.EventManager;
import floottymod.floottymod.events.GetAmbientOcclusionLightLevelListener.GetAmbientOcclusionLightLevelEvent;
import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
    @Shadow
    public abstract Block getBlock();

    @Inject(at = @At("TAIL"), method = "getAmbientOcclusionLightLevel", cancellable = true)
    private void onGetAmbientOcclusionLightLevel(BlockView blockView, BlockPos blockPos, CallbackInfoReturnable<Float> cir) {
        GetAmbientOcclusionLightLevelEvent event = new GetAmbientOcclusionLightLevelEvent((BlockState)(Object)this, cir.getReturnValueF());

        EventManager.fire(event);
        cir.setReturnValue(event.getLightLevel());
    }
}
