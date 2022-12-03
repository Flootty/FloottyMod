package floottymod.floottymod.util.oreesp;

import floottymod.floottymod.settings.BoolSetting;
import floottymod.floottymod.util.BlockUtils;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class OreEspGroup {
    protected final ArrayList<Box> boxes = new ArrayList<>();
    private final float[] color;
    private BoolSetting enabled;

    public OreEspGroup(float[] color, BoolSetting enabled) {
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

    public float[] getColor() {
        return color;
    }

    public void add(BlockEntity be) {
        Box box = getBox(be);
        if(box == null)
            return;

        boxes.add(box);
    }

    public List<Box> getBoxes() {
        return Collections.unmodifiableList(boxes);
    }

    private Box getBox(BlockEntity be) {
        BlockPos pos = be.getPos();
        return BlockUtils.getBoundingBox(pos);
    }
}
