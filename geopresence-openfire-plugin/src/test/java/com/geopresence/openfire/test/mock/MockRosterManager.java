package com.geopresence.openfire.test.mock;

import java.util.HashMap;

import org.jivesoftware.openfire.roster.Roster;
import org.jivesoftware.openfire.roster.RosterManager;

public class MockRosterManager extends RosterManager {

  HashMap<String, Roster> rosterMap = new HashMap<String, Roster>();

  public MockRosterManager() {


  }

  @Override
  public Roster getRoster(String username) {

    if (rosterMap.get(username) == null) {

      rosterMap.put(username, new Roster());

    }

    return rosterMap.get(username);

  }

}
