package floottymod.floottymod.util.chestesp;

import floottymod.floottymod.settings.BoolSetting;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class ChestEspGroup {
    protected final ArrayList<Box> boxes = new ArrayList<>();
    private final float[] color;
    private BoolSetting enabled;

    public ChestEspGroup(float[] color, BoolSetting enabled) {
        this.color = Objects.requireNonNull(color);
        this.enabled = enabled;
    }

    public void clear() {
        boxes.clear();
    }

    public boolean isEnabled() {
        return enabled.isEnabled();
    }

    public BoolSetting getSetting() {
        return enabled;
    }

    public float[] getColorF() {
        return color;
    }

    public List<Box> getBoxes() {
        return Collections.unmodifiableList(boxes);
    }
}
