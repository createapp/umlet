package com.baselet.control.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.baselet.control.basics.geom.PointDouble;

/**
 * textrotation=... rotates the text area of an element (#294, #433)
 */
public class TextRotationTest {

	private static final double W = 100;
	private static final double H = 40;

	@Test
	public void fromDegrees_multiplesOf90() {
		assertEquals(TextRotation.DEG_0, TextRotation.fromDegrees(0));
		assertEquals(TextRotation.DEG_90, TextRotation.fromDegrees(90));
		assertEquals(TextRotation.DEG_180, TextRotation.fromDegrees(180));
		assertEquals(TextRotation.DEG_270, TextRotation.fromDegrees(270));
	}

	@Test
	public void fromDegrees_isNormalized() {
		assertEquals(TextRotation.DEG_0, TextRotation.fromDegrees(360));
		assertEquals(TextRotation.DEG_90, TextRotation.fromDegrees(450));
		assertEquals(TextRotation.DEG_270, TextRotation.fromDegrees(-90));
		assertEquals(TextRotation.DEG_180, TextRotation.fromDegrees(-180));
	}

	@Test
	public void fromDegrees_otherAnglesAreNotSupported() {
		assertNull(TextRotation.fromDegrees(45));
		assertNull(TextRotation.fromDegrees(1));
		assertNull(TextRotation.fromDegrees(-30));
	}

	@Test
	public void swapsWidthAndHeight_onlyForQuarterTurns() {
		assertFalse(TextRotation.DEG_0.swapsWidthAndHeight());
		assertTrue(TextRotation.DEG_90.swapsWidthAndHeight());
		assertFalse(TextRotation.DEG_180.swapsWidthAndHeight());
		assertTrue(TextRotation.DEG_270.swapsWidthAndHeight());
	}

	@Test
	public void isRotated_onlyIfNotZero() {
		assertFalse(TextRotation.DEG_0.isRotated());
		assertTrue(TextRotation.DEG_90.isRotated());
		assertTrue(TextRotation.DEG_180.isRotated());
		assertTrue(TextRotation.DEG_270.isRotated());
	}

	@Test
	public void deg0_keepsCoordinates() {
		assertPoint(5, 15, TextRotation.DEG_0.toElementCoordinates(5, 15, W, H));
	}

	@Test
	public void deg90_readsBottomToTop() {
		// text area is H wide and W high. Its top left corner is the bottom left corner of the element
		assertPoint(0, H, TextRotation.DEG_90.toElementCoordinates(0, 0, W, H));
		// reading direction is upwards
		assertPoint(0, 0, TextRotation.DEG_90.toElementCoordinates(H, 0, W, H));
		// the next line is further right
		assertPoint(W, H, TextRotation.DEG_90.toElementCoordinates(0, W, W, H));
		assertPoint(15, 35, TextRotation.DEG_90.toElementCoordinates(5, 15, W, H));
	}

	@Test
	public void deg180_isUpsideDown() {
		assertPoint(W, H, TextRotation.DEG_180.toElementCoordinates(0, 0, W, H));
		assertPoint(0, H, TextRotation.DEG_180.toElementCoordinates(W, 0, W, H));
		assertPoint(W, 0, TextRotation.DEG_180.toElementCoordinates(0, H, W, H));
		assertPoint(95, 25, TextRotation.DEG_180.toElementCoordinates(5, 15, W, H));
	}

	@Test
	public void deg270_readsTopToBottom() {
		// text area is H wide and W high. Its top left corner is the top right corner of the element
		assertPoint(W, 0, TextRotation.DEG_270.toElementCoordinates(0, 0, W, H));
		// reading direction is downwards
		assertPoint(W, H, TextRotation.DEG_270.toElementCoordinates(H, 0, W, H));
		// the next line is further left
		assertPoint(0, 0, TextRotation.DEG_270.toElementCoordinates(0, W, W, H));
		assertPoint(85, 5, TextRotation.DEG_270.toElementCoordinates(5, 15, W, H));
	}

	private static void assertPoint(double expectedX, double expectedY, PointDouble actual) {
		assertEquals("x of " + actual, expectedX, actual.getX(), 0.0001);
		assertEquals("y of " + actual, expectedY, actual.getY(), 0.0001);
	}
}
