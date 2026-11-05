package com.baselet.element.elementnew;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

import com.baselet.control.StringStyle;
import com.baselet.control.basics.geom.DimensionDouble;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.diagram.draw.DrawFunction;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.element.facet.common.TextRotationFacet;
import com.baselet.element.interfaces.Component;
import com.baselet.element.interfaces.DrawHandlerInterface;
import com.baselet.element.settings.SettingsText;
import com.baselet.element.sticking.StickableMap;

/**
 * Layout of the Text element with textrotation=... (#294, #433)
 *
 * Uses a fixed font metric: every character is 10px wide and the text height is 10px (+3px line distance), the distance between border and text is 5px.
 * A 100x40 element rotated by 90 or 270 degrees therefore has a 40px wide and 100px high text area.
 */
public class TextRotationLayoutTest {

	private static final int WIDTH = 100;
	private static final int HEIGHT = 40;

	@Test
	public void noRotation_unchanged() {
		assertDrawCalls(draw("ab"),
				"print 'ab' at (5.0, 15.0) LEFT rotated 0");
	}

	@Test
	public void rotation0_sameAsNoRotation() {
		assertDrawCalls(draw("textrotation=0", "ab"),
				"print 'ab' at (5.0, 15.0) LEFT rotated 0");
	}

	@Test
	public void rotation90_startsAtBottomLeftAndReadsUpwards() {
		assertDrawCalls(draw("textrotation=90", "ab"),
				"print 'ab' at (15.0, 35.0) LEFT rotated 90");
	}

	@Test
	public void rotation180_startsAtBottomRightUpsideDown() {
		assertDrawCalls(draw("textrotation=180", "ab"),
				"print 'ab' at (95.0, 25.0) LEFT rotated 180");
	}

	@Test
	public void rotation270_startsAtTopRightAndReadsDownwards() {
		assertDrawCalls(draw("textrotation=270", "ab"),
				"print 'ab' at (85.0, 5.0) LEFT rotated 270");
	}

	@Test
	public void negativeRotation_isNormalized() {
		assertDrawCalls(draw("textrotation=-90", "ab"),
				"print 'ab' at (85.0, 5.0) LEFT rotated 270");
	}

	@Test
	public void rotation90_furtherLinesAreRightOfThePreviousLine() {
		assertDrawCalls(draw("textrotation=90", "ab", "cd"),
				"print 'ab' at (15.0, 35.0) LEFT rotated 90",
				"print 'cd' at (28.0, 35.0) LEFT rotated 90");
	}

	@Test
	public void rotation90_positionInPropertiesDoesNotMatter() {
		assertDrawCalls(draw("ab", "textrotation=90"),
				"print 'ab' at (15.0, 35.0) LEFT rotated 90");
	}

	@Test
	public void rotation90_centerAlignmentIsRelativeToTheTextArea() {
		// unrotated: centered in the 100x40 element
		assertDrawCalls(draw("halign=center", "valign=center", "ab"),
				"print 'ab' at (50.0, 23.5) CENTER rotated 0");
		// rotated: centered in the 40x100 text area, which is the element center again
		assertDrawCalls(draw("textrotation=90", "halign=center", "valign=center", "ab"),
				"print 'ab' at (53.5, 20.0) CENTER rotated 90");
	}

	@Test
	public void rotation90_rightAlignmentEndsAtTheTop() {
		assertDrawCalls(draw("textrotation=90", "halign=right", "ab"),
				"print 'ab' at (15.0, 5.0) RIGHT rotated 90");
	}

	@Test
	public void rotation90_separatorLineIsVertical() {
		assertDrawCalls(draw("ab", "--", "cd"),
				"print 'ab' at (5.0, 15.0) LEFT rotated 0",
				"line from (0.5, 20.5) to (99.0, 20.5)",
				"print 'cd' at (5.0, 33.0) LEFT rotated 0");
		assertDrawCalls(draw("textrotation=90", "ab", "--", "cd"),
				"print 'ab' at (15.0, 35.0) LEFT rotated 90",
				"line from (20.5, 39.5) to (20.5, 1.0)",
				"print 'cd' at (33.0, 35.0) LEFT rotated 90");
	}

	@Test
	public void rotation90_wordwrapUsesTheHeightOfTheElement() {
		// unrotated, the 80px text fits into the 90px wide line
		assertDrawCalls(draw("style=wordwrap", "aa bb cc"),
				"print 'aa bb cc' at (5.0, 15.0) LEFT rotated 0");
		// rotated, a line has only 30px, therefore every word gets its own line
		assertDrawCalls(draw("textrotation=90", "style=wordwrap", "aa bb cc"),
				"print 'aa' at (15.0, 35.0) LEFT rotated 90",
				"print 'bb' at (28.0, 35.0) LEFT rotated 90",
				"print 'cc' at (41.0, 35.0) LEFT rotated 90");
	}

