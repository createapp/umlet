package com.baselet.control.enums;

import com.baselet.control.basics.geom.PointDouble;

/**
 * Rotation of the text of an element, set with the textrotation=... property (#294, #433).
 *
 * The angle is counter-clockwise, like the start angle of DrawHandler#drawArc: at 90 degrees the text reads from bottom to top, at 270 degrees from top to bottom.
 *
 * The text is laid out in a "text area", which is the element rotated by the same angle.
 * Text area coordinates have x in reading direction and y towards the next line, so halign and valign are relative to the reading direction
 * (e.g. at 90 degrees halign=left starts the lines at the bottom border and valign=top places the first line at the left border).
 */
public enum TextRotation {
	DEG_0(0), DEG_90(90), DEG_180(180), DEG_270(270);

	private final int degrees;

	private TextRotation(int degrees) {
		this.degrees = degrees;
	}

	/**
	 * @return the counter-clockwise rotation in degrees (0, 90, 180 or 270)
	 */
	public int getDegrees() {
		return degrees;
	}

	public boolean isRotated() {
		return this != DEG_0;
	}

	/**
	 * @return true if the width of the text area is the height of the element (and vice versa)
	 */
	public boolean swapsWidthAndHeight() {
		return this == DEG_90 || this == DEG_270;
	}

	/**
	 * @param degrees counter-clockwise rotation, must be a multiple of 90 (negative values and values above 360 are allowed, e.g. -90 is the same as 270)
	 * @return the matching rotation or null if degrees is not a multiple of 90
	 */
	public static TextRotation fromDegrees(int degrees) {
		int normalized = (degrees % 360 + 360) % 360;
		for (TextRotation rotation : values()) {
			if (rotation.degrees == normalized) {
				return rotation;
			}
		}
		return null;
	}

	/**
	 * Maps a point of the text area to the coordinates of the (unrotated) element.
	 * @param x position in reading direction (0 is the start of a left aligned line)
	 * @param y position towards the next line (0 is the top border of the text area)
	 * @param elementWidth width of the element (not of the text area)
	 * @param elementHeight height of the element (not of the text area)
	 */
	public PointDouble toElementCoordinates(double x, double y, double elementWidth, double elementHeight) {
		switch (this) {
			case DEG_90:
				return new PointDouble(y, elementHeight - x);
			case DEG_180:
				return new PointDouble(elementWidth - x, elementHeight - y);
			case DEG_270:
				return new PointDouble(elementWidth - y, x);
			default:
				return new PointDouble(x, y);
		}
	}
}
