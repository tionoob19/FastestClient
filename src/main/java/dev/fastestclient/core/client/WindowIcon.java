package dev.fastestclient.core.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Util;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/** Sets the window icon (Windows/Linux). macOS has no per-window icon in GLFW. */
public final class WindowIcon {
	private static final int[] SIZES = {16, 32, 48};

	private WindowIcon() {}

	public static void apply() {
		if (Util.getOperatingSystem() == Util.OperatingSystem.OSX) return;
		ByteBuffer[] pixels = new ByteBuffer[SIZES.length];
		ByteBuffer[] raw = new ByteBuffer[SIZES.length];
		try (MemoryStack stack = MemoryStack.stackPush()) {
			GLFWImage.Buffer images = GLFWImage.mallocStack(SIZES.length, stack);
			IntBuffer w = stack.mallocInt(1), h = stack.mallocInt(1), c = stack.mallocInt(1);
			for (int i = 0; i < SIZES.length; i++) {
				byte[] data = read("/assets/fastestcore/icons/icon_" + SIZES[i] + ".png");
				raw[i] = MemoryUtil.memAlloc(data.length);
				raw[i].put(data).flip();
				pixels[i] = STBImage.stbi_load_from_memory(raw[i], w, h, c, 4);
				if (pixels[i] == null) throw new IllegalStateException(STBImage.stbi_failure_reason());
				images.get(i).set(w.get(0), h.get(0), pixels[i]);
			}
			GLFW.glfwSetWindowIcon(MinecraftClient.getInstance().getWindow().getHandle(), images);
		} catch (Exception e) {
			System.err.println("[FastestCore] Could not set window icon: " + e);
		} finally {
			for (int i = 0; i < SIZES.length; i++) {
				if (pixels[i] != null) STBImage.stbi_image_free(pixels[i]);
				if (raw[i] != null) MemoryUtil.memFree(raw[i]);
			}
		}
	}

	private static byte[] read(String path) throws Exception {
		try (InputStream in = WindowIcon.class.getResourceAsStream(path)) {
			if (in == null) throw new IllegalStateException("Missing " + path);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[4096];
			int n;
			while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
			return out.toByteArray();
		}
	}
}
