package com.geopresence.geocell.test;

import java.util.HashMap;
import java.util.List;

import com.beoui.geocell.model.Point;
import com.geopresence.geocell.model.Geocell;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.testng.annotations.BeforeSuite;

import com.geopresence.geocell.GeocellGridManager;

public abstract class GeocellBaseTest {

  protected GeocellGridManager gridManager = GeocellGridManager.getInstance();

  protected String ENTITY_NAME = "test@test.com";

  // 328 6th Ave, Brooklyn, NY
  protected Point home = new Point(40.671435, -73.98144);
  // 14 Wall St, New York, NY
  protected Point work = new Point(40.707612, -74.01075);
  // Hoboken, NY
  protected Point hoboken = new Point(40.7441, -74.0351);

  protected HashMap<String, Point> locations;

  @BeforeSuite
  public void setupLocations() throws Exception {

    locations = new HashMap<String, Point>();

    locations.put("Home", home);
    locations.put("Work", work);
    locations.put("Hoboken", hoboken);

  }

  protected void assertCells(List<Geocell> cells, List<String> cellNamesToCompare) {

    assert cells != null;

    for (String expectedCellName : cellNamesToCompare) {

      assert CollectionUtils.exists(cells, new GeocellNamePredicate(expectedCellName))
          : "Cell " + expectedCellName + " was expected but is not in list.";

    }

  }

  private class GeocellNamePredicate implements Predicate {

    private String cellName;

    public GeocellNamePredicate(String cellName) {
      this.cellName = cellName;
    }

    public boolean evaluate(Object arg) {
      Geocell cell = (Geocell) arg;
      return cell.getName().equals(cellName);
    }

  }

}
