package floottymod.floottymod.hacks.movement;

import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.DontSaveState;
import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.settings.SliderSetting;
import floottymod.floottymod.util.PacketUtils;
import floottymod.floottymod.util.RotationUtils;
import net.minecraft.util.math.Vec3d;

@DontSaveState
public class Teleport extends Hack {
    SliderSetting range = new SliderSetting("Range", 0, 0, 10, .5);

    public Teleport() {
        super("Teleport", Category.MOVEMENT);
        addSettings(range);
    }

    @Override
    public void onEnable() {
        Vec3d lookVec;
        if(MC.isInSingleplayer()) lookVec = MC.player.getRotationVector();
        else lookVec = RotationUtils.getServerLookVec();

        double delta = calcDelta(lookVec);

        MC.player.addVelocity(15, 15, 15);
        PacketUtils.sendPosition(MC.player.getPos().add(lookVec.x * delta, lookVec.y * delta, lookVec.z * delta));
        setEnabled(false);
    }

    private double calcDelta(Vec3d lookVec) {
        return Math.sqrt(Math.pow(range.getValue(), 2) / (Math.pow(lookVec.getX(), 2) + Math.pow(lookVec.getY(), 2) + Math.pow(lookVec.getZ(), 2)));
    }
}
