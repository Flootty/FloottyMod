package floottymod.floottymod.ui.screens.clickgui;

import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.settings.BoolSetting;
import floottymod.floottymod.settings.ModeSetting;
import floottymod.floottymod.setting.Setting;
import floottymod.floottymod.settings.SliderSetting;
import floottymod.floottymod.ui.screens.clickgui.setting.CheckBox;
import floottymod.floottymod.ui.screens.clickgui.setting.Component;
import floottymod.floottymod.ui.screens.clickgui.setting.ModeBox;
import floottymod.floottymod.ui.screens.clickgui.setting.Slider;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModButton {
	public Hack hack;
	public Frame parent;
	public int offset;
	public List<Component> components;
	public boolean extended;
	
	public ModButton(Hack hack, Frame parent, int offset) {
		this.hack = hack;
		this.parent = parent;
		this.offset = offset;
		this.components = new ArrayList<>();
		this.extended = false;
		
		int setOffset = parent.height;
		for(Setting s : hack.getSettings().values()) {
			if(s instanceof BoolSetting) {
				components.add(new CheckBox(s, this, setOffset));
			} else if(s instanceof ModeSetting) {
				components.add(new ModeBox(s, this, setOffset));
			} else if(s instanceof SliderSetting) {
				components.add(new Slider(s, this, setOffset));
			}
			setOffset += parent.height;
		}
	}
	
	public void render(MatrixStack matricies, int mouseX, int mouseY, float delta) {
		DrawableHelper.fill(matricies, parent.x, parent.y + offset, parent.x + parent.width, parent.y + offset + parent.height, new Color(0, 0, 0, 160).getRGB());
		if(isHovered(mouseX, mouseY)) DrawableHelper.fill(matricies, parent.x, parent.y + offset, parent.x + parent.width, parent.y + offset + parent.height, new Color(0, 0, 0, 180).getRGB());
		parent.mc.textRenderer.draw(matricies, hack.getName(), parent.x + 5, parent.y + offset + 2 + parent.mc.textRenderer.fontHeight / 2, hack.isEnabled() ? Color.red.getRGB() : -1);
		
		if(extended) for(Component c : components) c.render(matricies, mouseX, mouseY, delta);
	}
	
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if(isHovered(mouseX, mouseY)) {
			if(button == 0) hack.toggle(false);
			else if(button == 1) {
				extended = !extended;
				parent.updateButtons();
			}
		}
		for(Component c : components) c.mouseClicked(mouseX, mouseY, button);
	}
	
	public void mouseReleased(double mouseX, double mouseY, int button) {
		for(Component c : components) c.mouseReleased(mouseX, mouseY, button);
	}
	
	public boolean isHovered(double mouseX, double mouseY) {
		return mouseX > parent.x && mouseX < parent.x + parent.width && mouseY > parent.y + offset && mouseY < parent.y + offset + parent.height;
	}
}
