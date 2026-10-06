package dev.fastestclient.core.client;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/** Prefixes a name with the Fastest "F" icon (font glyph U+E000). */
public final class FastestIcon {
	private static final Identifier FONT = new Identifier("fastestcore", "icons");

	private FastestIcon() {}

	public static Text prefix(Text name) {
		return new LiteralText("")
			.append(new LiteralText("\uE000").setStyle(Style.EMPTY.withFont(FONT)))
			.append(new LiteralText(" "))
			.append(name);
	}
}
