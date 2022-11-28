package floottymod.floottymod.hacks.combat;

import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.events.PostMotionListener;
import floottymod.floottymod.events.TickListener;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.settings.BoolSetting;
import floottymod.floottymod.settings.ModeSetting;
import floottymod.floottymod.settings.SliderSetting;
import floottymod.floottymod.util.PacketUtils;
import floottymod.floottymod.util.RotationUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class TpAura extends Hack implements TickListener {
    public SliderSetting range = new SliderSetting("Range", 4, 0, 10, 0.1);
    public ModeSetting targetTypes = new ModeSetting("Targets", "All", "All", "Players", "Monsters", "Animals");
    public SliderSetting delay = new SliderSetting("Delay", 5, 0, 20, 1);
    public ModeSetting priority = new ModeSetting("Priority", "Distance", "Distance", "Angle", "Health");
    public BoolSetting rotate = new BoolSetting("Rotate Client", false);

    private int tickTimer;
    private Entity target;

    private final Random random = new Random();

    public TpAura() {
        super("TpAura", Category.COMBAT);
        addSettings(targetTypes, range, delay, priority, rotate);
        target = null;
    }

    @Override
    public void onEnable() {
        EVENTS.add(TickListener.class, this);
        tickTimer = 0;
    }

    @Override
    public void onDisable() {
        EVENTS.remove(TickListener.class, this);
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
                .filter(e -> e != MC.player);

        if(targetTypes.isMode("Players")) stream = stream.filter(e -> (e instanceof PlayerEntity));
        else if(targetTypes.isMode("Monsters")) stream = stream.filter(Predicate.not(e -> (e instanceof PlayerEntity))).filter(Predicate.not(e -> (e instanceof AnimalEntity)));
        else if(targetTypes.isMode("Animals")) stream = stream.filter(e -> (e instanceof AnimalEntity));

        if(priority.isMode("Distance")) target = stream.min((Priority.DISTANCE.comparator)).orElse(null);
        else if(priority.isMode("Angle")) target = stream.min((Priority.ANGLE.comparator)).orElse(null);
        else if(priority.isMode("Health")) target = stream.min((Priority.HEALTH.comparator)).orElse(null);

        if(target == null) return;

        if(delay.getValueInt() > 0 && tickTimer <= delay.getValue()) return;
        if (delay.getValueInt() == 0 && MC.player.getAttackCooldownProgress(0) < 1) return;

        Vec3d pos = MC.player.getPos();

        MC.player.setPosition(target.getX() + random.nextInt(3) * 2 - 2, target.getY(), target.getZ() + random.nextInt(3) * 2 - 2);

        if(rotate.isEnabled()) FloottyMod.INSTANCE.getRotationFaker().faceVectorClient(target.getEyePos());
        RotationUtils.Rotation rotations = RotationUtils.getNeededRotations(target.getEyePos());
        MC.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(rotations.getYaw(), rotations.getPitch(), MC.player.isOnGround()));

        MC.interactionManager.attackEntity(MC.player, target);
        MC.player.swingHand(Hand.MAIN_HAND);
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
