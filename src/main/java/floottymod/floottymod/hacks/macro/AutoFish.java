package floottymod.floottymod.hacks.macro;

import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.events.PacketInputListener;
import floottymod.floottymod.events.TickListener;
import floottymod.floottymod.events.UpdateListener;
import floottymod.floottymod.mixininterface.IFishingBobberEntity;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.settings.SliderSetting;
import floottymod.floottymod.util.ChatUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundEvents;

public class AutoFish extends Hack implements TickListener, PacketInputListener {
	private SliderSetting validRange = new SliderSetting("Valid range", 1.5, .25, 8, 0.25);
	
	private int bestRodValue;
	private int bestRodSlot;
	
	private int castRodTimer;
	private int reelInTimer;
	private int scheduledWindowClick;
	
	private boolean wasOpenWater;
	
	public AutoFish() {
		super("AutoFish", Category.MACRO);
		addSettings(validRange);
	}
	
	@Override
	public void onEnable() {
		bestRodValue = -1;
		bestRodSlot = -1;
		castRodTimer = 0;
		reelInTimer = -1;
		scheduledWindowClick = -1;
		wasOpenWater = true;
		
		EVENTS.add(TickListener.class, this);
		EVENTS.add(PacketInputListener.class, this);
	}
	
	@Override
	public void onDisable() {
		EVENTS.remove(TickListener.class, this);
		EVENTS.remove(PacketInputListener.class, this);
	}

	@Override
	public void onTick() {
		if(reelInTimer > 0) reelInTimer--;
		
		ClientPlayerEntity player = MC.player;
		PlayerInventory inventory = player.getInventory();
		
		if(scheduledWindowClick != -1) {
			FloottyMod.IMC.getInteractionManager().windowClick_PICKUP(scheduledWindowClick);
			scheduledWindowClick = -1;
			castRodTimer = 15;
			return;
		}
		
		updateBestRod();
		
		if(bestRodSlot == -1) {
			ChatUtils.message("AutoFish has ran out of fishing rods.");
			setEnabled(false);
			return;
		}
		
		if(bestRodSlot != inventory.selectedSlot) {
			selectBestRod();
			return;
		}
		
		if(castRodTimer > 0) {
			castRodTimer--;
			return;
		}
		
		if(player.fishHook == null || player.fishHook.isRemoved()) {
			rightClick();
			castRodTimer = 15;
			reelInTimer = 1200;
		}
		
		if(reelInTimer == 0) {
			reelInTimer--;
			rightClick();
			castRodTimer = 15;
		}
	}
	
	private void updateBestRod() {
		PlayerInventory inventory = MC.player.getInventory();
		int selectedSlot = inventory.selectedSlot;
		ItemStack selectedStack = inventory.getStack(selectedSlot);
		
		bestRodValue = getRodValue(selectedStack);
		bestRodSlot = bestRodSlot > -1 ? selectedSlot : -1;
		
		for(int slot = 0; slot < 36; slot++) {
			ItemStack stack = inventory.getStack(slot);
			int rodValue = getRodValue(stack);
			
			if(rodValue > bestRodValue) {
				bestRodValue = rodValue;
				bestRodSlot = slot;
			}
		}
	}
	
	private int getRodValue(ItemStack stack) {
		if(stack.isEmpty() || !(stack.getItem() instanceof FishingRodItem)) return -1;
		
		int luckOTSLv1 = EnchantmentHelper.getLevel(Enchantments.LUCK_OF_THE_SEA, stack);
		int lureLv1 = EnchantmentHelper.getLevel(Enchantments.LURE, stack);
		int unbreakingLv1 = EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack);
		int mendingBonus = EnchantmentHelper.getLevel(Enchantments.MENDING, stack);
		int noVanishCurse = EnchantmentHelper.hasVanishingCurse(stack) ? 0 : 1;
		
		return luckOTSLv1 * 9 + lureLv1 * 9 + unbreakingLv1 * 2 + mendingBonus + noVanishCurse;
	}
	
	private void selectBestRod() {
		PlayerInventory inventory = MC.player.getInventory();
		
		if(bestRodSlot < 9) {
			inventory.selectedSlot = bestRodSlot;
			return;
		}
		
		int firstEmptySlot = inventory.getEmptySlot();
		
		if(firstEmptySlot != -1) {
			if(firstEmptySlot >= 9) FloottyMod.IMC.getInteractionManager().windowClick_QUICK_MOVE(36 + inventory.selectedSlot);
			FloottyMod.IMC.getInteractionManager().windowClick_QUICK_MOVE(bestRodSlot);
		} else {
			FloottyMod.IMC.getInteractionManager().windowClick_PICKUP(bestRodSlot);
			FloottyMod.IMC.getInteractionManager().windowClick_PICKUP(36 + inventory.selectedSlot);
			scheduledWindowClick = -bestRodSlot;
		}
	}

	@Override
	public void onReceivePacket(PacketInputEvent event) {
		ClientPlayerEntity player = MC.player;
		if(player == null || player.fishHook == null) return;
		
		if(!(event.getPacket() instanceof PlaySoundS2CPacket)) return;
		
		PlaySoundS2CPacket sound = (PlaySoundS2CPacket)event.getPacket();
		if(!SoundEvents.ENTITY_FISHING_BOBBER_SPLASH.equals(sound.getSound())) return;
		
		FishingBobberEntity bobber = player.fishHook;
		if(Math.abs(sound.getX() - bobber.getX()) > validRange.getValue() || Math.abs(sound.getZ() - bobber.getZ()) > validRange.getValue()) return;
		
		boolean isOpenWater = isInOpenWater(bobber);
		if(!isOpenWater && wasOpenWater) {
			ChatUtils.warning("You are currently fishing in shallow water.");
			ChatUtils.message("You can't get any treasure items while fishing like this.");
		}
		
		rightClick();
		castRodTimer = 15;
		wasOpenWater = isOpenWater;
	}
	
	private boolean isInOpenWater(FishingBobberEntity bobber) {
		return ((IFishingBobberEntity)bobber).checkOpenWaterAround(bobber.getBlockPos());
	}
	
	private void rightClick() {
		ItemStack stack = MC.player.getInventory().getMainHandStack();
		if(stack.isEmpty() || !(stack.getItem() instanceof FishingRodItem)) return;
		
		FloottyMod.IMC.rightClick();
	}
}
