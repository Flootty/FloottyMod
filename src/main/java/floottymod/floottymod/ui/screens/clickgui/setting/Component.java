package floottymod.floottymod.ui.screens.clickgui.setting;


import floottymod.floottymod.setting.Setting;
import floottymod.floottymod.ui.screens.clickgui.ModButton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class Component {
	public Setting setting;
	public ModButton parent;
	public int offset;
	
	protected MinecraftClient mc = MinecraftClient.getInstance();
	
	public Component(Setting setting, ModButton parent, int offset) {
		this.setting = setting;
		this.parent = parent;
		this.offset = offset;
	}
	
	public void render(MatrixStack matricies, int mouseX, int mouseY, float delta) {
		
	}
	
	public void mouseClicked(double mouseX, double mouseY, int button) {
		
	}
	
	public void mouseReleased(double mouseX, double mouseY, int button) {
		
	}
	
	public boolean isHovered(double mouseX, double mouseY) {
		return mouseX > parent.parent.x && mouseX < parent.parent.x + parent.parent.width && mouseY > parent.parent.y + parent.offset + offset && mouseY < parent.parent.y + parent.offset + offset + parent.parent.height;
	}
}
