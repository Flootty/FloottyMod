package floottymod.floottymod.util;

import floottymod.floottymod.FloottyMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public enum ChatUtils {
	;
	private static final MinecraftClient MC = FloottyMod.MC;
	
	public static final String BLUECODE_PREFIX ="\u00a78[\u00a76FloottyMod\u00a78]\u00a7r ";
	private static final String WARNING_PREFIX ="\u00a78[\u00a7c\u00a7lWARNING\u00a78]\u00a7r ";
	private static final String ERROR_PREFIX ="\u00a78[\u00a7c\u00a7lERROR\u00a78]\u00a7r ";
	private static final String SYNTAX_ERROR_PREFIX ="\u00a7cSyntax error:\u00a7r ";
	
	private static boolean enabled = true;
	
	public static void setEnabled(boolean enabled) {
		ChatUtils.enabled = enabled;
	}
	
	public static void component(Text component) {
		if(!enabled) return;
		
		ChatHud chatHud = MC.inGameHud.getChatHud();
		MutableText prefix = Text.literal(BLUECODE_PREFIX);
		chatHud.addMessage(prefix.append(component));
	}
	
	public static void message(String message) {
		component(Text.literal(message));
	}
	
	public static void warning(String message) {
		component(Text.literal(WARNING_PREFIX + message));
	}
	
	public static void error(String message) {
		component(Text.literal(ERROR_PREFIX + message));
	}
	
	public static void syntaxError(String message) {
		component(Text.literal(SYNTAX_ERROR_PREFIX + message));
	}
}
