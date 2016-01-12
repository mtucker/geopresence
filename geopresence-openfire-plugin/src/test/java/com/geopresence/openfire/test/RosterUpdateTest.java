package com.geopresence.openfire.test;

import com.beoui.geocell.GeocellUtils;
import com.geopresence.GeopresenceManager;
import com.geopresence.geocell.GeocellGeopresenceManager;
import org.jivesoftware.openfire.roster.Roster;
import org.jivesoftware.openfire.roster.RosterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xmpp.packet.JID;

public class RosterUpdateTest extends GeopresencePluginBaseTest {

  private static final Logger log = LoggerFactory.getLogger(RosterUpdateTest.class);

  private GeopresenceManager geopresenceManager;
  private RosterManager rosterManager;

  @BeforeClass
  public void setup() throws Exception {

    geopresenceManager = GeocellGeopresenceManager.getInstance();
    rosterManager = mockGeopresencePlugin.getRosterManager();

    this.geopresenceManager.removeEntity("test1@test.com");
    this.geopresenceManager.removeEntity("test2@test.com");

  }

  @AfterMethod
  public void resetGrid() throws Exception {

    this.geopresenceManager.removeEntity("test1@test.com");
    this.geopresenceManager.removeEntity("test2@test.com");

  }

  @Test
  public void testPlacingEntity() throws Exception {

    geopresenceManager.updateEntity("test1@test.com", home.getLat(), home.getLon(), 5000d);
    assertRosterSize(rosterManager.getRoster("test1@test.com"), 0);

    geopresenceManager.updateEntity("test2@test.com", home.getLat(), home.getLon(), 5000d);

    Roster roster = rosterManager.getRoster("test1@test.com");
    assertRosterSize(roster, 1);
    assert roster.isRosterItem(new JID("test2@test.com"));

    roster = rosterManager.getRoster("test2@test.com");
    assertRosterSize(roster, 1);
    assert roster.isRosterItem(new JID("test1@test.com"));

  }

  @Test
  public void testMovingEntityWithinProximity() throws Exception {

    geopresenceManager.updateEntity("test1@test.com", home.getLat(), home.getLon(), 5000d);
    geopresenceManager.updateEntity("test2@test.com", home.getLat(), home.getLon(), 5000d);

    assertRosterSize(rosterManager.getRoster("test1@test.com"), 1);

    geopresenceManager.updateEntity("test1@test.com", work.getLat(), work.getLon(), 5000d);
    assertRosterSize(rosterManager.getRoster("test1@test.com"), 1);

    geopresenceManager.updateEntity("test2@test.com", hoboken.getLat(), hoboken.getLon(), 5000d);

    assertRosterSize(rosterManager.getRoster("test1@test.com"), 1);

  }

  @Test
  public void testMovingEntityOutOfProximity() throws Exception {

    geopresenceManager.updateEntity("test1@test.com", home.getLat(), home.getLon(), 5000d);
    geopresenceManager.updateEntity("test2@test.com", home.getLat(), home.getLon(), 5000d);

    assertRosterSize(rosterManager.getRoster("test1@test.com"), 1);
    assertRosterSize(rosterManager.getRoster("test2@test.com"), 1);

    geopresenceManager.updateEntity("test1@test.com", hoboken.getLat(), hoboken.getLon(), 5000d);

    assertRosterSize(rosterManager.getRoster("test2@test.com"), 0);
    assertRosterSize(rosterManager.getRoster("test1@test.com"), 0);

  }

  @Test
  public void testChangingProximity() throws Exception {

    geopresenceManager.updateEntity("test1@test.com", home.getLat(), home.getLon(), 5000d);
    geopresenceManager.updateEntity("test2@test.com", home.getLat(), home.getLon(), 5000d);

    assertRosterSize(rosterManager.getRoster("test1@test.com"), 1);

    geopresenceManager.updateEntity("test2@test.com", work.getLat(), work.getLon(), 5000d);

    assertRosterSize(rosterManager.getRoster("test1@test.com"), 1);

    geopresenceManager.updateEntity("test1@test.com", home.getLat(), home.getLon(), 3000d);

    Double distanceBetweenHomeAndWork = GeocellUtils.distance(home, work);

    assert distanceBetweenHomeAndWork > 3000;

    assertRosterSize(rosterManager.getRoster("test1@test.com"), 0);

  }

  @Test
  public void testRemovingEntity() throws Exception {

    geopresenceManager.updateEntity("test1@test.com", home.getLat(), home.getLon(), 5000d);
    geopresenceManager.updateEntity("test2@test.com", home.getLat(), home.getLon(), 5000d);

    assertRosterSize(rosterManager.getRoster("test1@test.com"), 1);

    geopresenceManager.removeEntity("test2@test.com");

    assertRosterSize(rosterManager.getRoster("test1@test.com"), 0);

  }

  private void assertRosterSize(Roster roster, Integer size) {

    assert roster != null : "Roster is null.";
    assert roster.getRosterItems() != null : "RosterItems is null.";
    assert roster.getRosterItems().size() == size : "Expected Roster size " + size + ", got " + roster.getRosterItems().size();

  }

}
