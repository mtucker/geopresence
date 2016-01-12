package com.geopresence.mongodb.test;

import com.geopresence.event.GeopresenceEventDispatcher;
import com.geopresence.event.GeopresenceEventListener;
import com.geopresence.mongodb.MongodbGeopresenceManager;
import com.mongodb.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MongodbGeopresenceManagerTest extends MongodbGeopresenceBaseTest {

  protected MongodbGeopresenceManager geopresenceManager;

  private MongoClient m;
  private DB db;
  private DBCollection entities;

  @BeforeClass
  public void setupDatabase() throws UnknownHostException {

    geopresenceManager = new MongodbGeopresenceManager();

    m = new MongoClient();
    assert m != null : "MongoClient could not be instantiated.";

    db = m.getDB("geopresence");
    assert db != null : "DB 'geopresence' could not be found.";

    entities = db.getCollection("entities");
    entities.ensureIndex(new BasicDBObject("loc", "2d"), "geospacialIdx");

  }

  @BeforeMethod
  public void resetCollection() {

    entities.drop();
    entities = db.getCollection("entities");

  }

  @Test
  public void testUpdateEntity() {

    geopresenceManager.updateEntity("test@test.com", home.getLat(), home.getLon(), 5000d);

    DBCursor results = entities.find(new BasicDBObject("name", "test@test.com"));
    assert results.hasNext();

    DBObject result = results.next();
    assert result != null;

  }

  @Test
  public void testRemoveEntity() {

    final BasicDBObject loc = new BasicDBObject("name", "test@test.com");
    loc.put("loc", new double[]{home.getLat(), home.getLon()});
    entities.update(new BasicDBObject("name", "test@test.com"), loc, true, false);

    geopresenceManager.removeEntity("test@test.com");

    DBCursor results = entities.find(new BasicDBObject("name", "test@test.com"));
    assert !results.hasNext();

  }

  @Test
  public void testEntityExists() {

    final BasicDBObject loc = new BasicDBObject("name", "test@test.com");
    loc.put("loc", new double[]{home.getLat(), home.getLon()});
    entities.update(new BasicDBObject("name", "test@test.com"), loc, true, false);

    assert geopresenceManager.entityExists("test@test.com");

  }

  //@Test
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

    assert actualEventParams.size() == 2 : "Expected 2, got " + actualEventParams.size();

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

  //@Test
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
