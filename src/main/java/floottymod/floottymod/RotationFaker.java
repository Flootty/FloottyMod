package floottymod.floottymod;


import floottymod.floottymod.events.PostMotionListener;
import floottymod.floottymod.events.PreMotionListener;
import floottymod.floottymod.util.RotationUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

public final class RotationFaker implements PreMotionListener, PostMotionListener {
	private boolean fakeRotation;
	private float serverYaw;
	private float serverPitch;
	private float realYaw;
	private float realPitch;
	
	@Override
	public void onPreMotion() {
		if(!fakeRotation) return;
		
		ClientPlayerEntity player = FloottyMod.MC.player;
		realYaw = player.getYaw();
		realPitch = player.getPitch();
		player.setYaw(serverYaw);
		player.setPitch(serverPitch);
	}
	
	@Override
	public void onPostMotion() {
		if(!fakeRotation) return;
		
		ClientPlayerEntity player = FloottyMod.MC.player;
		player.setYaw(realYaw);
		player.setPitch(realPitch);
		fakeRotation = false;
	}
	
	public void faceVectorClient(Vec3d vec) {
		RotationUtils.Rotation rotations = RotationUtils.getNeededRotations(vec);

		FloottyMod.MC.player.setYaw(rotations.getYaw());
		FloottyMod.MC.player.setPitch(rotations.getPitch());
	}
	
	public void faceVectorClientIgnorePitch(Vec3d vec) {
		RotationUtils.Rotation rotations = RotationUtils.getNeededRotations(vec);

		FloottyMod.MC.player.setYaw(rotations.getYaw());
		FloottyMod.MC.player.setPitch(0);
	}
	
	public float getServerYaw() {
		return fakeRotation ? serverYaw : FloottyMod.MC.player.getYaw();
	}
	
	public float getServerPitch() {
		return fakeRotation ? serverPitch : FloottyMod.MC.player.getPitch();
	}
}