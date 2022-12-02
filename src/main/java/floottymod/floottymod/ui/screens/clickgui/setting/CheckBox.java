package floottymod.floottymod.ui.screens.clickgui.setting;

import floottymod.floottymod.settings.BoolSetting;
import floottymod.floottymod.setting.Setting;
import floottymod.floottymod.ui.screens.clickgui.Component;
import floottymod.floottymod.ui.screens.clickgui.ModButton;
import floottymod.floottymod.util.UIUtils;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class CheckBox extends Component {
	private BoolSetting boolSet;

	public CheckBox(Setting setting, int x, int y, int width, int height) {
		super(setting, x, y, width, height);
		this.boolSet = (BoolSetting)setting;
	}
	
	@Override
	public void render(MatrixStack matricies, int x, int y, int mouseX, int mouseY, float delta) {
		this.x = x;
		this.y = y;

		if(isHovered(mouseX, mouseY)) DrawableHelper.fill(matricies, x, y, x + width, y + height, new Color(0, 0, 0, 120).getRGB()); //Hover
		
		int bh = height - 4;
		int bw = bh;
		int by = y + height / 2 - bh / 2;
		DrawableHelper.fill(matricies, x + width - bw - 2, by, x + width - 2, by + bh, new Color(0, 0, 0, 220).getRGB());

		by = y + height / 2 - bh / 2;
		if(boolSet.isEnabled()) DrawableHelper.fill(matricies, x + width - bw + (bw - 10) / 2 - 2, by + (bh - 10) / 2, x + width - (bw - 10) / 2 - 2, by + bh - (bh - 10) / 2, UIUtils.getColor());

		int textOffset = height / 2 - mc.textRenderer.fontHeight / 2;
		mc.textRenderer.draw(matricies, boolSet.getName(), x + 5, y + textOffset, -1);
		
		super.render(matricies, x, y, mouseX, mouseY, delta);
	}
	
	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if(isHovered(mouseX, mouseY) && button == 0) boolSet.toggle();
		super.mouseClicked(mouseX, mouseY, button);
	}
}
