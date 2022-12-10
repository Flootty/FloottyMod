package floottymod.floottymod.hacks.qol;

import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.settings.SliderSetting;

public class AutoReconnect extends Hack {
    private final SliderSetting waitTime = new SliderSetting("Wait time", 5, 0, 60, 0.5);

    public AutoReconnect() {
        super("AutoReconnect", Category.QOL);
        addSettings(waitTime);
    }

    public int getWaitTicks() {
        return waitTime.getValueInt() * 20;
    }

    //add Button to screen
}
