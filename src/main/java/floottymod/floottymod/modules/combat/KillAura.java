package floottymod.floottymod.modules.combat;

import com.ibm.icu.text.Normalizer2;
import floottymod.floottymod.events.PostMotionListener;
import floottymod.floottymod.events.UpdateListener;
import floottymod.floottymod.modules.Category;
import floottymod.floottymod.modules.Hack;
import floottymod.floottymod.modules.settings.ModeSetting;
import floottymod.floottymod.modules.settings.SliderSetting;
import floottymod.floottymod.util.ChatUtils;
import floottymod.floottymod.util.RotationUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import oshi.jna.platform.mac.SystemB;

import java.util.Comparator;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class KillAura extends Hack implements UpdateListener, PostMotionListener {
	public SliderSetting range = new SliderSetting("Range", 4, 0, 6, 0.1);
	public ModeSetting targetTypes = new ModeSetting("Targets", "All", "All", "Players", "Monsters", "Animals");
	public SliderSetting delay = new SliderSetting("Delay", 5, 0, 20, 1);
	public ModeSetting priority = new ModeSetting("Priority", "Distance", "Distance", "Angle", "Health");
	
	private Entity target;

	private int tickTimer;
	
	public KillAura() {
		super("KillAura", Category.COMBAT);
		addSettings(targetTypes, range, delay, priority);
		target = null;
	}
	
	@Override
	public void onEnable() {
		EVENTS.add(UpdateListener.class, this);
		EVENTS.add(PostMotionListener.class, this);
		tickTimer = 0;
	}
		
	@Override
	public void onDisable() {
		EVENTS.remove(UpdateListener.class, this);
		EVENTS.remove(PostMotionListener.class, this);
	}

	@Override
	public void onUpdate() {
		tickTimer++;

		double rangeSq = Math.pow(range.getValue(), 2);
		Stream<Entity> stream = StreamSupport.stream(MC.world.getEntities().spliterator(), true)
			.filter(e -> !e.isRemoved())
			.filter(e -> e instanceof LivingEntity && ((LivingEntity)e).getHealth() > 0 || e instanceof EndCrystalEntity)
			.filter(e -> MC.player.squaredDistanceTo(e) <= rangeSq)
			.filter(e -> e != MC.player);
			
		if(targetTypes.isMode("Players")) stream = stream.filter(e -> (e instanceof PlayerEntity));
		else if(targetTypes.isMode("Monsters")) stream = stream.filter(Predicate.not(e -> (e instanceof PlayerEntity))).filter(Predicate.not(e -> (e instanceof AnimalEntity)));
		else if(targetTypes.isMode("Animals")) stream = stream.filter(e -> (e instanceof AnimalEntity));

		if(priority.isMode("Distance")) stream = stream.sorted(Priority.DISTANCE.comparator);
		else if(priority.isMode("Angle")) stream = stream.sorted(Priority.ANGLE.comparator);
		else if(priority.isMode("Health")) stream = stream.sorted(Priority.HEALTH.comparator);
		
		target = stream.min(Comparator.comparingDouble(e -> e.squaredDistanceTo(MC.player))).orElse(null);
	}
	
	@Override
	public void onPostMotion() {
		if (target == null) return;
		if(delay.getValueInt() > 0 && tickTimer <= delay.getValue()) return;
		if (delay.getValueInt() == 0 && MC.player.getAttackCooldownProgress(0) < 1) return;

		ClientPlayerEntity player = MC.player;
		MC.interactionManager.attackEntity(player, target);
		player.swingHand(Hand.MAIN_HAND);
		tickTimer = 0;

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
