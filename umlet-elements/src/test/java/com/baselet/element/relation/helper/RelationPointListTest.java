package com.baselet.element.relation.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.baselet.control.basics.geom.PointDouble;

/**
 * The main relation text and the drag box are centered on the middle of the whole relation path (#608)
 */
public class RelationPointListTest {

	private static final double DELTA = 0.0001;

	private static RelationPointList points(double... xy) {
		RelationPointList list = new RelationPointList();
		for (int i = 0; i < xy.length; i += 2) {
			list.add(xy[i], xy[i + 1]);
		}
		return list;
	}

	private static void assertPoint(double expectedX, double expectedY, PointDouble actual) {
		assertEquals("x", expectedX, actual.getX(), DELTA);
		assertEquals("y", expectedY, actual.getY(), DELTA);
	}

	@Test
	public void relationCenter_singleSegment() {
		assertPoint(130, 20, points(10, 20, 250, 20).getRelationCenter());
	}

	@Test
	public void relationCenter_multipleSegments_isHalfOfTotalLength() {
		// segments: 20 + 60 + 220 = 300 => center is 150 along the path, 70 into the last segment
		assertPoint(100, 80, points(10, 20, 30, 20, 30, 80, 250, 80).getRelationCenter());
	}

	@Test
	public void relationCenter_zeroLength_returnsStartPoint() {
		// #679: all points on top of each other must not return null
		assertPoint(20, 20, points(20, 20, 20, 20).getRelationCenter());
	}

	@Test
	public void dragBox_isCenteredOnRelationCenter() {
		RelationPointList list = points(10, 20, 30, 20, 30, 80, 250, 80);
		assertPoint(100, 80, list.getDragBox().getCenter());
	}
}
