package floottymod.floottymod.util.chestesp;

import floottymod.floottymod.settings.BoolSetting;
import net.minecraft.entity.Entity;

import java.util.ArrayList;

public class ChestEspEntityGroup extends ChestEspGroup {
    private final ArrayList<Entity> entities = new ArrayList<>();

    public ChestEspEntityGroup(float[] color, BoolSetting enabled) {
        super(color, enabled);
    }

    public void add(Entity e) {
        entities.add(e);
    }

    @Override
    public void clear() {
        entities.clear();
        super.clear();
    }

    public void updateBoxes(float partialTicks) {
        boxes.clear();

        for(Entity e : entities) {
            double offsetX = -(e.getX() - e.lastRenderX) + (e.getX() - e.lastRenderX) * partialTicks;
            double offsetY = -(e.getY() - e.lastRenderY) + (e.getY() - e.lastRenderY) * partialTicks;
            double offsetZ = -(e.getZ() - e.lastRenderZ) + (e.getZ() - e.lastRenderZ) * partialTicks;

            boxes.add(e.getBoundingBox().offset(offsetX, offsetY, offsetZ));
        }
    }
}
