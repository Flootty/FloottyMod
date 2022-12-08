package floottymod.floottymod.hacks.qol;

import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.events.BlockBreakingProgressListener;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.settings.BoolSetting;
import floottymod.floottymod.util.BlockUtils;
import floottymod.floottymod.util.ChatUtils;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

import javax.tools.Tool;
import java.util.ArrayList;
import java.util.List;

public class AutoTool extends Hack implements BlockBreakingProgressListener {
    private BoolSetting preferFortune = new BoolSetting("Prefer fortune", false);
    private BoolSetting preferSilkTouch = new BoolSetting("Prefer silk touch", false);
    private BoolSetting onlyNecessary = new BoolSetting("Necessary", false);
    private BoolSetting preferHotbar = new BoolSetting("Hotbar", false);
    private BoolSetting preferDurability = new BoolSetting("Durability", false);

    private BlockPos targetBlockPos;

    public AutoTool() {
        super("AutoTool", Category.QOL);
        addSettings(preferFortune, preferSilkTouch, onlyNecessary, preferHotbar, preferDurability);
    }

    @Override
    public void onEnable() {
        EVENTS.add(BlockBreakingProgressListener.class, this);
        targetBlockPos = null;
    }

    @Override
    public void onDisable() {
        EVENTS.remove(BlockBreakingProgressListener.class, this);
    }

    @Override
    public void onBlocKBreakingProgress(BlockBreakingProgressEvent event) {
        //compare current block to last block
        BlockPos blockPos = event.getBlockPos();
        if(blockPos == targetBlockPos) return;
        targetBlockPos = blockPos;

        if(onlyNecessary.isEnabled() && MC.player.getInventory().getMainHandStack().isSuitableFor(BlockUtils.getState(blockPos))) return;

        //if changed get best tool
        int bestToolSlot = getBestToolSlot(event.getBlockPos());

        //if other tool -> swap tool
        if(MC.player.getInventory().selectedSlot == bestToolSlot || bestToolSlot == -1) return;
        swapToSlot(bestToolSlot);
    }

    private int getBestToolSlot(BlockPos blockPos) {
        PlayerInventory inventory = MC.player.getInventory();
        int selectedSlot = inventory.selectedSlot;
        ItemStack selectedStack = inventory.getStack(selectedSlot);
        BlockState state = BlockUtils.getState(blockPos);

        //Determine effective tools
        int bestToolValue = -1;
        int bestSlot = -1;
        for(int slot = 0; slot < 36; slot++) {
            ItemStack stack = inventory.getStack(slot);
            Item item = stack.getItem();
            if(!(item instanceof ToolItem) || stack.isEmpty()) continue;
            if(item.isSuitableFor(state)) {
                int toolValue = getToolValue(stack, slot);
                if(toolValue > bestToolValue) {
                    bestToolValue = toolValue;
                    bestSlot = slot;
                }
            }
        }
        return bestSlot;
    }

    private int getToolValue(ItemStack itemStack, int slot) {
        int fortune = preferFortune.isEnabled() ? EnchantmentHelper.getLevel(Enchantments.FORTUNE, itemStack) * 11 : EnchantmentHelper.getLevel(Enchantments.FORTUNE, itemStack);
        int silkTouch = preferSilkTouch.isEnabled() ? EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, itemStack) * 10 : EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, itemStack);
        int hotBar = preferHotbar.isEnabled() ? (slot > 9 ? 0 : 100) : 0;
        int durability = preferDurability.isEnabled() ? Math.round(itemStack.getDamage() * 1f / itemStack.getMaxDamage() * 100) : 0;

        return fortune + silkTouch + hotBar + durability;
    }

    private void swapToSlot(int slot) {
        PlayerInventory inventory = MC.player.getInventory();

        if(slot < 9) {
            inventory.selectedSlot = slot;
            return;
        }

        int firstEmptySlot = inventory.getEmptySlot();

        if(firstEmptySlot != -1) {
            if(firstEmptySlot >= 9) FloottyMod.IMC.getInteractionManager().windowClick_QUICK_MOVE(36 + inventory.selectedSlot);
            FloottyMod.IMC.getInteractionManager().windowClick_QUICK_MOVE(slot);
        } else {
            FloottyMod.IMC.getInteractionManager().windowClick_PICKUP(slot);
            FloottyMod.IMC.getInteractionManager().windowClick_PICKUP(36 + inventory.selectedSlot);
        }
    }
}
