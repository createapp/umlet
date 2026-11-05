package com.baselet.control.basics.geom;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GeometricFunctionsTest {

	private static final double DELTA = 0.0001;

	@Test
	public void distanceBetweenLineAndPoint() {
		assertEquals(3, GeometricFunctions.getDistanceBetweenLineAndPoint(new PointDouble(0, 0), new PointDouble(10, 0), new PointDouble(5, 3)), DELTA);
	}

	@Test
	public void distanceBetweenZeroLengthLineAndPoint() {
		// #679: a line whose start and end are the same point must not divide by zero
		assertEquals(5, GeometricFunctions.getDistanceBetweenLineAndPoint(new PointDouble(5, 5), new PointDouble(5, 5), new PointDouble(8, 9)), DELTA);
	}
}
