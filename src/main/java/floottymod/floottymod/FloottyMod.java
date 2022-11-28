package floottymod.floottymod;

import floottymod.floottymod.event.EventManager;
import floottymod.floottymod.events.PostMotionListener;
import floottymod.floottymod.events.PreMotionListener;
import floottymod.floottymod.mixininterface.IMinecraftClient;
import floottymod.floottymod.hack.HackList;
import floottymod.floottymod.setting.SettingsFile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Environment(EnvType.CLIENT)
public enum FloottyMod {
    INSTANCE;

    public static final MinecraftClient MC = MinecraftClient.getInstance();
    public static final IMinecraftClient IMC = (IMinecraftClient)MC;

    public static final String name = "FloottyMod";
    public static final String version = "1.2.4";

    private Path floottyFolder;
    private SettingsFile settingsFile;
    private EventManager eventManager;
    private HackList hackList;

    public boolean enabled = true;

    private RotationFaker rotationFaker;

    public void initialize() {
        System.out.println("Loading " + name + " v" + version);

        eventManager = new EventManager(this);

        floottyFolder = createFloottyFolder();

        Path enabledHacksFile = floottyFolder.resolve("enabled-hacks.json");
        hackList = new HackList(enabledHacksFile);

        Path settingsFile = floottyFolder.resolve("settings.json");
        this.settingsFile = new SettingsFile(settingsFile, hackList);
        this.settingsFile.load();

        rotationFaker = new RotationFaker();
        eventManager.add(PreMotionListener.class, rotationFaker);
        eventManager.add(PostMotionListener.class, rotationFaker);
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public HackList getHackList() {
        return hackList;
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

    public void saveSettings() {
        settingsFile.save();
    }

    private Path createFloottyFolder() {
        Path dotMinecraftFolder = MC.runDirectory.toPath().normalize();
        Path floottyFolder = dotMinecraftFolder.resolve("FloottyMod");

        try {
            Files.createDirectories(floottyFolder);
        } catch(IOException e) {
            throw new RuntimeException("Couldn't create .minecraft/FloottyMod folder.", e);
        }

        return floottyFolder;
    }
}
