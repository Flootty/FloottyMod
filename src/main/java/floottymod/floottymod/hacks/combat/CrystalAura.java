package floottymod.floottymod.hacks.combat;

import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.events.PostMotionListener;
import floottymod.floottymod.events.UpdateListener;
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
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.Hand;

import java.util.Comparator;
import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CrystalAura extends Hack implements UpdateListener, PostMotionListener {
    public SliderSetting range = new SliderSetting("Range", 4, 0, 6, 0.1);
    public ModeSetting priority = new ModeSetting("Priority", "Distance", "Distance", "Angle");
    public BoolSetting rotate = new BoolSetting("Rotate Client", false);

    private Entity target;

    public CrystalAura() {
        super("CristalAura", Category.COMBAT);
        addSettings(range, priority, rotate);
        target = null;
    }

    @Override
    public void onEnable() {
        EVENTS.add(UpdateListener.class, this);
        EVENTS.add(PostMotionListener.class, this);
    }

    @Override
    public void onDisable() {
        EVENTS.remove(UpdateListener.class, this);
        EVENTS.remove(PostMotionListener.class, this);
    }

    @Override
    public void onUpdate() {
        double rangeSq = Math.pow(range.getValue(), 2);
        Stream<Entity> stream = StreamSupport.stream(MC.world.getEntities().spliterator(), true)
                .filter(e -> MC.player.squaredDistanceTo(e) <= rangeSq)
                .filter(e -> e instanceof EndCrystalEntity);

        if(priority.isMode("Distance")) target = stream.min((Priority.DISTANCE.comparator)).orElse(null);
        else if(priority.isMode("Angle")) target = stream.min((Priority.ANGLE.comparator)).orElse(null);

        if(target != null) {
            RotationUtils.Rotation rotation = RotationUtils.getNeededRotations(target.getEyePos());
            if(!rotate.isEnabled()) PacketUtils.sendRotation(rotation.getYaw(), rotation.getPitch());
        }
    }

    @Override
    public void onPostMotion() {
        if (target == null) return;

        ClientPlayerEntity player = MC.player;
        if(rotate.isEnabled()) FloottyMod.INSTANCE.getRotationFaker().faceVectorClient(target.getEyePos());
        MC.interactionManager.attackEntity(player, target);
        player.swingHand(Hand.MAIN_HAND);

        target = null;
    }

    private enum Priority {
        DISTANCE("Distance", e -> MC.player.squaredDistanceTo(e)),
        ANGLE("Angle", e -> RotationUtils.getAngleToLookVec(e.getBoundingBox().getCenter()));

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
