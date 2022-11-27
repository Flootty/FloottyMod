package floottymod.floottymod.hacks.movement;

import floottymod.floottymod.events.TickListener;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.settings.BoolSetting;
import floottymod.floottymod.settings.SliderSetting;
import floottymod.floottymod.util.ChatUtils;
import floottymod.floottymod.util.PacketUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.tick.Tick;

public class BoatFly extends Hack implements TickListener {
    private BoolSetting changeForwardSpeed = new BoolSetting("Change speed", false);
    private SliderSetting forwardSpeed = new SliderSetting("Forward", 1, 0.05, 5, 0.05);
    private SliderSetting upwardSpeed = new SliderSetting("Upward", 0.3, 0, 5, 0.05);

    private int ticks;
    private int INTERVAL = 40;

    public BoatFly() {
        super("BoatFly", Category.MOVEMENT);
        addSettings(changeForwardSpeed, forwardSpeed, upwardSpeed);
    }

    @Override
    public void onEnable() {
        EVENTS.add(TickListener.class, this);
        ticks = 0;
    }

    @Override
    public void onDisable() {
        EVENTS.remove(TickListener.class, this);
    }

    @Override
    public void onTick() {
        if(MC.player == null) return;
        if(!MC.player.hasVehicle()) return;

        Entity vehicle = MC.player.getVehicle();
        Vec3d velocity = vehicle.getVelocity();

        double motionX = velocity.x;
        double motionY = 0;
        double motionZ = velocity.z;

        if(MC.options.jumpKey.isPressed()) motionY = upwardSpeed.getValue();
        else if(MC.options.sprintKey.isPressed()) motionY = -upwardSpeed.getValue();

        if(ticks >= 40 && vehicle.getVelocity().getY() > 0.1) {
            motionY = -.001;
            ticks = 0;
        }

        if(MC.options.forwardKey.isPressed() && changeForwardSpeed.isEnabled()) {
            double speed = forwardSpeed.getValue();
            float yawRad = vehicle.getYaw() * MathHelper.RADIANS_PER_DEGREE;

            motionX = MathHelper.sin(-yawRad) * speed;
            motionZ = MathHelper.cos(yawRad) * speed;
        }

        vehicle.setVelocity(motionX, motionY, motionZ);
        ticks++;
    }
}
