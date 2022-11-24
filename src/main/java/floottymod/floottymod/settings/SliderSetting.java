package floottymod.floottymod.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.setting.Setting;
import floottymod.floottymod.util.json.JsonUtils;

public class SliderSetting extends Setting {
    private double value, min, max, increment;

    public SliderSetting(String name, double defaultValue, double min, double max, double increment) {
        super(name);
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.increment = increment;
    }

    public static double clamp(double value, double min, double max) {
        value = Math.max(min, value);
        value = Math.min(max, value);
        return value;
    }

    public double getValue() {
        return value;
    }
    public float getValueFloat() {
        return (float)value;
    }
    public int getValueInt() {
        return (int)value;
    }
    public void setValue(double value) {
        value = clamp(value, this.min, this.max);
        value = Math.round(value * (1 / this.increment)) / (1 / this.increment);
        this.value = value;

        FloottyMod.INSTANCE.saveSettings();
    }
    public void increment(boolean positive) {
        if(positive) setValue(this.value + this.increment);
        else setValue(this.value - this.increment);
    }
    public double getMax() {
        return max;
    }
    public double getMin() {
        return min;
    }

    @Override
    public void fromJson(JsonElement json) {
        if(!JsonUtils.isNumber(json)) return;

        double value = json.getAsDouble();
        if(value > max || value < min) return;

        setValue(value);
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(Math.round(value * 1e6) / 1e6);
    }
}
