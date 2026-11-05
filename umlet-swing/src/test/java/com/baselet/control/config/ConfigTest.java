package com.baselet.control.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Font;

import org.junit.BeforeClass;
import org.junit.Test;

import com.baselet.control.constants.Constants;
import com.baselet.control.enums.Program;
import com.baselet.control.enums.RuntimeType;

public class ConfigTest {

	@BeforeClass
	public static void beforeClass() {
		Program.init("test", RuntimeType.BATCH); // batchmode doesn't access the UIManager
	}

	@Test
	public void osxReplaceDefaultFonts_enabledByDefault() {
		assertTrue(new Config().isOsxReplaceDefaultFonts());
	}

	@Test
	public void getPhysicalFontFamily_enabled_usesFontFamilyMapOfCurrentOs() {
		Config config = new Config();
		for (String logical : Constants.fontFamilyList) {
			String expected = Constants.fontFamilyMap.containsKey(logical) ? Constants.fontFamilyMap.get(logical) : logical;
			assertEquals(expected, config.getPhysicalFontFamily(logical));
		}
	}

	@Test
	public void getPhysicalFontFamily_disabled_keepsLogicalFonts() {
		Config config = new Config();
		config.setOsxReplaceDefaultFonts(false);
		for (String logical : Constants.fontFamilyList) {
			assertEquals(logical, config.getPhysicalFontFamily(logical));
		}
	}

	@Test
	public void getPhysicalFontFamily_unknownFamily_isUnchanged() {
		assertEquals(Font.DIALOG, new Config().getPhysicalFontFamily(Font.DIALOG));
	}
}
