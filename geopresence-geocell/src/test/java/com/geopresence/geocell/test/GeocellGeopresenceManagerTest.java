package com.geopresence.geocell.test;

import com.geopresence.event.GeopresenceEventDispatcher;
import com.geopresence.event.GeopresenceEventListener;
import com.geopresence.geocell.GeocellGeopresenceManager;
import com.geopresence.geocell.model.Geocell;
import com.geopresence.geocell.model.GeocellEntity;
import com.geopresence.geocell.utils.GeocellUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;

public class GeocellGeopresenceManagerTest extends GeocellBaseTest {

  protected GeocellGeopresenceManager geopresenceManager = GeocellGeopresenceManager.getInstance();

  @BeforeMethod
  public void resetGrid() {

    geopresenceManager.removeEntity("test@test.com");
    geopresenceManager.removeEntity("test1@test.com");
    geopresenceManager.removeEntity("test2@test.com");

  }

  @Test
  public void testUpdateEntity() {

    geopresenceManager.updateEntity("test@test.com", home.getLat(), home.getLon(), 5000d);

    List<String> expectedOccupiedCells = GeocellUtils.generateGeoCell(home);
    GeocellEntity entity = gridManager.getGeocellEntity("test@test.com");
    List<Geocell> occupiedCells = entity.getOccupiedCells();

    assertCells(occupiedCells, expectedOccupiedCells);

  }

  @Test
  public void testRemoveEntity() {

    geopresenceManager.removeEntity("test@test.com");

    GeocellEntity entity = gridManager.getGeocellEntity("test@test.com");

    assert entity == null;

  }

  @Test
  public void testEntityExists() {

    geopresenceManager.updateEntity("test@test.com", home.getLat(), home.getLon(), 5000d);

    assert geopresenceManager.entityExists("test@test.com");

    geopresenceManager.removeEntity("test@test.com");

    assert !geopresenceManager.entityExists("test@test.com");

  }

  @Test
  public void testEntityIsProximateToEventEmission() {

    MockGeopresenceEventListener eventListener = new MockGeopresenceEventListener();
    GeopresenceEventDispatcher.addListener(eventListener);

    ArrayList<Map<String, Object>> expectedEventParams = new ArrayList<Map<String, Object>>();
    Map<String, Object> expectedEventParams1 = new HashMap<String, Object>();
    expectedEventParams1.put("subjectEntity", "test1@test.com");
    expectedEventParams1.put("proximateEntity", "test2@test.com");
    Map<String, Object> expectedEventParams2 = new HashMap<String, Object>();
    expectedEventParams2.put("subjectEntity", "test2@test.com");
    expectedEventParams2.put("proximateEntity", "test1@test.com");
    expectedEventParams.add(expectedEventParams1);
    expectedEventParams.add(expectedEventParams2);

    geopresenceManager.updateEntity("test1@test.com", home.getLat(), home.getLon(), 5000d);
    geopresenceManager.updateEntity("test2@test.com", home.getLat(), home.getLon(), 5000d);

    ArrayList<Map<String, Object>> actualEventParams = eventListener.getEntityIsProximateTo();

    assert actualEventParams.size() == 2 : "Expected 1, got " + actualEventParams.size();

    for (Map<String, Object> expectedEventParam : actualEventParams) {

      assert expectedEventParam.get("subjectEntity") == "test1@test.com"
          || expectedEventParam.get("subjectEntity") == "test2@test.com";

      if (expectedEventParam.get("subjectEntity").equals("test1@test.com")) {

        assert expectedEventParam.get("proximateEntity").equals("test2@test.com");

      } else if (expectedEventParam.get("subjectEntity").equals("test2@test.com")) {

        assert expectedEventParam.get("proximateEntity").equals("test1@test.com");

      }

    }

  }

  @Test
  public void testEntityIsNoLongerProximateToEventEmission() {

    MockGeopresenceEventListener eventListener = new MockGeopresenceEventListener();
    GeopresenceEventDispatcher.addListener(eventListener);

    ArrayList<Map<String, Object>> expectedEventParams = new ArrayList<Map<String, Object>>();
    Map<String, Object> expectedEventParams1 = new HashMap<String, Object>();
    expectedEventParams1.put("subjectEntity", "test1@test.com");
    expectedEventParams1.put("noLongerProximateEntity", "test2@test.com");
    Map<String, Object> expectedEventParams2 = new HashMap<String, Object>();
    expectedEventParams2.put("subjectEntity", "test2@test.com");
    expectedEventParams2.put("noLongerProximateEntity", "test1@test.com");
    expectedEventParams.add(expectedEventParams1);
    expectedEventParams.add(expectedEventParams2);

    geopresenceManager.updateEntity("test1@test.com", home.getLat(), home.getLon(), 5000d);
    geopresenceManager.updateEntity("test2@test.com", home.getLat(), home.getLon(), 5000d);

    geopresenceManager.updateEntity("test2@test.com", hoboken.getLat(), hoboken.getLon(), 5000d);

    ArrayList<Map<String, Object>> actualEventParams = eventListener.getEntityIsNoLongerProximateTo();

    assert actualEventParams.size() == 2 : "Expected 1, got " + actualEventParams.size();

    for (Map<String, Object> expectedEventParam : actualEventParams) {

      assert expectedEventParam.get("subjectEntity") == "test1@test.com"
          || expectedEventParam.get("subjectEntity") == "test2@test.com";

      if (expectedEventParam.get("subjectEntity").equals("test1@test.com")) {

        assert expectedEventParam.get("noLongerProximateEntity").equals("test2@test.com");

      } else if (expectedEventParam.get("subjectEntity").equals("test2@test.com")) {

        assert expectedEventParam.get("noLongerProximateEntity").equals("test1@test.com");

      }

    }

  }

  private class MockGeopresenceEventListener implements GeopresenceEventListener {

    private ArrayList<Map<String, Object>> entityIsProximateTo = new ArrayList<Map<String, Object>>();
    private ArrayList<Map<String, Object>> entityIsNoLongerProximateTo = new ArrayList<Map<String, Object>>();
    ;

    @Override
    public void entityIsProximateTo(Map<String, Object> params) {
      entityIsProximateTo.add(params);
    }

    @Override
    public void entityIsNoLongerProximateTo(Map<String, Object> params) {
      entityIsNoLongerProximateTo.add(params);
    }

    public ArrayList<Map<String, Object>> getEntityIsProximateTo() {
      return entityIsProximateTo;
    }

    public ArrayList<Map<String, Object>> getEntityIsNoLongerProximateTo() {
      return entityIsNoLongerProximateTo;
    }

  }

}
