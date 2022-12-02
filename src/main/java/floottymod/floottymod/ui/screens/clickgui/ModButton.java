package floottymod.floottymod.ui.screens.clickgui;

import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.util.UIUtils;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

import static floottymod.floottymod.FloottyMod.MC;

public class ModButton {
	public Hack hack;
	public Frame parent;
	public int offset;
	public boolean extended;

	private int color, bgColor;
	
	public ModButton(Hack hack, Frame parent, int offset) {
		this.hack = hack;
		this.parent = parent;
		this.offset = offset;
		this.extended = false;

		color = UIUtils.getColor();
		bgColor = UIUtils.getBgColor();
	}
	
	public void render(MatrixStack matricies, int mouseX, int mouseY, float delta) {
		DrawableHelper.fill(matricies, parent.x, parent.y + offset, parent.x + parent.width, parent.y + offset + parent.height, new Color(0, 0, 0, 160).getRGB()); //Background
		if(isHovered(mouseX, mouseY)) DrawableHelper.fill(matricies, parent.x, parent.y + offset, parent.x + parent.width, parent.y + offset + parent.height, bgColor); //Hovered
		MC.textRenderer.draw(matricies, hack.getName(), parent.x + 5, parent.y + offset + 2 + MC.textRenderer.fontHeight / 2, hack.isEnabled() ? color : -1); //Name
	}
	
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if(isHovered(mouseX, mouseY)) {
			if(button == 0) hack.toggle(hack.silent);
			else if(button == 1) {
				if(hack.getSettings().isEmpty()) return;
				ClickGui.INSTANCE.drawFrames = false;
				ClickGui.INSTANCE.optionScreen.update(hack);
			}
		}
	}
	
	public boolean isHovered(double mouseX, double mouseY) {
		return mouseX > parent.x && mouseX < parent.x + parent.width && mouseY > parent.y + offset && mouseY < parent.y + offset + parent.height;
	}
}
