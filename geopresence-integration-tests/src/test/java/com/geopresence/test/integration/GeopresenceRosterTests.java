package com.geopresence.test.integration;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.geopresence.test.integration.util.RosterEventListener;
import com.geopresence.test.integration.util.RosterEventType;
import com.geopresence.xmpp.packet.GeoLoc;

/**
 * Verifies that we can create users and update their geographical location and
 * proximity radius.
 */
public class GeopresenceRosterTests extends GeopresenceIntegrationBaseTest {

  private static final Logger log = LoggerFactory.getLogger(GeopresenceRosterTests.class);

  private ConnectionConfiguration serverConnConfig;

  private String user1JID;

  private final String USER1_USERNAME = "user1." + System.currentTimeMillis();
  private final String USER1_PASSWORD = "password";
  private final String USER1_RESOURCE = "test";

  private XMPPConnection user1XMPPConn;
  private RosterEventListener user1RosterListener;

  private String user2JID;

  private final String USER2_USERNAME = "user2." + System.currentTimeMillis();
  private final String USER2_PASSWORD = "password";
  private final String USER2_RESOURCE = "test";

  private XMPPConnection user2XMPPConn;
  private RosterEventListener user2RosterListener;

  private Double maxProximity = 5000D;

  @BeforeClass
  public void setup() throws Exception {

    user1JID = USER1_USERNAME + "@" + XMPP_SERVER_HOST + "/" + USER1_RESOURCE;
    user2JID = USER2_USERNAME + "@" + XMPP_SERVER_HOST + "/" + USER2_RESOURCE;

    // Create the configuration for this new connection
    serverConnConfig = new ConnectionConfiguration(XMPP_SERVER_HOST, XMPP_CLIENT_TO_SERVER_PORT);
    serverConnConfig.setCompressionEnabled(true);
    serverConnConfig.setSASLAuthenticationEnabled(false);

  }

  @Test
  public void testCreateAccount() throws Exception {

    // Create User1
    user1XMPPConn = new XMPPConnection(serverConnConfig);
    user1XMPPConn.connect();

    AccountManager acctManager = user1XMPPConn.getAccountManager();
    acctManager.createAccount(USER1_USERNAME, USER1_PASSWORD);

    user1XMPPConn.login(USER1_USERNAME, USER1_PASSWORD, USER1_RESOURCE);

    assert user1XMPPConn.isAuthenticated();

    user1RosterListener = new RosterEventListener(USER1_USERNAME);
    user1XMPPConn.getRoster().addRosterListener(user1RosterListener);

    // Create User2
    user2XMPPConn = new XMPPConnection(serverConnConfig);
    user2XMPPConn.connect();
    acctManager = user2XMPPConn.getAccountManager();
    acctManager.createAccount(USER2_USERNAME, USER2_PASSWORD);

    user2XMPPConn.login(USER2_USERNAME, USER2_PASSWORD, USER2_RESOURCE);

    assert user2XMPPConn.isAuthenticated();

    user2RosterListener = new RosterEventListener(USER2_USERNAME);
    user2XMPPConn.getRoster().addRosterListener(user2RosterListener);

  }

  @Test(dependsOnMethods = "testCreateAccount")
  public void testGeoLocCreate() throws Exception {

    // Create a new geographical location
    GeoLoc geoLoc = new GeoLoc();
    geoLoc.setLat(home.getLat());
    geoLoc.setLon(home.getLon());
    geoLoc.setMaxProximity(maxProximity);
    geoLoc.setType(Type.SET);

    // Move both users to same GeoLoc
    user1XMPPConn.sendPacket(geoLoc);

    user2XMPPConn.sendPacket(geoLoc);

    // Assert that the users were added to each other's roster
    assert user1RosterListener.waitForRosterEvent(RosterEventType.ADDED, 10000L, USER2_USERNAME);
    assert user2RosterListener.waitForRosterEvent(RosterEventType.ADDED, 10000L, USER1_USERNAME);

    assert user1XMPPConn.getRoster().contains(USER2_USERNAME) : user1JID + "'s Roster does not contain " + USER2_USERNAME;
    assert user2XMPPConn.getRoster().contains(USER1_USERNAME) : user2JID + "'s Roster does not contain " + USER1_USERNAME;

  }

