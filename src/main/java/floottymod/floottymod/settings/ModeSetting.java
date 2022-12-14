package floottymod.floottymod.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.setting.Setting;
import floottymod.floottymod.util.json.JsonUtils;

import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting {
	private String mode;
	private List<String> modes;
	private int index;
	
	public ModeSetting(String name, String defaultMode, String... modes) {
		super(name);
		this.mode = defaultMode;
		this.modes = Arrays.asList(modes);
		this.index = this.modes.indexOf(defaultMode);
	}
	
	public String getMode() {
		return mode;
	}
	public List<String> getModes() {
		return modes;
	}
	public void setMode(String mode) {
		this.mode = mode;
		this.index = modes.indexOf(mode);

		FloottyMod.INSTANCE.saveSettings();
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
		this.mode = modes.get(index);
	}
	
	public void cycle() {
		if(index < modes.size() - 1) index++;
		else index = 0;
		mode = modes.get(index);
		setMode(mode);
	}
	
	public boolean isMode(String mode) {
		return this.mode == mode;
	}

	@Override
	public void fromJson(JsonElement json) {
		if(!JsonUtils.isNumber(json)) return;
		setMode(modes.get(json.getAsInt()));
	}

	@Override
	public JsonElement toJson() {
		return new JsonPrimitive(modes.indexOf(mode));
	}
}
