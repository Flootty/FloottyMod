package floottymod.floottymod.ui.screens.clickgui;

import floottymod.floottymod.FloottyMod;
import floottymod.floottymod.hack.Category;
import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.ui.screens.clickgui.setting.Component;
import floottymod.floottymod.util.UIUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Frame {
	public int x, y, width, height, dragX, dragY, color, bgColor;
	public Category category;
	public boolean dragging, collapsed = true;
	private List<ModButton> buttons;
	
	protected MinecraftClient mc = MinecraftClient.getInstance();
	
	public Frame(Category category, int x, int y) {
		this.x = x;
		this.y = y;
		this.width =  (int) Math.floor(UIUtils.getFrame_width());
		this.height = (int) Math.floor(UIUtils.getOption_height());
		this.category = category;
		this.dragging = false;

		color = UIUtils.getColor();
		bgColor = UIUtils.getBgColor();
		
		buttons = new ArrayList<>();
		int offset = height;
		for(Hack m : FloottyMod.INSTANCE.getHackList().getAllModsByCategory(category)) {
			buttons.add(new ModButton(m, this, offset));
			offset += height;
		}
	}
	
	public void render(MatrixStack matricies, int mouseX, int mouseY, float delta) {

		DrawableHelper.fill(matricies, x, y, x + width, y + height, color); //Header
		mc.textRenderer.draw(matricies, category.name, x + 5, y + height / 2 - mc.textRenderer.fontHeight / 2, -1); //Title
		mc.textRenderer.draw(matricies, collapsed ? "+" : "-", x + width - 5 - mc.textRenderer.getWidth("+"), y + height / 2 - mc.textRenderer.fontHeight / 2, -1); //Collapsed indicator
		
		if(!collapsed) {
			for(ModButton b : buttons) b.render(matricies, mouseX, mouseY, delta);
		}
	}
	
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if(isHovered(mouseX, mouseY)) {
			if(button == 0) {
				dragging = true;
				dragX = (int) (mouseX - x);
				dragY = (int) (mouseY - y);
			} else if(button == 1) {
				collapsed = !collapsed;
			}
		}
		if(!collapsed) {
			for(ModButton b : buttons) b.mouseClicked(mouseX, mouseY, button);
		}
	}
	
	public void mouseReleased(double mouseX, double mouseY, int button) {
		if(button == 0 && dragging == true) dragging = false;
		
		for(ModButton b : buttons) b.mouseReleased(mouseX, mouseY, button);
	}
	
	public boolean isHovered(double mouseX, double mouseY) {
		return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
	}
	
	public void updatePosition(double mouseX, double mouseY) {
		if(dragging) {
			x = (int) (mouseX - dragX);
			y = (int) (mouseY - dragY);
		}
	}
	
	public void updateButtons() {
		int offset = height;
		for(ModButton b : buttons) {
			b.offset = offset;
			offset += height;
			
			if(b.extended) {
				for(Component c : b.components) if(c.setting.isVisible()) offset += height;
			}
		}
	}
}
