package com.baselet.element.facet.common;

import com.baselet.control.enums.TextRotation;
import com.baselet.diagram.draw.helper.StyleException;
import com.baselet.element.facet.FirstRunKeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;

/**
 * textrotation=90 rotates the whole text of the element counter-clockwise (#294, #433).
 * The text is laid out as if the element was rotated by the same angle, therefore halign, valign, style=wordwrap, style=autoresize and -- work as usual (relative to the reading direction).
 */
public class TextRotationFacet extends FirstRunKeyValueFacet {

	public static final TextRotationFacet INSTANCE = new TextRotationFacet();

	private TextRotationFacet() {}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue("textrotation",
				new ValueInfo(TextRotation.DEG_0.getDegrees(), "no text rotation (default)"),
				new ValueInfo(TextRotation.DEG_90.getDegrees(), "text reads from bottom to top (rotated 90 degrees counter-clockwise)"),
				new ValueInfo(TextRotation.DEG_180.getDegrees(), "text is upside down"),
				new ValueInfo(TextRotation.DEG_270.getDegrees(), "text reads from top to bottom (rotated 90 degrees clockwise)"));
	}

	@Override
	public void handleValue(String value, PropertiesParserState state) {
		TextRotation rotation;
		try {
			rotation = TextRotation.fromDegrees(Integer.parseInt(value.trim()));
		} catch (NumberFormatException e) {
			rotation = null;
		}
		if (rotation == null) {
			throw new StyleException("value must be 0, 90, 180 or 270 (counter-clockwise degrees)");
		}
		state.setTextRotation(rotation);
	}

}
