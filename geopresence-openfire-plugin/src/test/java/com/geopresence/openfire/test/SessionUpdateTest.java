package com.geopresence.openfire.test;

import com.geopresence.GeopresenceManager;
import com.geopresence.geocell.GeocellGeopresenceManager;
import org.jivesoftware.openfire.event.SessionEventDispatcher;
import org.jivesoftware.openfire.event.SessionEventDispatcher.EventType;
import org.jivesoftware.openfire.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;

import com.geopresence.geocell.GeocellGridManager;
import com.geopresence.openfire.test.mock.MockSession;

public class SessionUpdateTest extends GeopresencePluginBaseTest {

  private static final Logger log = LoggerFactory.getLogger(SessionUpdateTest.class);

  private GeopresenceManager geopresenceManager;
  private GeocellGridManager geocellGridManager;

  @BeforeClass
  public void setup() throws Exception {

    geopresenceManager = GeocellGeopresenceManager.getInstance();
    geocellGridManager = GeocellGridManager.getInstance();


  }

  //@Test
  public void testDeleteUser() throws Exception {

    geopresenceManager.updateEntity("test1@test.com", home.getLat(), home.getLon(), 5000d);

    assert geopresenceManager.entityExists("test1@test.com");

    Session session = new MockSession("test1@test.com");

    SessionEventDispatcher.dispatchEvent(session, EventType.session_destroyed);

    assert !geopresenceManager.entityExists("test1@test.com");

  }

}
