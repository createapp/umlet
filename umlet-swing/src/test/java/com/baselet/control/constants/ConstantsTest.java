package com.baselet.control.constants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Font;
import java.util.Map;

import org.junit.Test;

import com.baselet.control.enums.Os;

public class ConstantsTest {

	@Test
	public void fontFamilyMap_mac_replacesAllLogicalFonts() {
		Map<String, String> map = Constants.createFontFamilyMap(Os.MAC);
		assertEquals("Arial", map.get(Font.SANS_SERIF));
		assertEquals("Times New Roman", map.get(Font.SERIF));
		assertEquals("Menlo", map.get(Font.MONOSPACED));
		assertEquals(Constants.fontFamilyList.size(), map.size());
	}

	@Test
	public void fontFamilyMap_otherOs_keepsLogicalFonts() {
		for (Os os : Os.values()) {
			if (os != Os.MAC) {
				assertTrue(os + " must not replace logical fonts", Constants.createFontFamilyMap(os).isEmpty());
			}
		}
	}

	@Test(expected = UnsupportedOperationException.class)
	public void fontFamilyMap_isUnmodifiable() {
		Constants.fontFamilyMap.put(Font.SANS_SERIF, "Comic Sans MS");
	}
}
