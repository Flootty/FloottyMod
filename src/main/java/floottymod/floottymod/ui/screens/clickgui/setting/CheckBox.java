package floottymod.floottymod.ui.screens.clickgui.setting;

import floottymod.floottymod.modules.settings.BoolSetting;
import floottymod.floottymod.modules.settings.Setting;
import floottymod.floottymod.ui.screens.clickgui.ModButton;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class CheckBox extends Component {
	private BoolSetting boolSet = (BoolSetting)setting;

	public CheckBox(Setting setting, ModButton parent, int offset) {
		super(setting, parent, offset);
		this.boolSet = (BoolSetting)setting;
	}
	
	@Override
	public void render(MatrixStack matricies, int mouseX, int mouseY, float delta) {
		DrawableHelper.fill(matricies, parent.parent.x, parent.parent.y + parent.offset + offset, parent.parent.x + parent.parent.width, parent.parent.y + parent.offset + offset + parent.parent.height, new Color(0, 0, 0, 120).getRGB());
		if(isHovered(mouseX, mouseY)) DrawableHelper.fill(matricies, parent.parent.x, parent.parent.y + parent.offset + offset, parent.parent.x + parent.parent.width, parent.parent.y + parent.offset + offset + parent.parent.height, new Color(0, 0, 0, 120).getRGB());
		
		int w = 14;
		int h = 14;
		int x = parent.parent.x + parent.parent.width - w - 4;
		int y = parent.parent.y + parent.offset + offset + parent.parent.height / 2 - h / 2;
		DrawableHelper.fill(matricies, x, y, x + w, y + h, new Color(0, 0, 0, 220).getRGB());
		w = 10;
		h = 10;
		if(boolSet.isEnabled()) DrawableHelper.fill(matricies, x + 2, y + 2, x + 2 + w, y + 2 + h, Color.red.getRGB());
		
		int textOffset = ((parent.parent.height / 2) - mc.textRenderer.fontHeight / 2);
		mc.textRenderer.draw(matricies, boolSet.getName(), parent.parent.x + 5, parent.parent.y + parent.offset + offset + textOffset, -1);
		
		super.render(matricies, mouseX, mouseY, delta);
	}
	
	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if(isHovered(mouseX, mouseY) && button == 0) boolSet.toggle();
		super.mouseClicked(mouseX, mouseY, button);
	}
}
