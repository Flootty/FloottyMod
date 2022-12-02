package floottymod.floottymod.ui.screens.clickgui;

import floottymod.floottymod.hack.Category;
import floottymod.floottymod.util.ChatUtils;
import floottymod.floottymod.util.UIUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static floottymod.floottymod.FloottyMod.MC;

public class ClickGui extends Screen {
	public static final ClickGui INSTANCE = new ClickGui();
	
	private List<Frame> frames;

	public boolean drawFrames;

	public HackOptionScreen optionScreen;
	
	private ClickGui() {
		super(Text.literal("Click GUI"));

		UIUtils.updateVariables();

		frames = new ArrayList<Frame>();
		int offset = 20;
		for(Category c : Category.values()) {
			frames.add(new Frame(c, offset, 20));
			offset += 120;
		}

		drawFrames = true;

		optionScreen = new HackOptionScreen();
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		if(drawFrames) {
			for(Frame f : frames) f.render(matrices, mouseX, mouseY, delta);
		} else optionScreen.render(matrices, mouseX, mouseY, delta);

		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if(drawFrames) {
			for(Frame f : frames) {
				f.mouseClicked(mouseX, mouseY, button);;
			}
		} else {
			optionScreen.mouseClicked(mouseX, mouseY, button);
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if(drawFrames) {
			for(Frame f : frames) {
				f.mouseReleased(mouseX, mouseY, button);;
			}
		} else {
			optionScreen.mouseReleased(mouseX, mouseY, button);
		}
		return super.mouseReleased(mouseX, mouseY, button);
	}
}
