package floottymod.floottymod.util.chestesp;

import com.mojang.blaze3d.systems.RenderSystem;
import floottymod.floottymod.util.RenderUtils;
import floottymod.floottymod.util.RotationUtils;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;
import java.util.stream.Stream;

public final class ChestEspRenderer {
    private static VertexBuffer solidBox;
    private static VertexBuffer outlinedBox;

    private final MatrixStack matrixStack;
    private final int regionX;
    private final int regionZ;
    private final Vec3d start;

    public ChestEspRenderer(MatrixStack matrixStack) {
        this.matrixStack = matrixStack;

        BlockPos camPos = RenderUtils.getCameraBlockPos();
        regionX = (camPos.getX() >> 9) * 512;
        regionZ = (camPos.getZ() >> 9) * 512;

        start = RotationUtils.getClientLookVec().add(RenderUtils.getCameraPos()).subtract(regionX, 0, regionZ);
    }

    public void renderBoxes(ChestEspGroup group) {
        float[] colorF = group.getColorF();

        for(Box box : group.getBoxes()) {
            matrixStack.push();

            matrixStack.translate(box.minX - regionX, box.minY, box.minZ - regionZ);

            matrixStack.scale((float)(box.maxX - box.minX), (float)(box.maxY - box.minY), (float)(box.maxZ - box.minZ));

            Matrix4f viewMatrix = matrixStack.peek().getPositionMatrix();
            Matrix4f projMatrix = RenderSystem.getProjectionMatrix();
            Shader shader = RenderSystem.getShader();

            RenderSystem.setShaderColor(colorF[0], colorF[1], colorF[2], 0.25F);
            solidBox.bind();
            solidBox.draw(viewMatrix, projMatrix, shader);
            VertexBuffer.unbind();

            RenderSystem.setShaderColor(colorF[0], colorF[1], colorF[2], 0.5F);
            outlinedBox.bind();
            outlinedBox.draw(viewMatrix, projMatrix, shader);
            VertexBuffer.unbind();

            matrixStack.pop();
        }
    }

    public static void prepareBuffers() {
        closeBuffers();
        solidBox = new VertexBuffer();
        outlinedBox = new VertexBuffer();

        Box box = new Box(BlockPos.ORIGIN);
        RenderUtils.drawSolidBox(box, solidBox);
        RenderUtils.drawOutlinedBox(box, outlinedBox);
    }

    public static void closeBuffers() {
        Stream.of(solidBox, outlinedBox).filter(Objects::nonNull)
                .forEach(VertexBuffer::close);
    }
}
