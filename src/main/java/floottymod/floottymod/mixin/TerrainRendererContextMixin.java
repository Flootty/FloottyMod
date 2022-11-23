package floottymod.floottymod.mixin;

import floottymod.floottymod.event.EventManager;
import floottymod.floottymod.events.TesselateBlockListener.TesselateBlockEvent;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.TerrainRenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TerrainRenderContext.class)
public class TerrainRendererContextMixin {
    @Inject(at = @At("HEAD"), method = "tessellateBlock", cancellable = true, remap = false)
    private void tesselateBlock(BlockState blockState, BlockPos blockPos, final BakedModel model, MatrixStack matrixStack, CallbackInfoReturnable<Boolean> cir) {
        TesselateBlockEvent event = new TesselateBlockEvent(blockState);
        EventManager.fire(event);

        if(event.isCancelled()) cir.cancel();
    }
}
