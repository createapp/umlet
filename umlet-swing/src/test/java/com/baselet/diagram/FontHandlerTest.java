package com.baselet.diagram;

import static org.junit.Assert.assertEquals;

import java.awt.Font;

import org.junit.BeforeClass;
import org.junit.Test;

import com.baselet.control.config.Config;
import com.baselet.control.enums.Program;
import com.baselet.control.enums.RuntimeType;

public class FontHandlerTest {

	@BeforeClass
	public static void beforeClass() {
		Program.init("test", RuntimeType.BATCH); // batchmode doesn't access the UIManager
	}

	private FontHandler monospacedFontHandler() {
		FontHandler fontHandler = new FontHandler(null); // the DiagramHandler is only used to apply the zoom level
		fontHandler.setDiagramDefaultFontFamily(Font.MONOSPACED);
		fontHandler.setDiagramDefaultFontSize(8.0);
		return fontHandler;
	}

	@Test
	public void getFont_usesPhysicalFontFamily() {
		Font font = monospacedFontHandler().getFont(false);
		assertEquals(Config.getInstance().getPhysicalFontFamily(Font.MONOSPACED), font.getName());
		assertEquals(8, font.getSize());
	}

	@Test
	public void copyWithLogicalFonts_keepsLogicalFontFamilyAndDiagramSettings() {
		Font font = monospacedFontHandler().copyWithLogicalFonts().getFont(false);
		assertEquals(Font.MONOSPACED, font.getName());
		assertEquals(8, font.getSize());
	}
}
