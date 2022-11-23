package floottymod.floottymod.modules;

import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.event.EventManager;
import floottymod.floottymod.modules.settings.Setting;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class Hack {
    protected static final EventManager EVENTS = FloottyMod.INSTANCE.getEventManager();
    protected static final MinecraftClient MC = FloottyMod.MC;

    private String name;
    private boolean enabled;
    private Category category;

    private List<Setting> settings = new ArrayList<>();

    public Hack(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public void addSetting(Setting setting) {
        settings.add(setting);
    }

    public void addSettings(Setting...settings) {
        for(Setting s : settings) addSetting(s);
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if(enabled) onEnable();
        else onDisable();
    }

    public Category getCategory() {
        return category;
    }

    public void toggle() {
        this.enabled = !this.enabled;
        if(enabled) onEnable();
        else onDisable();
    }

    public void onEnable() {

    }

    public void onDisable() {

    }
}
