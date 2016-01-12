package com.beoui.utils;

import java.util.List;

import org.testng.annotations.Test;

import com.beoui.geocell.GeocellUtils;
import com.beoui.geocell.model.BoundingBox;
import com.beoui.geocell.model.Point;

public class GeocellTests {

  @Test
  public void testGetCellsWithinRadius() throws Exception {

    Double lat = 40.638967;
    Double lon = -73.87207;

    Double distance = 5000.0;

    // Transform it to a point
    Point p = new Point(lat, lon);

    List<String> cells = GeocellUtils.getCellsWithinRadius(p, distance);

    assert cells != null;

    for (String cell : cells) {

      if (!GeocellUtils.withinRadius(p, cell, distance)) {

        BoundingBox bb = GeocellUtils.computeBox(cell);

        StringBuffer msg = new StringBuffer("Cell " + cell + " is not within " + distance + " meters of point." + "\n");
        msg.append("North: " + bb.getNorth() + " East: " + bb.getEast() + " South: " + bb.getSouth() + " West: " + bb.getWest() + "\n");
        msg.append("Minimum Distance: " + GeocellUtils.pointDistance(cell, p));

        System.out.println(msg.toString());

      }

    }

  }

}
