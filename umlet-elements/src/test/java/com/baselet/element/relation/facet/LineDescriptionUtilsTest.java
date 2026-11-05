package com.baselet.element.relation.facet;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.baselet.control.StringStyle;
import com.baselet.control.basics.geom.DimensionDouble;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.facet.customdrawings.DummyDrawHandler;
import com.baselet.element.relation.helper.LineDescriptionBasePositionEnum;
import com.baselet.element.relation.helper.RelationPointHandler;
import com.baselet.element.relation.helper.RelationPointList;

/**
 * Position of the main relation text with and without cbase=... (#421, #608)
 * Uses a fixed font metric: every character is 10px wide, the text height is 10px (+3px line distance).
 * The text "ab" is therefore 20px wide.
 */
public class LineDescriptionUtilsTest {

	private static final double DELTA = 0.0001;

	private static class FixedSizeDrawHandler extends DummyDrawHandler {
		@Override
		protected DimensionDouble textDimensionHelper(StringStyle singleLine) {
			return new DimensionDouble(10 * singleLine.getStringWithoutMarkup().length(), 10);
		}
	}

	private static RelationPointHandler relation(double... xy) {
		RelationPointList points = new RelationPointList();
		for (int i = 0; i < xy.length; i += 2) {
			points.add(xy[i], xy[i + 1]);
		}
		return new RelationPointHandler(null, points);
	}

	private static List<LineDescriptionBasePositionEnum> cbase(String value) {
		PropertiesParserState state = new PropertiesParserState(null, null);
		if (!value.isEmpty()) {
			LineDescriptionBasePositionFacet.INSTANCE.handleValue(value, state);
		}
		return state.getOrInitFacetResponse(LineDescriptionBasePositionFacet.class, new ArrayList<LineDescriptionBasePositionEnum>());
	}

	private static PointDouble pos(RelationPointHandler relation, String cbase, int lineNr) {
		FixedSizeDrawHandler drawer = new FixedSizeDrawHandler();
		double halfMiddleBlockHeight = drawer.textHeightMaxWithSpace() / 2; // a single text line
		return LineDescriptionUtils.calcPosOfMiddleText(drawer, "ab", lineNr, halfMiddleBlockHeight, relation, cbase(cbase));
	}

	private static void assertPos(double expectedX, double expectedY, PointDouble actual) {
		assertEquals("x", expectedX, actual.getX(), DELTA);
		assertEquals("y", expectedY, actual.getY(), DELTA);
	}

	// horizontal line from (0,0) to (100,0): center is (50,0)

	@Test
	public void horizontal_default_centeredAboveLine() {
		assertPos(40, -4, pos(relation(0, 0, 100, 0), "", 0));
	}

	@Test
	public void horizontal_down() {
		assertPos(40, 17, pos(relation(0, 0, 100, 0), "d", 0));
	}

	@Test
	public void horizontal_down_secondLineIsBelowFirst() {
		assertPos(40, 30, pos(relation(0, 0, 100, 0), "d", 1));
	}

	@Test
	public void horizontal_up() {
		assertPos(40, -6.5, pos(relation(0, 0, 100, 0), "u", 0));
	}

	@Test
	public void horizontal_right() {
		assertPos(54, -4, pos(relation(0, 0, 100, 0), "r", 0));
	}

	@Test
	public void horizontal_left() {
		assertPos(26, -4, pos(relation(0, 0, 100, 0), "l", 0));
	}

	@Test
	public void horizontal_rightUp() {
		assertPos(54, -6.5, pos(relation(0, 0, 100, 0), "ru", 0));
	}

	// vertical line from (0,0) to (0,100): center is (0,50)

	@Test
	public void vertical_default_rightOfLine() {
		assertPos(4, 56.5, pos(relation(0, 0, 0, 100), "", 0));
	}

	@Test
	public void vertical_left() {
		assertPos(-24, 56.5, pos(relation(0, 0, 0, 100), "l", 0));
	}

	@Test
	public void vertical_down_centeredBelowCenter() {
		assertPos(-10, 67, pos(relation(0, 0, 0, 100), "d", 0));
	}

	@Test
	public void multiSegment_textIsPlacedAtCenterOfWholePath() {
		// segments: 20 + 60 + 220 = 300 => center is 150 along the path at (100,80) on the last (horizontal) segment
		assertPos(90, 76, pos(relation(10, 20, 30, 20, 30, 80, 250, 80), "", 0));
	}

	@Test
	public void zeroLengthRelation_doesNotCrash() {
		// #679: all points on top of each other; the line has no direction (it's treated as vertical), so the text is placed right of the point (20,20)
		assertPos(24, 26.5, pos(relation(20, 20, 20, 20), "", 0));
	}
}
