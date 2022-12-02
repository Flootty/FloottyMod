package floottymod.floottymod.ui.screens.clickgui.setting;

import floottymod.floottymod.setting.Setting;
import floottymod.floottymod.settings.BoolSetting;
import floottymod.floottymod.settings.SliderSetting;
import floottymod.floottymod.ui.screens.clickgui.Component;
import floottymod.floottymod.ui.screens.clickgui.ModButton;
import floottymod.floottymod.util.UIUtils;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Slider extends Component {
	public SliderSetting numSet;
	private boolean sliding = false;

	public Slider(Setting setting, int x, int y, int width, int height) {
		super(setting, x, y, width, height);
		this.numSet = (SliderSetting) setting;
	}

	@Override
	public void render(MatrixStack matricies, int x, int y, int mouseX, int mouseY, float delta) {
		this.x = x;
		this.y = y;

		if(isHovered(mouseX, mouseY)) DrawableHelper.fill(matricies, x, y, x + width, y + height, new Color(0, 0, 0, 120).getRGB()); //Hover

		double diff = Math.min(width, Math.max(0, mouseX - x));
		int renderWidth = (int)(width * (numSet.getValue() - numSet.getMin()) / (numSet.getMax() - numSet.getMin()));
		
		DrawableHelper.fill(matricies, x, y, x + renderWidth, y + height, UIUtils.getColor());
		
		if(sliding) {
			if(diff == 0) {
				numSet.setValue(numSet.getMin());
			} else {
				numSet.setValue(roundToPlace((diff / width) * (numSet.getMax() - numSet.getMin()) + numSet.getMin(), 2));
			}
		}
		
		int textOffset = height / 2 - mc.textRenderer.fontHeight / 2;
		mc.textRenderer.draw(matricies, numSet.getName() + ": " + roundToPlace(numSet.getValue(), 2), x + 5, y + textOffset, -1);
		
		super.render(matricies, x, y, mouseX, mouseY, delta);
	}
	
	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if(isHovered(mouseX, mouseY) && button == 0) sliding = true;
		super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {
		sliding = false;
		super.mouseReleased(mouseX, mouseY, button);
	}
	
	private double roundToPlace(double value, int place) {
		if(place < 0) return value;
		
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(place, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
}
