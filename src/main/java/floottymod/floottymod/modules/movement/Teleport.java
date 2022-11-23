package floottymod.floottymod.modules.movement;

import floottymod.floottymod.modules.Category;
import floottymod.floottymod.modules.Hack;
import floottymod.floottymod.modules.settings.SliderSetting;
import floottymod.floottymod.util.ChatUtils;
import floottymod.floottymod.util.PacketUtils;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.message.SentMessage;
import net.minecraft.util.math.Vec3d;

public class Teleport extends Hack {
    SliderSetting range = new SliderSetting("Range", 0, 0, 8, .5);

    public Teleport() {
        super("Teleport", Category.MOVEMENT);
        addSettings(range);
    }

    @Override
    public void onEnable() {
        Vec3d lookVec = MC.player.getRotationVector();
        double delta = calcDelta(lookVec);

        MC.player.addVelocity(15, 15, 15);
        PacketUtils.sendPosition(MC.player.getPos().add(lookVec.x * delta, lookVec.y * delta, lookVec.z * delta));
        setEnabled(false);
    }

    private double calcDelta(Vec3d lookVec) {
        return Math.sqrt(Math.pow(range.getValue(), 2) / (Math.pow(lookVec.getX(), 2) + Math.pow(lookVec.getY(), 2) + Math.pow(lookVec.getZ(), 2)));
    }
}
