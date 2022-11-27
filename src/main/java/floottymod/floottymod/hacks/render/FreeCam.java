package floottymod.floottymod.hacks.render;

import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.events.*;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.DontSaveState;
import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.settings.BoolSetting;
import floottymod.floottymod.settings.SliderSetting;
import floottymod.floottymod.ui.screens.clickgui.ClickGui;
import floottymod.floottymod.util.MathUtils;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.DeathMessageS2CPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

@DontSaveState
public class FreeCam extends Hack implements TickListener, PacketOutputListener, PacketInputListener, KeyPressListener, MousePressListener, MouseScrollListener, DamageListener, SetOpaqueCubeListener {
    private SliderSetting speed = new SliderSetting("Speed", 1, 0.05, 10, 0.05);
    private SliderSetting speedScrollSensitivity = new SliderSetting("Sensitivity", 0, 0, 2, 0.1);
    private BoolSetting toggleOnDamage = new BoolSetting("Damage", false);
    private BoolSetting toggleOnDeath = new BoolSetting("Death", false);
    private BoolSetting reloadChunks = new BoolSetting("Caves", false);

    public Vec3d pos = Vec3d.ZERO;
    public Vec3d prevPos = Vec3d.ZERO;

    private Perspective perspective;
    private double speedValue;

    public float yaw, pitch;
    public float prevYaw, prevPitch;

    private double fovScale;

    private boolean forward, backward, right, left, up, down;

    public FreeCam() {
        super("FreeCam", Category.RENDER);
        addSettings(speed, speedScrollSensitivity, toggleOnDamage, toggleOnDeath, reloadChunks);
    }

    @Override
    public void onEnable() {
        fovScale = MC.options.getFovEffectScale().getValue();

        yaw = MC.player.getYaw();
        pitch = MC.player.getPitch();

        perspective = MC.options.getPerspective();
        speedValue = speed.getValue();

        pos = MC.gameRenderer.getCamera().getPos();
        prevPos = pos;

        prevYaw = yaw;
        prevPitch = pitch;

        forward = false;
        backward = false;
        right = false;
        left = false;
        up = false;
        down = false;

        unpress();

        if(reloadChunks.isEnabled()) MC.worldRenderer.reload();

        EVENTS.add(TickListener.class, this);
        EVENTS.add(PacketOutputListener.class, this);
        EVENTS.add(KeyPressListener.class, this);
        EVENTS.add(MousePressListener.class, this);
        EVENTS.add(MouseScrollListener.class, this);
    }

    @Override
    public void onDisable() {
        if(reloadChunks.isEnabled()) MC.worldRenderer.reload();
        if(perspective != null) MC.options.setPerspective(perspective);

        EVENTS.remove(TickListener.class, this);
        EVENTS.remove(PacketOutputListener.class, this);
        EVENTS.remove(KeyPressListener.class, this);
        EVENTS.remove(MousePressListener.class, this);
        EVENTS.remove(MouseScrollListener.class, this);
    }

    @Override
    public void onSentPacket(PacketOutputEvent event) {
        if(event.getPacket() instanceof PlayerMoveC2SPacket) event.cancel();
    }

    @Override
    public void onTick() {
        if(guiOpen()) return;

        if (MC.cameraEntity.isInsideWall()) MC.getCameraEntity().noClip = true;
        if(perspective == null) return;
        if (!perspective.isFirstPerson()) MC.options.setPerspective(Perspective.FIRST_PERSON);

        speedValue = speed.getValue();

        Vec3d forward = Vec3d.fromPolar(0, yaw);
        Vec3d right = Vec3d.fromPolar(0, yaw + 90);
        double velX = 0;
        double velY = 0;
        double velZ = 0;

        double s = 0.5;
        if (MC.options.sprintKey.isPressed()) s = 1;

        boolean a = false;
        if (this.forward) {
            velX += forward.x * s * speedValue;
            velZ += forward.z * s * speedValue;
            a = true;
        }
        if (this.backward) {
            velX -= forward.x * s * speedValue;
            velZ -= forward.z * s * speedValue;
            a = true;
        }

        boolean b = false;
        if (this.right) {
            velX += right.x * s * speedValue;
            velZ += right.z * s * speedValue;
            b = true;
        }
        if (this.left) {
            velX -= right.x * s * speedValue;
            velZ -= right.z * s * speedValue;
            b = true;
        }

        if (a && b) {
            double diagonal = 1 / Math.sqrt(2);
            velX *= diagonal;
            velZ *= diagonal;
        }

        if (this.up) {
            velY += s * speedValue;
        }
        if (this.down) {
            velY -= s * speedValue;
        }

        prevPos = pos;
        pos = new Vec3d(pos.x + velX, pos.y + velY, pos.z + velZ);
    }

    private void unpress() {
        MC.options.forwardKey.setPressed(false);
        MC.options.backKey.setPressed(false);
        MC.options.rightKey.setPressed(false);
        MC.options.leftKey.setPressed(false);
        MC.options.jumpKey.setPressed(false);
        MC.options.sneakKey.setPressed(false);
    }

