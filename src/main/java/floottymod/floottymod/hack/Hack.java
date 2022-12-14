package floottymod.floottymod.hack;

import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.event.EventManager;
import floottymod.floottymod.setting.Setting;
import floottymod.floottymod.util.ChatUtils;
import net.minecraft.client.MinecraftClient;

import java.util.*;

public class Hack {
    protected static final EventManager EVENTS = FloottyMod.INSTANCE.getEventManager();
    protected static final MinecraftClient MC = FloottyMod.MC;

    private String name;
    private boolean enabled;
    private Category category;

    private final boolean stateSaved = !getClass().isAnnotationPresent(DontSaveState.class);

    private final LinkedHashMap<String, Setting> settings = new LinkedHashMap<>();

    public boolean silent = false;

    public Hack(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public final Map<String, Setting> getSettings() {
        return Collections.unmodifiableMap(settings);
    }

    protected final void addSetting(Setting setting) {
        String key = setting.getName().toLowerCase();

        if(settings.containsKey(key)) throw new IllegalArgumentException("Duplicate setting: " + getName() + " " + key);

        settings.put(key, setting);
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

    public void setEnabled(boolean enabled, boolean silent) {
        this.enabled = enabled;
        if(enabled) onEnable();
        else onDisable();

        if(!silent)ChatUtils.message(name + (enabled ? " \u00a7aOn" : " \u00a7cOff"));

        if(stateSaved) FloottyMod.INSTANCE.getHackList().saveEnabledHacks();
    }

    public Category getCategory() {
        return category;
    }

    public void toggle(boolean silent) {
        setEnabled(!enabled, silent);
    }

    public void onEnable() {
    }

    public void onDisable() {

    }

    public final boolean isStateSaved() {
        return stateSaved;
    }
}
