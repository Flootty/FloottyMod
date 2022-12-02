package floottymod.floottymod.ui.screens.clickgui;


import floottymod.floottymod.setting.Setting;
import floottymod.floottymod.ui.screens.clickgui.ModButton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class Component {
	public Setting setting;

	public int x, y, width, height;
	
	protected MinecraftClient mc = MinecraftClient.getInstance();
	
	public Component(Setting setting, int x, int y, int width, int height) {
		this.setting = setting;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void render(MatrixStack matricies, int x, int y, int mouseX, int mouseY, float delta) {
		
	}
	
	public void mouseClicked(double mouseX, double mouseY, int button) {
		
	}
	
	public void mouseReleased(double mouseX, double mouseY, int button) {
		
	}

	public boolean isHovered(double mouseX, double mouseY) {
		return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
	}
}