  @Test(dependsOnMethods = "testCreateAccount")
  public void testGeoLocUpdate() throws Exception {

    // Create a new geographical location
    GeoLoc geoLoc = new GeoLoc();
    geoLoc.setLat(hoboken.getLat());
    geoLoc.setLon(hoboken.getLon());
    geoLoc.setMaxProximity(maxProximity);
    geoLoc.setType(Type.SET);

    // Update User 1's location and assert that
    // User 2 is no longer in her roster (User 2 is no longer within proximity)
    user1XMPPConn.sendPacket(geoLoc);
    assert user1RosterListener.waitForRosterEvent(RosterEventType.DELETED, 10000L, USER2_USERNAME);

    assert !user1XMPPConn.getRoster().contains(USER2_USERNAME) : user1JID + "'s Roster contains " + USER2_USERNAME;
    assert !user2XMPPConn.getRoster().contains(USER2_USERNAME) : user2JID + "'s Roster contains " + USER1_USERNAME;

    // Move User 2 to same location as User 1
    user2XMPPConn.sendPacket(geoLoc);

    // Assert that User 1 and 2 are now again on each
    // other's roster
    assert user1RosterListener.waitForRosterEvent(RosterEventType.ADDED, 10000L, USER2_USERNAME);

    assert user1XMPPConn.getRoster().contains(USER2_USERNAME) : user1JID + "'s Roster does not contain " + USER2_USERNAME;
    assert user2XMPPConn.getRoster().contains(USER1_USERNAME) : user2JID + "'s Roster does not contain " + USER1_USERNAME;

    // Move User 1 again but still within
    // User 2's proximity radius
    geoLoc.setLat(work.getLat());
    geoLoc.setLon(work.getLon());

    user1XMPPConn.sendPacket(geoLoc);

    // Assert that users 1 and 2 are still on each
    // other's rosters.
    assert !user1RosterListener.waitForRosterEvent(RosterEventType.DELETED, 10000L, USER2_USERNAME);

    assert user1XMPPConn.getRoster().contains(USER2_USERNAME) : user1JID + "'s Roster does not contain " + USER2_USERNAME;
    assert user2XMPPConn.getRoster().contains(USER1_USERNAME) : user2JID + "'s Roster does not contain " + USER1_USERNAME;

  }

  @Test(dependsOnMethods = "testCreateAccount")
  public void testProximityUpdate() throws Exception {

    // Create a new geographical location
    GeoLoc geoLoc = new GeoLoc();
    geoLoc.setLat(home.getLat());
    geoLoc.setLon(home.getLon());
    geoLoc.setMaxProximity(maxProximity);
    geoLoc.setType(Type.SET);

    // Move both users to same GeoLoc
    user1XMPPConn.sendPacket(geoLoc);
    user2XMPPConn.sendPacket(geoLoc);

    // Assert that the users were added to each other's roster
    assert user1XMPPConn.getRoster().contains(USER2_USERNAME) : user1JID + "'s Roster does not contain " + USER2_USERNAME;
    assert user2XMPPConn.getRoster().contains(USER1_USERNAME) : user2JID + "'s Roster does not contain " + USER1_USERNAME;

    // Move User 2 to new location
    // within User 1's proximity radius
    geoLoc.setLat(work.getLat());
    geoLoc.setLon(work.getLon());

    user2XMPPConn.sendPacket(geoLoc);

    // Assert that User 2 was not deleted from
    // from User 1's roster
    assert !user1RosterListener.waitForRosterEvent(RosterEventType.DELETED, 10000L, USER2_USERNAME);
    assert user1XMPPConn.getRoster().contains(USER2_USERNAME) : user1JID + "'s Roster does not contain " + USER2_USERNAME;

    // Update User 1's max proximity
    // so that User 2 is now no longer in range
    geoLoc.setLat(home.getLat());
    geoLoc.setLon(home.getLon());
    geoLoc.setMaxProximity(3000);

    // Assert that User 2 is no longer on
    // User 1's roster
    user1XMPPConn.sendPacket(geoLoc);
    assert user1RosterListener.waitForRosterEvent(RosterEventType.DELETED, 10000L, USER2_USERNAME);
    assert !user1XMPPConn.getRoster().contains(USER2_USERNAME) : user1JID + "'s Roster contains " + USER2_USERNAME;

  }

  @AfterClass(alwaysRun = true)
  public void cleanup() throws Exception {

    AccountManager acctManager = user1XMPPConn.getAccountManager();
    acctManager.deleteAccount();

    acctManager = user2XMPPConn.getAccountManager();
    acctManager.deleteAccount();

  }

}
