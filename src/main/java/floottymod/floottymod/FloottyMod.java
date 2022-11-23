package floottymod.floottymod;

import floottymod.floottymod.event.EventManager;
import floottymod.floottymod.events.PostMotionListener;
import floottymod.floottymod.events.PreMotionListener;
import floottymod.floottymod.mixininterface.IMinecraftClient;
import floottymod.floottymod.modules.HackList;
import floottymod.floottymod.ui.screens.clickgui.ClickGui;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public enum FloottyMod {
    INSTANCE;

    public static final MinecraftClient MC = MinecraftClient.getInstance();
    public static final IMinecraftClient IMC = (IMinecraftClient)MC;

    public static final String name = "FloottyMod";
    public static final String version = "1.1";

    private EventManager eventManager;
    private HackList modList;

    public boolean enabled = true;

    private RotationFaker rotationFaker;

    public void initialize() {
        System.out.println("Loading " + name + " v" + version);

        eventManager = new EventManager(this);
        modList = new HackList();

        rotationFaker = new RotationFaker();
        eventManager.add(PreMotionListener.class, rotationFaker);
        eventManager.add(PostMotionListener.class, rotationFaker);
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public HackList getHackList() {
        return modList;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public RotationFaker getRotationFaker() {
        return rotationFaker;
    }
}
