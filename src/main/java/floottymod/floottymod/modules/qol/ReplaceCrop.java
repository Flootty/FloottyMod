package floottymod.floottymod.modules.qol;

import floottymod.floottymod.modules.Category;
import floottymod.floottymod.modules.Hack;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.*;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ReplaceCrop extends Hack {
    public ReplaceCrop() {
        super("Replace Crop", Category.QOL);
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if(!isEnabled()) return ActionResult.PASS;
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = hitResult.getBlockPos();
                ItemStack heldStack = player.getMainHandStack();
                ItemStack offhandStack = player.getOffHandStack();
                if (hand == Hand.MAIN_HAND && !heldStack.isEmpty()) {
                    this.replaceCrop(heldStack, pos, world);
                } else if (hand == Hand.OFF_HAND && !offhandStack.isEmpty()) {
                    this.replaceCrop(offhandStack, pos, world);
                }
            }

            return ActionResult.PASS;
        });
    }

    private void replaceCrop(ItemStack stack, BlockPos pos, World world) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        ClientPlayerInteractionManager interactionManager = MC.interactionManager;
        if (stack.getItem() instanceof BlockItem) {
            Block itemBlock = ((BlockItem)stack.getItem()).getBlock();
            if (block instanceof CropBlock) {
                if (itemBlock instanceof CropBlock && ((CropBlock)block).isMature(state)) {
                    interactionManager.attackBlock(pos, Direction.DOWN);
                }
            } else {
                if (block instanceof NetherWartBlock) {
                    NetherWartBlock wartBlock = (NetherWartBlock)block;
                    if (block == itemBlock) {
                        if (wartBlock.getOutlineShape(state, world, pos, ShapeContext.absent()).getBoundingBox().getYLength() * 16.0 == 14.0) {
                            interactionManager.attackBlock(pos, Direction.DOWN);
                        }

                        return;
                    }
                }

                if (block instanceof CocoaBlock) {
                    CocoaBlock cocoaBlock = (CocoaBlock)block;
                    if (cocoaBlock.getOutlineShape(state, world, pos, ShapeContext.absent()).getBoundingBox().getYLength() == 0.5625) {
                        interactionManager.attackBlock(pos, (Direction)world.getBlockState(pos).get(CocoaBlock.FACING));
                    }
                }
            }
        }

    }
}
