package floottymod.floottymod.ui.screens.clickgui.setting;

import floottymod.floottymod.settings.BoolSetting;
import floottymod.floottymod.settings.ModeSetting;
import floottymod.floottymod.setting.Setting;
import floottymod.floottymod.ui.screens.clickgui.Component;
import floottymod.floottymod.ui.screens.clickgui.ModButton;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class ModeBox extends Component {
	private ModeSetting modeSet;

	public ModeBox(Setting setting, int x, int y, int width, int height) {
		super(setting, x, y, width, height);
		this.modeSet = (ModeSetting) setting;
	}
	
	@Override
	public void render(MatrixStack matricies, int x, int y, int mouseX, int mouseY, float delta) {
		this.x = x;
		this.y = y;

		if(isHovered(mouseX, mouseY)) DrawableHelper.fill(matricies, x, y, x + width, y + height, new Color(0, 0, 0, 120).getRGB()); //Hover

		int textOffset = height / 2 - mc.textRenderer.fontHeight / 2;
		mc.textRenderer.draw(matricies, modeSet.getName() + ": " + modeSet.getMode(), x + 5, y + textOffset, -1);

		super.render(matricies, x, y, mouseX, mouseY, delta);
	}
	
	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if(isHovered(mouseX, mouseY) && button == 0) modeSet.cycle();
		super.mouseClicked(mouseX, mouseY, button);
	}
}
