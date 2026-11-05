package com.baselet.element.relation.facet;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.baselet.control.basics.geom.Point;
import com.baselet.diagram.draw.helper.StyleException;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.relation.helper.LineDescriptionEnum;

/**
 * cpos=x,y moves the main relation text like m1pos/m2pos/r1pos/r2pos move the end texts (#421, #608)
 */
public class LineDescriptionPositionFacetTest {

	private static Map<String, Point> parse(LineDescriptionPositionFacet facet, String value) {
		PropertiesParserState state = new PropertiesParserState(null, null);
		facet.handleValue(value, state);
		return state.getOrInitFacetResponse(LineDescriptionPositionFacet.class, new HashMap<String, Point>());
	}

	@Test
	public void mainTextKeyIsCpos() {
		assertEquals("cpos", LineDescriptionPositionFacet.INSTANCE_MESSAGE_CENTER.getKeyValue().getKey());
	}

	@Test
	public void cposStoresDisplacementOfMainText() {
		Map<String, Point> displacements = parse(LineDescriptionPositionFacet.INSTANCE_MESSAGE_CENTER, "40,-15");
		assertEquals(new Point(40, -15), displacements.get(LineDescriptionEnum.MESSAGE_CENTER.getKey()));
	}

	@Test
	public void endTextPositionsStillWork() {
		assertEquals("m1pos", LineDescriptionPositionFacet.INSTANCE_MESSAGE_START.getKeyValue().getKey());
		assertEquals(new Point(10, 0), parse(LineDescriptionPositionFacet.INSTANCE_MESSAGE_START, "10,0").get(LineDescriptionEnum.MESSAGE_START.getKey()));
	}

	@Test(expected = StyleException.class)
	public void cposOutOfRangeIsRejected() {
		parse(LineDescriptionPositionFacet.INSTANCE_MESSAGE_CENTER, "300,0");
	}

	@Test(expected = StyleException.class)
	public void cposWithoutTwoIntegersIsRejected() {
		parse(LineDescriptionPositionFacet.INSTANCE_MESSAGE_CENTER, "40");
	}
}
