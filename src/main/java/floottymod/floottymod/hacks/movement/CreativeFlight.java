package floottymod.floottymod.hacks.movement;

import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.events.TickListener;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.mixininterface.IKeyBinding;
import floottymod.floottymod.settings.BoolSetting;
import floottymod.floottymod.settings.SliderSetting;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.util.math.Vec3d;

public class CreativeFlight extends Hack implements TickListener {
    private BoolSetting antiKick = new BoolSetting("Anti kick", false);
    private SliderSetting antiKickInterval = new SliderSetting("Anti kick interval", 30, 5, 80, 1);

    private int tickCounter = 0;

    public CreativeFlight() {
        super("CreativeFlight", Category.MOVEMENT);
        addSettings(antiKick, antiKickInterval);
    }

    @Override
    public void onEnable() {
        tickCounter = 0;
        FloottyMod.INSTANCE.getHackList().flight.setEnabled(false, true);
        EVENTS.add(TickListener.class, this);
    }

    @Override
    public void onDisable() {
        EVENTS.remove(TickListener.class, this);

        ClientPlayerEntity player = MC.player;
        PlayerAbilities abilities = player.getAbilities();
        boolean creative = player.isCreative();

        abilities.flying = creative && !player.isCreative();
        abilities.allowFlying = creative;

        restoreKeyPresses();
    }

    @Override
    public void onTick() {
        if(MC.player == null) return;

        PlayerAbilities abilities = MC.player.getAbilities();
        abilities.allowFlying = true;

        if(antiKick.isEnabled() && abilities.flying) doAntiKick();
    }

    private void doAntiKick() {
        if(tickCounter > antiKickInterval.getValueInt()) tickCounter = 0;

        switch (tickCounter) {
            case 0 -> {
                if(MC.options.sneakKey.isPressed() && !MC.options.jumpKey.isPressed()) tickCounter = 3;
                else setMotionY(-0.07);
            }
            case 1 -> setMotionY(0.07);
            case 2 -> setMotionY(0);
            case 3 -> restoreKeyPresses();
        }

        tickCounter++;
    }

    private void setMotionY(double motionY) {
        MC.options.sneakKey.setPressed(false);
        MC.options.jumpKey.setPressed(false);

        Vec3d velocity = MC.player.getVelocity();
        MC.player.setVelocity(velocity.x, motionY, velocity.z);
    }

    private void restoreKeyPresses() {
        KeyBinding[] bindings = {MC.options.jumpKey, MC.options.sneakKey};
        for(KeyBinding binding : bindings) binding.setPressed(((IKeyBinding)binding).isActallyPressed());
    }
}
