package com.geopresence.openfire.test.mock;

import org.jivesoftware.openfire.PacketException;
import org.jivesoftware.openfire.handler.PresenceSubscribeHandler;
import org.jivesoftware.openfire.roster.Roster;
import org.jivesoftware.openfire.roster.RosterManager;
import org.xmpp.packet.Presence.Type;
import org.xmpp.packet.Presence;

public class MockPresenceSubscribeHandler extends PresenceSubscribeHandler {

  MockRosterManager rosterManager = new MockRosterManager();

  @Override
  public void process(Presence presence) throws PacketException {

    try {

      if (presence.getType().equals(Type.subscribe)) {

        Roster roster = rosterManager.getRoster(presence.getFrom().toString());
        roster.createRosterItem(presence.getTo(), true, false);

      } else if (presence.getType().equals(Type.unsubscribe)) {

        Roster roster = rosterManager.getRoster(presence.getFrom().toString());
        roster.deleteRosterItem(presence.getTo(), false);

      }

    } catch (Exception e) {
      throw new PacketException(e.getMessage());
    }

  }

  public RosterManager getRosterManager() {

    return this.rosterManager;

  }

}
