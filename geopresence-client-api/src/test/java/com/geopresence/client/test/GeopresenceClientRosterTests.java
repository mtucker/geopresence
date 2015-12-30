package com.geopresence.client.test;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.geopresence.client.test.util.RosterEventListener;
import com.geopresence.client.test.util.RosterEventType;
import com.geopresence.xmpp.packet.GeoLoc;

public class GeopresenceClientRosterTests extends GeopresenceClientBaseTest {
	
	private static final Logger log = LoggerFactory.getLogger(GeopresenceClientRosterTests.class);

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
	public void setup() throws Exception{
		
		user1JID = USER1_USERNAME + "@" + SERVER_NAME + "/" + USER1_RESOURCE;
		user2JID = USER2_USERNAME + "@" + SERVER_NAME + "/" + USER2_RESOURCE;
		
		// Create the configuration for this new connection
		serverConnConfig = new ConnectionConfiguration(SERVER_NAME, SERVER_PORT);
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
	
	@Test(dependsOnMethods="testCreateAccount")
	public void testGeoLocCreate() throws Exception {
		
		GeoLoc geoLoc = new GeoLoc();
		geoLoc.setLat(home.getLat());
		geoLoc.setLon(home.getLon());
		geoLoc.setMaxProximity(maxProximity);
		geoLoc.setType(Type.SET);
		
		user1XMPPConn.sendPacket(geoLoc);
		
		user2XMPPConn.sendPacket(geoLoc);
		
		assert user1RosterListener.waitForRosterEvent(RosterEventType.ADDED, 10000L, USER2_USERNAME);
		assert user2RosterListener.waitForRosterEvent(RosterEventType.ADDED, 10000L, USER1_USERNAME);
		
		assert user1XMPPConn.getRoster().contains(USER2_USERNAME) : user1JID + "'s Roster does not contain " + USER2_USERNAME;
		assert user2XMPPConn.getRoster().contains(USER1_USERNAME) : user2JID + "'s Roster does not contain " + USER1_USERNAME;
		
	}
	
	@Test(dependsOnMethods="testCreateAccount")
	public void testGeoLocUpdate() throws Exception {
		
		GeoLoc geoLoc = new GeoLoc();
		geoLoc.setLat(hoboken.getLat());
		geoLoc.setLon(hoboken.getLon());
		geoLoc.setMaxProximity(maxProximity);
		geoLoc.setType(Type.SET);
		
		user1XMPPConn.sendPacket(geoLoc);
		assert user1RosterListener.waitForRosterEvent(RosterEventType.DELETED, 10000L, USER2_USERNAME);
		
		assert !user1XMPPConn.getRoster().contains(USER2_USERNAME) : user1JID + "'s Roster contains " + USER2_USERNAME;
		assert !user2XMPPConn.getRoster().contains(USER2_USERNAME) : user2JID + "'s Roster contains " + USER1_USERNAME;
		
		user2XMPPConn.sendPacket(geoLoc);
		
		assert user1RosterListener.waitForRosterEvent(RosterEventType.ADDED, 10000L, USER2_USERNAME);
		
		assert user1XMPPConn.getRoster().contains(USER2_USERNAME) : user1JID + "'s Roster does not contain " + USER2_USERNAME;
		assert user2XMPPConn.getRoster().contains(USER1_USERNAME) : user2JID + "'s Roster does not contain " + USER1_USERNAME;
		
		geoLoc.setLat(work.getLat());
		geoLoc.setLon(work.getLon());
		
		user1XMPPConn.sendPacket(geoLoc);
		assert !user1RosterListener.waitForRosterEvent(RosterEventType.DELETED, 10000L, USER2_USERNAME);
		
		assert user1XMPPConn.getRoster().contains(USER2_USERNAME) : user1JID + "'s Roster does not contain " + USER2_USERNAME;
		assert user2XMPPConn.getRoster().contains(USER1_USERNAME) : user2JID + "'s Roster does not contain " + USER1_USERNAME;
		
	}
	
	@Test(dependsOnMethods="testCreateAccount")
	public void testProximityUpdate() throws Exception{
		
		GeoLoc geoLoc = new GeoLoc();
		geoLoc.setLat(home.getLat());
		geoLoc.setLon(home.getLon());
		geoLoc.setMaxProximity(maxProximity);
		geoLoc.setType(Type.SET);
		
		user1XMPPConn.sendPacket(geoLoc);
		user2XMPPConn.sendPacket(geoLoc);
		
		//assert user1RosterListener.waitForRosterEvent(RosterEventType.ADDED, 10000L, USER2_USERNAME);
		
		assert user1XMPPConn.getRoster().contains(USER2_USERNAME) : user1JID + "'s Roster does not contain " + USER2_USERNAME;
		assert user2XMPPConn.getRoster().contains(USER1_USERNAME) : user2JID + "'s Roster does not contain " + USER1_USERNAME;
		
		geoLoc.setLat(work.getLat());
		geoLoc.setLon(work.getLon());
		
		user2XMPPConn.sendPacket(geoLoc);
		
		assert !user1RosterListener.waitForRosterEvent(RosterEventType.DELETED, 10000L, USER2_USERNAME);
		assert user1XMPPConn.getRoster().contains(USER2_USERNAME) : user1JID + "'s Roster does not contain " + USER2_USERNAME;
		
		geoLoc.setLat(home.getLat());
		geoLoc.setLon(home.getLon());
		geoLoc.setMaxProximity(3000);
		
		user1XMPPConn.sendPacket(geoLoc);
		assert user1RosterListener.waitForRosterEvent(RosterEventType.DELETED, 10000L, USER2_USERNAME);
		assert !user1XMPPConn.getRoster().contains(USER2_USERNAME) : user1JID + "'s Roster contains " + USER2_USERNAME;
		
	}
	
	@AfterClass(alwaysRun=true)
	public void cleanup() throws Exception {
		
		AccountManager acctManager = user1XMPPConn.getAccountManager();
		acctManager.deleteAccount();
		
		acctManager = user2XMPPConn.getAccountManager();
		acctManager.deleteAccount();
		
	}

}
