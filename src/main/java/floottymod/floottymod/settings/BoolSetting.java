package floottymod.floottymod.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.setting.Setting;
import floottymod.floottymod.util.json.JsonUtils;

public class BoolSetting extends Setting {
    private boolean enabled;

    public BoolSetting(String name, boolean defaultValue) {
        super(name);
        this.enabled = defaultValue;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        FloottyMod.INSTANCE.saveSettings();
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    @Override
    public void fromJson(JsonElement json) {
        if(!JsonUtils.isBoolean(json)) return;

        setEnabled(json.getAsBoolean());
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(enabled);
    }
}
