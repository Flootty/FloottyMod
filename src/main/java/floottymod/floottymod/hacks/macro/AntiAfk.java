package floottymod.floottymod.hacks.macro;

import floottymod.floottymod.events.TickListener;
import floottymod.floottymod.events.UpdateListener;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.util.ChatUtils;
import net.minecraft.network.message.SentMessage;

public class AntiAfk extends Hack implements TickListener {
    private int PERIOD = 40;
    private int ticks = 0;
    private int dir = 0;

    public AntiAfk() {
        super("AntiAFK", Category.MACRO);
    }

    @Override
    public void onEnable() {
        EVENTS.add(TickListener.class, this);
        ticks = 0;
        dir = 0;
    }

    @Override
    public void onDisable() {
        EVENTS.remove(TickListener.class, this);
    }

    @Override
    public void onTick() {
        if(MC.player == null) return;

        ticks++;
        if(ticks >= PERIOD) {
            if(dir == 0) MC.player.addVelocity(.5, 0, 0);
            else if(dir == 1) MC.player.addVelocity(-.25, 0, .5);
            else if(dir == 2) MC.player.addVelocity(-.25, 0, -.5);

            dir++;
            if(dir > 2) dir = 0;
            ticks = 0;
        }
    }
}
