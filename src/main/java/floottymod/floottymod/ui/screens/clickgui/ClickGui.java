package floottymod.floottymod.ui.screens.clickgui;

import floottymod.floottymod.modules.Category;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ClickGui extends Screen {
	public static final ClickGui INSTANCE = new ClickGui();
	
	private List<Frame> frames;
	
	private ClickGui() {
		super(Text.literal("Click GUI"));
		
		frames = new ArrayList<Frame>();
		int offset = 20;
		for(Category c : Category.values()) {
			frames.add(new Frame(c, offset, 20, 100, 20));
			offset += 120;
		}
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		for(Frame f : frames) {
			f.render(matrices, mouseX, mouseY, delta);
			f.updatePosition(mouseX, mouseY);
		}
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for(Frame f : frames) {
			f.mouseClicked(mouseX, mouseY, button);;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		for(Frame f : frames) {
			f.mouseReleased(mouseX, mouseY, button);;
		}
		return super.mouseReleased(mouseX, mouseY, button);
	}
}
