package floottymod.floottymod.modules.combat;

import floottymod.floottymod.events.LeftClickListener;
import floottymod.floottymod.events.PostMotionListener;
import floottymod.floottymod.modules.Category;
import floottymod.floottymod.modules.Hack;
import floottymod.floottymod.modules.settings.ModeSetting;
import floottymod.floottymod.modules.settings.SliderSetting;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ClickAura extends Hack implements LeftClickListener, PostMotionListener {
	public SliderSetting range = new SliderSetting("Range", 4, 0, 6, 0.1);
	public ModeSetting targetTypes = new ModeSetting("Targets", "All", "All", "Players", "Monsters", "Animals");
	
	private Entity target;
	
	public ClickAura() {
		super( "ClickAura", Category.COMBAT);
		addSettings(targetTypes, range);
		target = null;
	}
	
	@Override
	public void onEnable() {
		EVENTS.add(LeftClickListener.class, this);
		EVENTS.add(PostMotionListener.class, this);
	}
	
	@Override
	public void onDisable() {
		EVENTS.remove(LeftClickListener.class, this);
		EVENTS.remove(PostMotionListener.class, this);
	}

	@Override
	public void onLeftClick(LeftClickEvent event) {
		double rangeSq = Math.pow(range.getValue(), 2);
		Stream<Entity> stream = StreamSupport.stream(MC.world.getEntities().spliterator(), true)
			.filter(e -> !e.isRemoved())
			.filter(e -> e instanceof LivingEntity && ((LivingEntity)e).getHealth() > 0 || e instanceof EndCrystalEntity)
			.filter(e -> MC.player.squaredDistanceTo(e) <= rangeSq)
			.filter(e -> e != MC.player);
			
		if(targetTypes.isMode("Players")) stream = stream.filter(e -> (e instanceof PlayerEntity));
		else if(targetTypes.isMode("Monsters")) stream = stream.filter(Predicate.not(e -> (e instanceof PlayerEntity))).filter(Predicate.not(e -> (e instanceof AnimalEntity)));
		else if(targetTypes.isMode("Animals")) stream = stream.filter(e -> (e instanceof AnimalEntity));
		
		target = stream.min(Comparator.comparingDouble(e -> e.squaredDistanceTo(MC.player))).orElse(null);
		
		if(target == null) return;
		
		MC.interactionManager.attackEntity(MC.player, target);
		MC.player.swingHand(Hand.MAIN_HAND);
	}
	
	@Override
	public void onPostMotion() {
		if(target == null) return;
	
		ClientPlayerEntity player = MC.player;
		MC.interactionManager.attackEntity(player, target);
		player.swingHand(Hand.MAIN_HAND);
		
		target = null;
	}
}
