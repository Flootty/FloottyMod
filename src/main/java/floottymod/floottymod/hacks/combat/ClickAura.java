package floottymod.floottymod.hacks.combat;

import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.events.LeftClickListener;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.settings.BoolSetting;
import floottymod.floottymod.settings.ModeSetting;
import floottymod.floottymod.settings.SliderSetting;
import floottymod.floottymod.util.PacketUtils;
import floottymod.floottymod.util.RotationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

import java.util.Comparator;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ClickAura extends Hack implements LeftClickListener {
	public SliderSetting range = new SliderSetting("Range", 4, 0, 6, 0.1);
	public ModeSetting targetTypes = new ModeSetting("Targets", "All", "All", "Players", "Monsters", "Animals");
	public ModeSetting priority = new ModeSetting("Priority", "Distance", "Distance", "Angle", "Health");
	public BoolSetting rotate = new BoolSetting("Rotate Client", false);
	
	private Entity target;
	
	public ClickAura() {
		super( "ClickAura", Category.COMBAT);
		addSettings(targetTypes, range, priority, rotate);
		target = null;
	}
	
	@Override
	public void onEnable() {
		EVENTS.add(LeftClickListener.class, this);
	}
	
	@Override
	public void onDisable() {
		EVENTS.remove(LeftClickListener.class, this);
	}

	@Override
	public void onLeftClick(LeftClickEvent event) {
		if(MC.player == null) return;

		double rangeSq = Math.pow(range.getValue(), 2);
		Stream<Entity> stream = StreamSupport.stream(MC.world.getEntities().spliterator(), true)
			.filter(e -> !e.isRemoved())
			.filter(e -> e instanceof LivingEntity && ((LivingEntity)e).getHealth() > 0)
			.filter(e -> MC.player.squaredDistanceTo(e) <= rangeSq)
			.filter(e -> e != MC.player)
			.filter(e -> !(e instanceof VillagerEntity))
			.filter(e -> !(e instanceof IronGolemEntity));;

		if(targetTypes.isMode("Players")) stream = stream.filter(e -> (e instanceof PlayerEntity));
		else if(targetTypes.isMode("Monsters")) stream = stream.filter(Predicate.not(e -> (e instanceof PlayerEntity))).filter(Predicate.not(e -> (e instanceof AnimalEntity)));
		else if(targetTypes.isMode("Animals")) stream = stream.filter(e -> (e instanceof AnimalEntity));

		if(priority.isMode("Distance")) target = stream.min((Priority.DISTANCE.comparator)).orElse(null);
		else if(priority.isMode("Angle")) target = stream.min((Priority.ANGLE.comparator)).orElse(null);
		else if(priority.isMode("Health")) target = stream.min((Priority.HEALTH.comparator)).orElse(null);

		if(target == null) return;

		if(target != null) {
			RotationUtils.Rotation rotation = RotationUtils.getNeededRotations(target.getEyePos());
			if(!rotate.isEnabled()) PacketUtils.sendRotation(rotation.getYaw(), rotation.getPitch());
			if(rotate.isEnabled()) FloottyMod.INSTANCE.getRotationFaker().faceVectorClient(target.getEyePos());
		}
		
		MC.interactionManager.attackEntity(MC.player, target);
		MC.player.swingHand(Hand.MAIN_HAND);

		target = null;
	}

	private enum Priority {
		DISTANCE("Distance", e -> MC.player.squaredDistanceTo(e)),
		ANGLE("Angle", e -> RotationUtils.getAngleToLookVec(e.getBoundingBox().getCenter())),
		HEALTH("Health", e -> e instanceof LivingEntity ? ((LivingEntity) e).getHealth() : Integer.MAX_VALUE);;

		private final String name;
		private final Comparator<Entity> comparator;

		private Priority(String name, ToDoubleFunction<Entity> keyExtractor) {
			this.name = name;
			comparator = Comparator.comparingDouble(keyExtractor);
		}

		@Override
		public String toString() {
			return name;
		}
	}
}