	@Test
	public void rotation90_autoresizeSwapsWidthAndHeight() {
		StubComponent unrotated = new StubComponent();
		draw(unrotated, "style=autoresize", "abcdefgh");
		assertEquals(new Rectangle(0, 0, 90, 30), unrotated.getBoundsRect());

		StubComponent rotated = new StubComponent();
		List<String> drawCalls = draw(rotated, "textrotation=90", "style=autoresize", "abcdefgh");
		assertEquals(new Rectangle(0, 0, 30, 90), rotated.getBoundsRect());
		assertDrawCalls(drawCalls, "print 'abcdefgh' at (20.0, 85.0) LEFT rotated 90");
	}

	@Test
	public void invalidRotation_showsError() {
		List<String> drawCalls = draw("textrotation=45", "ab");
		assertContains(drawCalls, "value must be 0, 90, 180 or 270");
		drawCalls = draw("textrotation=up", "ab");
		assertContains(drawCalls, "value must be 0, 90, 180 or 270");
	}

	@Test
	public void textAndNoteSettingsOfferTextRotation() {
		assertTrue(new SettingsText().getFacetsForFirstRun().contains(TextRotationFacet.INSTANCE));
		assertTrue(new Text().createSettings().getFacetsForFirstRun().contains(TextRotationFacet.INSTANCE));
	}

	private static List<String> draw(String... properties) {
		return draw(new StubComponent(), properties);
	}

	private static List<String> draw(StubComponent component, String... properties) {
		StringBuilder panelAttributes = new StringBuilder();
		for (String property : properties) {
			if (panelAttributes.length() > 0) {
				panelAttributes.append('\n');
			}
			panelAttributes.append(property);
		}
		Text text = new Text();
		text.init(new Rectangle(0, 0, WIDTH, HEIGHT), panelAttributes.toString(), "", component, new StubHandler());
		text.updateModelFromText();
		component.drawer.drawAll();
		return component.drawer.drawCalls;
	}

	private static void assertDrawCalls(List<String> actual, String... expected) {
		assertEquals(Arrays.asList(expected), actual);
	}

	private static void assertContains(List<String> drawCalls, String expectedPart) {
		for (String drawCall : drawCalls) {
			if (drawCall.contains(expectedPart)) {
				return;
			}
		}
		throw new AssertionError("no draw call contains '" + expectedPart + "': " + drawCalls);
	}

	private static String format(PointDouble p) {
		return String.format(Locale.ENGLISH, "(%.1f, %.1f)", p.getX(), p.getY());
	}

	/**
	 * records print and line calls (only the ones which are drawn, e.g. not during the preparsing of the properties)
	 */
	private static class RecordingDrawHandler extends DrawHandler {
		private final List<String> drawCalls = new ArrayList<String>();

		private void record(final String drawCall) {
			addDrawable(new DrawFunction() {
				@Override
				public void run() {
					drawCalls.add(drawCall);
				}
			});
		}

		@Override
		protected DimensionDouble textDimensionHelper(StringStyle singleLine) {
			return new DimensionDouble(singleLine.getStringWithoutMarkup().length() * 10, 10);
		}

		@Override
		protected double getDefaultFontSize() {
			return 10;
		}

		@Override
		public void printHelper(StringStyle[] lines, PointDouble point, AlignHorizontal align) {
			for (StringStyle line : lines) {
				record("print '" + line.getStringWithoutMarkup() + "' at " + format(point) + " " + align + " rotated " + style.getTextRotation().getDegrees());
			}
		}

		@Override
		public void drawLines(PointDouble... points) {
			record("line from " + format(points[0]) + " to " + format(points[points.length - 1]));
		}

		@Override
		public void drawArc(double x, double y, double width, double height, double start, double extent, boolean open) {}

		@Override
		public void drawCircle(double x, double y, double radius) {}

		@Override
		public void drawEllipse(double x, double y, double width, double height) {}

		@Override
		public void drawRectangle(double x, double y, double width, double height) {}

		@Override
		public void drawRectangleRound(double x, double y, double width, double height, double radius) {}
	}

	private static class StubComponent implements Component {
		private final RecordingDrawHandler drawer = new RecordingDrawHandler();
		private final RecordingDrawHandler metaDrawer = new RecordingDrawHandler();
		private Rectangle bounds;

		@Override
		public void setBoundsRect(Rectangle rect) {
			bounds = rect.copy();
		}

		@Override
		public Rectangle getBoundsRect() {
			return bounds.copy();
		}

		@Override
		public void repaintComponent() {}

		@Override
		public DrawHandler getDrawHandler() {
			return drawer;
		}

		@Override
		public DrawHandler getMetaDrawHandler() {
			return metaDrawer;
		}

		@Override
		public void afterModelUpdate() {}

		@Override
		public void translateForExport() {}
	}

	private static class StubHandler implements DrawHandlerInterface {
		@Override
		public void updatePropertyPanel() {}

		@Override
		public int getGridSize() {
			return 10;
		}

		@Override
		public StickableMap getStickableMap() {
			return StickableMap.EMPTY_MAP;
		}

		@Override
		public boolean isInitialized() {
			return true;
		}
	}
}
