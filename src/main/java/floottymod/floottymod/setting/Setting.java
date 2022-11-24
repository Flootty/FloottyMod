package floottymod.floottymod.setting;

import com.google.gson.JsonElement;

import java.util.Objects;

public abstract class Setting {
	private String name;
	private boolean visible = true;
	
	public Setting(String name) {
		this.name = Objects.requireNonNull(name);
	}
	
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public String getName() {
		return name;
	}

	public abstract void fromJson(JsonElement json);

	public abstract JsonElement toJson();
}
