package floottymod.floottymod.hacks.combat;

import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.RotationFaker;
import floottymod.floottymod.events.PacketInputListener;
import floottymod.floottymod.events.PostMotionListener;
import floottymod.floottymod.events.TickListener;
import floottymod.floottymod.events.UpdateListener;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.settings.BoolSetting;
import floottymod.floottymod.settings.ModeSetting;
import floottymod.floottymod.settings.SliderSetting;
import floottymod.floottymod.util.ChatUtils;
import floottymod.floottymod.util.PacketUtils;
import floottymod.floottymod.util.RotationUtils;
import floottymod.floottymod.util.RotationUtils.Rotation;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.Hand;
import org.apache.logging.log4j.core.net.Priority;

import java.util.Comparator;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class KillAura extends Hack implements TickListener, PostMotionListener {
	public SliderSetting range = new SliderSetting("Range", 4, 0, 6, 0.1);
	public ModeSetting targetTypes = new ModeSetting("Targets", "All", "All", "Players", "Monsters", "Animals");
	public SliderSetting delay = new SliderSetting("Delay", 5, 0, 20, 1);
	public ModeSetting priority = new ModeSetting("Priority", "Distance", "Distance", "Angle", "Health");
	public BoolSetting crit = new BoolSetting("Critical", true);
	public BoolSetting name = new BoolSetting("Exclude NameTags", true);
	public BoolSetting rotate = new BoolSetting("Rotate Client", false);
	
	private Entity target;
	private Critical critical;

	private int tickTimer;
	
	public KillAura() {
		super("KillAura", Category.COMBAT);
		addSettings(targetTypes, range, delay, priority, crit, name, rotate);
		target = null;
	}
	
	@Override
	public void onEnable() {
		EVENTS.add(TickListener.class, this);
		EVENTS.add(PostMotionListener.class, this);
		critical = FloottyMod.INSTANCE.getHackList().critical;
		tickTimer = 0;
	}
		
	@Override
	public void onDisable() {
		EVENTS.remove(TickListener.class, this);
		EVENTS.remove(PostMotionListener.class, this);
	}

	@Override
	public void onTick() {
		if(MC.player == null) return;

		tickTimer++;

		double rangeSq = Math.pow(range.getValue(), 2);
		Stream<Entity> stream = StreamSupport.stream(MC.world.getEntities().spliterator(), true)
			.filter(e -> !e.isRemoved())
			.filter(e -> e instanceof LivingEntity && ((LivingEntity)e).getHealth() > 0)
			.filter(e -> MC.player.squaredDistanceTo(e) <= rangeSq)
			.filter(e -> e != MC.player)
			.filter(e -> !(e instanceof VillagerEntity))
			.filter(e -> !(e instanceof IronGolemEntity))
			.filter(e -> !(e instanceof ArmorStandEntity))
			.filter(e -> !(e instanceof AllayEntity));

		if(name.isEnabled()) stream = stream.filter(e -> e.getCustomName() == null);

		if(targetTypes.isMode("Players")) stream = stream.filter(e -> (e instanceof PlayerEntity));
		else if(targetTypes.isMode("Monsters")) stream = stream.filter(Predicate.not(e -> (e instanceof PlayerEntity))).filter(Predicate.not(e -> (e instanceof AnimalEntity)));
		else if(targetTypes.isMode("Animals")) stream = stream.filter(e -> (e instanceof AnimalEntity));

		if(priority.isMode("Distance")) target = stream.min((Priority.DISTANCE.comparator)).orElse(null);
		else if(priority.isMode("Angle")) target = stream.min((Priority.ANGLE.comparator)).orElse(null);
		else if(priority.isMode("Health")) target = stream.min((Priority.HEALTH.comparator)).orElse(null);

		if(target != null) {
			if(target.getCustomName() != null) target = null;
			Rotation rotation = RotationUtils.getNeededRotations(target.getEyePos());
			if(!rotate.isEnabled()) PacketUtils.sendRotation(rotation.getYaw(), rotation.getPitch());
		}
	}

	@Override
	public void onPostMotion() {
		if (target == null) return;
		if(delay.getValueInt() > 0 && tickTimer <= delay.getValue()) return;
		if (delay.getValueInt() == 0 && MC.player.getAttackCooldownProgress(0) < 1) return;

		ClientPlayerEntity player = MC.player;
		if(rotate.isEnabled()) FloottyMod.INSTANCE.getRotationFaker().faceVectorClient(target.getEyePos());
		critical.doCritical();
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
