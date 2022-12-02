package floottymod.floottymod.ui.screens.clickgui;

import floottymod.floottymod.hack.Hack;
import floottymod.floottymod.setting.Setting;
import floottymod.floottymod.settings.BoolSetting;
import floottymod.floottymod.settings.ModeSetting;
import floottymod.floottymod.settings.SliderSetting;
import floottymod.floottymod.ui.screens.clickgui.setting.CheckBox;
import floottymod.floottymod.ui.screens.clickgui.setting.ModeBox;
import floottymod.floottymod.ui.screens.clickgui.setting.Slider;
import floottymod.floottymod.util.UIUtils;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.List;

import static floottymod.floottymod.FloottyMod.MC;

public class HackOptionScreen {
    private int x, y, width, height, centerX, centerY, dragX, dragY;

    private boolean dragging;

    private Hack hack;
    private List<Component> components;

    public void update(Hack hack) {
        this.hack = hack;

        components = new ArrayList<>();
        int setOffset = 1;
        for(Setting s : hack.getSettings().values()) {
            if(s instanceof BoolSetting) {
                components.add(new CheckBox(s, x, y + setOffset, width, (int) UIUtils.getOption_height()));
            } else if(s instanceof ModeSetting) {
                components.add(new ModeBox(s, x, y + setOffset, width, (int) UIUtils.getOption_height()));
            } else if(s instanceof SliderSetting) {
                components.add(new Slider(s, x, y + setOffset, width, (int) UIUtils.getOption_height()));
            }
            setOffset += (int) UIUtils.getOption_height();
        }

        width = (int) UIUtils.getFrame_width() * 2;
        height = (int) UIUtils.getOption_height() * components.size();
        centerX = (int) UIUtils.getCenterX();
        centerY = (int) UIUtils.getCenterY();
        x = centerX - width / 2;
        y = centerY - height / 2;

        dragging = false;
    }

    public void render(MatrixStack matricies, int mouseX, int mouseY, float delta) {
        DrawableHelper.fill(matricies, x, y - (int) UIUtils.getOption_height(), x + width, y, UIUtils.getColor()); //Header
        DrawableHelper.fill(matricies, x, y, x + width, y + height, UIUtils.getBgColor()); //Background

        MC.textRenderer.draw(matricies, hack.getName(), x + 5, y - (int) UIUtils.getOption_height() / 2 - MC.textRenderer.fontHeight / 2, -1); //Title
        MC.textRenderer.draw(matricies, "X", x + width - 5 - MC.textRenderer.getWidth("X"), y - (int) UIUtils.getOption_height() / 2 - MC.textRenderer.fontHeight / 2, -1); //X

        int offset = 1;
        for(Component c : components) {
            c.render(matricies, x, y + offset, mouseX, mouseY, delta);
            offset += (int) UIUtils.getOption_height();
        }

        updatePosition(mouseX, mouseY);
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        if(mouseX > x + width - 5 - MC.textRenderer.getWidth("X") && mouseX < x + width - 5 && mouseY > y - (int) UIUtils.getOption_height() / 2 - MC.textRenderer.fontHeight / 2 && mouseY < y - (int) UIUtils.getOption_height() / 2 + MC.textRenderer.fontHeight / 2) {
            ClickGui.INSTANCE.drawFrames = true;
        }

        if(isHovered(mouseX, mouseY)) {
            if(button == 0) {
                dragging = true;
                dragX = (int) (mouseX - x);
                dragY = (int) (mouseY - y);
            }
        }
        
        for(Component c : components) c.mouseClicked(mouseX, mouseY, button);
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        if(button == 0 && dragging == true) dragging = false;
        
        for(Component c : components) c.mouseReleased(mouseX, mouseY, button);
    }

    private boolean isHovered(double mouseX, double mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y - UIUtils.getOption_height() && mouseY < y;
    }

    private void updatePosition(double mouseX, double mouseY) {
        if(dragging) {
            x = (int) (mouseX - dragX);
            y = (int) (mouseY - dragY);
        }
    }
}
