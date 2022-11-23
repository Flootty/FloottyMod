package floottymod.floottymod;

import net.fabricmc.api.ModInitializer;

public class FloottyModInit implements ModInitializer {
    private static boolean initialized;

    @Override
    public void onInitialize() {
        if(initialized) throw new RuntimeException("onInitialize() ran twice!");
        FloottyMod.INSTANCE.initialize();
        initialized = true;
    }
}
