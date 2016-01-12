package com.geopresence.test.integration;

import java.util.List;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.geopresence.test.integration.util.ChatListener;
import com.geopresence.test.integration.util.ChatMessageListener;
import com.geopresence.test.integration.util.RosterEventListener;
import com.geopresence.test.integration.util.RosterEventType;
import com.geopresence.xmpp.packet.GeoLoc;

/**
 * Verifies that users that are in proximity to each other can exchange messages.
 */
public class GeopresenceChatTest extends GeopresenceIntegrationBaseTest {

  private static final Logger log = LoggerFactory.getLogger(GeopresenceChatTest.class);

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

  @Test
  public void testChat() throws Exception {

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

    // Send a message from User 1 to User 2
    ChatManager user1Chatmanager = user1XMPPConn.getChatManager();
    ChatMessageListener user1ChatMessageListener = new ChatMessageListener();
    Chat user1Chat = user1Chatmanager.createChat(user1JID, user1ChatMessageListener);

    ChatListener user2ChatListener = new ChatListener();
    user2XMPPConn.getChatManager().addChatListener(user2ChatListener);
    ChatMessageListener user2ChatMessageListener = user2ChatListener.getMessageListener();

    user1Chat.sendMessage("Howdy!");

    Thread.sleep(3000);

    // Verify that User 2 received the message
    List<Message> messages = user2ChatMessageListener.getChats(user1JID);

    assert messages != null;
    assert messages.size() == 1;

    Message message = messages.get(0);
    assert message.getBody().equals("Howdy!");

  }

  @AfterClass(alwaysRun = true)
  public void cleanup() throws Exception {

    AccountManager acctManager = user1XMPPConn.getAccountManager();
    acctManager.deleteAccount();

    acctManager = user2XMPPConn.getAccountManager();
    acctManager.deleteAccount();

  }

}