    public double getX(float tickDelta) {
        return MathHelper.lerp(tickDelta, prevPos.x, pos.x);
    }
    public double getY(float tickDelta) {
        return MathHelper.lerp(tickDelta, prevPos.y, pos.y);
    }
    public double getZ(float tickDelta) {
        return MathHelper.lerp(tickDelta, prevPos.z, pos.z);
    }

    public double getYaw(float tickDelta) {
        return MathHelper.lerp(tickDelta, prevYaw, yaw);
    }
    public double getPitch(float tickDelta) {
        return MathHelper.lerp(tickDelta, prevPitch, pitch);
    }

    @Override
    public void onKey(KeyPressEvent event) {
        if (event.key == GLFW.GLFW_KEY_F3) return;

        boolean cancel = true;

        if (MC.options.forwardKey.matchesKey(event.key, 0)) {
            forward = event.action != GLFW.GLFW_RELEASE;
            MC.options.forwardKey.setPressed(false);
        } else if (MC.options.backKey.matchesKey(event.key, 0)) {
            backward = event.action != GLFW.GLFW_RELEASE;
            MC.options.backKey.setPressed(false);
        } else if (MC.options.rightKey.matchesKey(event.key, 0)) {
            right = event.action != GLFW.GLFW_RELEASE;
            MC.options.rightKey.setPressed(false);
        } else if (MC.options.leftKey.matchesKey(event.key, 0)) {
            left = event.action != GLFW.GLFW_RELEASE;
            MC.options.leftKey.setPressed(false);
        } else if (MC.options.jumpKey.matchesKey(event.key, 0)) {
            up = event.action != GLFW.GLFW_RELEASE;
            MC.options.jumpKey.setPressed(false);
        } else if (MC.options.sneakKey.matchesKey(event.key, 0)) {
            down = event.action != GLFW.GLFW_RELEASE;
            MC.options.sneakKey.setPressed(false);
        } else if (MC.options.togglePerspectiveKey.matchesKey(event.key, 0)) {
            MC.options.togglePerspectiveKey.setPressed(false);
        } else {
            cancel = false;
        }

        if (cancel) event.cancel();
    }

    @Override
    public void onMousePress(MousePressEvent event) {
        boolean cancel = true;

        if (MC.options.forwardKey.matchesMouse(event.key)) {
            forward = event.action != GLFW.GLFW_RELEASE;
            MC.options.forwardKey.setPressed(false);
        } else if (MC.options.backKey.matchesMouse(event.key)) {
            backward = event.action != GLFW.GLFW_RELEASE;
            MC.options.backKey.setPressed(false);
        } else if (MC.options.rightKey.matchesMouse(event.key)) {
            right = event.action != GLFW.GLFW_RELEASE;
            MC.options.rightKey.setPressed(false);
        } else if (MC.options.leftKey.matchesMouse(event.key)) {
            left = event.action != GLFW.GLFW_RELEASE;
            MC.options.leftKey.setPressed(false);
        } else if (MC.options.jumpKey.matchesMouse(event.key)) {
            up = event.action != GLFW.GLFW_RELEASE;
            MC.options.jumpKey.setPressed(false);
        } else if (MC.options.sneakKey.matchesMouse(event.key)) {
            down = event.action != GLFW.GLFW_RELEASE;
            MC.options.sneakKey.setPressed(false);
        } else if (MC.options.togglePerspectiveKey.matchesMouse(event.key)) {
            MC.options.togglePerspectiveKey.setPressed(false);
        } else {
            cancel = false;
        }

        if (cancel) event.cancel();
    }

    @Override
    public void onMouseScroll(MouseScrollEvent event) {
        if (speedScrollSensitivity.getValue() > 0) {
            speedValue += event.value * 0.25 * (speedScrollSensitivity.getValue() * speedValue);
            speedValue = MathUtils.clamp(speedValue, speed.getMin(), speed.getMax());
            speed.setValue(speedValue);

            event.cancel();
        }
    }

    @Override
    public void onReceivePacket(PacketInputEvent event) {
        if(event.getPacket() instanceof DeathMessageS2CPacket packet) {
            Entity entity = MC.world.getEntityById(packet.getEntityId());
            if (entity == MC.player && toggleOnDeath.isEnabled()) toggle(false);
        }
    }

    @Override
    public void onDamage(DamageEvent event) {
        if(event.source.getSource().getUuid() == null) return;
        if(!event.source.getSource().getUuid().equals(MC.player.getUuid())) return;

        if(toggleOnDamage.isEnabled()) toggle(false);
    }

    @Override
    public void onSetOpaqueCubeListener(SetOpaqueCubeEvent event) {
        event.cancel();
    }

    public void changeLookDirection(double deltaX, double deltaY) {
        prevYaw = yaw;
        prevPitch = pitch;

        yaw += deltaX;
        pitch += deltaY;

        pitch = MathHelper.clamp(pitch, -90, 90);
    }

    private boolean guiOpen() {
        if (MC.currentScreen != null && !(MC.currentScreen instanceof ClickGui)) return true;
        return false;
    }
}
