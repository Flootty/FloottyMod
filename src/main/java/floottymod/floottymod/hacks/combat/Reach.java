package floottymod.floottymod.hacks.combat;

import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.events.LeftClickListener;
import floottymod.floottymod.events.TickListener;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.settings.SliderSetting;
import floottymod.floottymod.util.ChatUtils;
import floottymod.floottymod.util.PacketUtils;
import floottymod.floottymod.util.RotationUtils;
import net.minecraft.client.network.ChatPreviewer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.message.SentMessage;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Reach extends Hack implements TickListener, LeftClickListener {
    private SliderSetting range = new SliderSetting("Range", 4, 0, 100, 0.1);

    private Entity target;

    public Reach() {
        super("Reach", Category.COMBAT);
        addSettings(range);
    }

    @Override
    public void onEnable() {
        EVENTS.add(TickListener.class, this);
        EVENTS.add(LeftClickListener.class, this);
        target = null;
    }

    @Override
    public void onDisable() {
        EVENTS.remove(TickListener.class, this);
        EVENTS.remove(LeftClickListener.class, this);
    }

    @Override
    public void onTick() {
        if(MC.player == null) return;

        double rangeSq = Math.pow(range.getValue(), 2);
        Stream<Entity> stream = StreamSupport.stream(MC.world.getEntities().spliterator(), true)
                .filter(e -> !e.isRemoved())
                .filter(e -> e instanceof LivingEntity && ((LivingEntity)e).getHealth() > 0)
                .filter(e -> MC.player.squaredDistanceTo(e) <= rangeSq)
                .filter(e -> e != MC.player);

        target = stream.min(Comparator.comparingDouble(e -> RotationUtils.getAngleToLookVec(e.getBoundingBox().getCenter()))).orElse(null);
        if(target == null) return;
        if(RotationUtils.getAngleToLookVec(target.getBoundingBox().getCenter()) > 5) target = null;
    }

    @Override
    public void onLeftClick(LeftClickEvent event) {
        if(target == null) return;

        Vec3d prevPos = MC.player.getPos();
        teleportFromTo(MC.player.getPos(), target.getPos());

        ClientPlayerEntity player = MC.player;
        MC.interactionManager.attackEntity(player, target);
        player.swingHand(Hand.MAIN_HAND);

        teleportFromTo(MC.player.getPos(), prevPos);

        MC.player.setPosition(prevPos);

        target = null;
    }

    private void teleportFromTo(Vec3d fromPos, Vec3d toPos) {
        double distancePerBlink = 8.5;
        double targetDistance = Math.ceil(fromPos.distanceTo(toPos) / distancePerBlink);
        for(int i = 1; i <= targetDistance; i++) {
            Vec3d tempPos = fromPos.lerp(toPos, i / targetDistance);
            PacketUtils.sendPosition(tempPos);
            if(i % 4 == 0) {
                try {
                    Thread.sleep((long)((1/20) * 1000));
                } catch (InterruptedException e) {
                    ChatUtils.error("Sleep failed");
                }
            }
        }
    }
}
