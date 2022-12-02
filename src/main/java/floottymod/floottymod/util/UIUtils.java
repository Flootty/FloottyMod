package floottymod.floottymod.util;

import java.awt.*;

import static floottymod.floottymod.FloottyMod.MC;

public enum UIUtils {
    ;
    private static int width;
    private static int height;
    private static float centerX;
    private static float centerY;
    private static int frame_width;
    private static int option_height;
    private static int color = Color.red.getRGB();
    private static int bgColor = new Color(0, 0, 0, 180).getRGB();

    public static void updateVariables() {
        width = MC.getWindow().getScaledWidth();
        height = MC.getWindow().getScaledHeight();
        centerX = width / 2;
        centerY = height / 2;
        frame_width = width / 10;
        option_height = height / 30;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static float getCenterX() {
        return centerX;
    }

    public static float getCenterY() {
        return centerY;
    }

    public static float getFrame_width() {
        return frame_width;
    }

    public static float getOption_height() {
        return option_height;
    }

    public static int getColor() {
        return color;
    }

    public static int getBgColor() {
        return bgColor;
    }
}
