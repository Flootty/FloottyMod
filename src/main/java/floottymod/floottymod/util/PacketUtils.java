package floottymod.floottymod.util;

import floottymod.floottymod.mixin.ClientConnectionInvoker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

import static floottymod.floottymod.FloottyMod.MC;

public class PacketUtils {
    public static void sendPosition(Vec3d pos) {
        MinecraftClient MC = MinecraftClient.getInstance();
        ClientConnectionInvoker conn = (ClientConnectionInvoker) MC.player.networkHandler.getConnection();
        conn.sendIm(new PlayerMoveC2SPacket.PositionAndOnGround(pos.x, pos.y, pos.z, false), null);
    }

    public static void sendAttackEntity(Entity entity) {
        MinecraftClient MC = MinecraftClient.getInstance();
        ClientConnectionInvoker conn = (ClientConnectionInvoker) MC.player.networkHandler.getConnection();
        conn.sendIm(PlayerInteractEntityC2SPacket.attack(entity, MC.player.isSneaking()), null);
    }

    public static void sendRotation(float yaw, float pitch) {
        ClientConnectionInvoker conn = (ClientConnectionInvoker) MC.player.networkHandler.getConnection();
        conn.sendIm(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, MC.player.isOnGround()), null);
    }

    public static void sendOnGround(boolean onGround) {
        ClientConnectionInvoker conn = (ClientConnectionInvoker) MC.player.networkHandler.getConnection();
        conn.sendIm(new PlayerMoveC2SPacket.OnGroundOnly(onGround), null);
    }
}
