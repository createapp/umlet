package com.baselet.element.relation.facet;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.baselet.diagram.draw.helper.StyleException;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.relation.helper.LineDescriptionBasePositionEnum;

/**
 * cbase=... positions the main relation text relative to the line (#421, #608)
 */
public class LineDescriptionBasePositionFacetTest {

	private static List<LineDescriptionBasePositionEnum> parse(String value) {
		PropertiesParserState state = new PropertiesParserState(null, null);
		LineDescriptionBasePositionFacet.INSTANCE.handleValue(value, state);
		return state.getOrInitFacetResponse(LineDescriptionBasePositionFacet.class, new ArrayList<LineDescriptionBasePositionEnum>());
	}

	@Test
	public void keyIsCbase() {
		assertEquals("cbase", LineDescriptionBasePositionFacet.INSTANCE.getKeyValue().getKey());
	}

	@Test
	public void singleDirections() {
		assertEquals(Arrays.asList(LineDescriptionBasePositionEnum.MESSAGE_MIDDLE_LEFT), parse("l"));
		assertEquals(Arrays.asList(LineDescriptionBasePositionEnum.MESSAGE_MIDDLE_RIGHT), parse("r"));
		assertEquals(Arrays.asList(LineDescriptionBasePositionEnum.MESSAGE_MIDDLE_UP), parse("u"));
		assertEquals(Arrays.asList(LineDescriptionBasePositionEnum.MESSAGE_MIDDLE_DOWN), parse("d"));
	}

	@Test
	public void horizontalAndVerticalDirectionCanBeCombined() {
		assertEquals(Arrays.asList(LineDescriptionBasePositionEnum.MESSAGE_MIDDLE_RIGHT, LineDescriptionBasePositionEnum.MESSAGE_MIDDLE_UP), parse("ru"));
		assertEquals(Arrays.asList(LineDescriptionBasePositionEnum.MESSAGE_MIDDLE_DOWN, LineDescriptionBasePositionEnum.MESSAGE_MIDDLE_LEFT), parse("dl"));
	}

	@Test(expected = StyleException.class)
	public void unknownDirectionIsRejected() {
		parse("x");
	}

	@Test(expected = StyleException.class)
	public void leftAndRightCannotBeCombined() {
		parse("lr");
	}

	@Test(expected = StyleException.class)
	public void upAndDownCannotBeCombined() {
		parse("ud");
	}

	@Test(expected = StyleException.class)
	public void duplicateDirectionIsRejected() {
		parse("rr");
	}

	@Test(expected = StyleException.class)
	public void moreThanTwoDirectionsAreRejected() {
		parse("rul");
	}
}
