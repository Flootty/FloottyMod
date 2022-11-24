package floottymod.floottymod.ui.screens.clickgui.setting;

import floottymod.floottymod.setting.Setting;
import floottymod.floottymod.settings.SliderSetting;
import floottymod.floottymod.ui.screens.clickgui.ModButton;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Slider extends Component {
	public SliderSetting numSet = (SliderSetting)setting;
	private boolean sliding = false;
	
	public Slider(Setting setting, ModButton parent, int offset) {
		super(setting, parent, offset);
		this.numSet = (SliderSetting)setting;
	}

	@Override
	public void render(MatrixStack matricies, int mouseX, int mouseY, float delta) {
		DrawableHelper.fill(matricies, parent.parent.x, parent.parent.y + parent.offset + offset, parent.parent.x + parent.parent.width, parent.parent.y + parent.offset + offset + parent.parent.height, new Color(0, 0, 0, 120).getRGB());
		if(isHovered(mouseX, mouseY)) DrawableHelper.fill(matricies, parent.parent.x, parent.parent.y + parent.offset + offset, parent.parent.x + parent.parent.width, parent.parent.y + parent.offset + offset + parent.parent.height, new Color(0, 0, 0, 120).getRGB());
		
		double diff = Math.min(parent.parent.width, Math.max(0, mouseX - parent.parent.x));
		int renderWidth = (int)(parent.parent.width * (numSet.getValue() - numSet.getMin()) / (numSet.getMax() - numSet.getMin()));
		
		DrawableHelper.fill(matricies, parent.parent.x, parent.parent.y + parent.offset + offset, parent.parent.x + renderWidth, parent.parent.y + parent.offset + offset + parent.parent.height, Color.red.getRGB());
		
		if(sliding) {
			if(diff == 0) {
				numSet.setValue(numSet.getMin());
			} else {
				numSet.setValue(roundToPlace((diff / parent.parent.width) * (numSet.getMax() - numSet.getMin()) + numSet.getMin(), 2));
			}
		}
		
		int textOffset = ((parent.parent.height / 2) - mc.textRenderer.fontHeight / 2);
		mc.textRenderer.draw(matricies, numSet.getName() + ": " + roundToPlace(numSet.getValue(), 2), parent.parent.x + 5, parent.parent.y + parent.offset + offset + textOffset, -1);
		
		super.render(matricies, mouseX, mouseY, delta);
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
