package com.geopresence.openfire.test.mock;

import com.geopresence.openfire.GeopresencePlugin;
import org.jivesoftware.openfire.roster.RosterManager;

public class MockGeopresencePlugin extends GeopresencePlugin {

    private RosterManager rosterManager = new MockRosterManager();

    @Override
    public RosterManager getRosterManager(){

        return this.rosterManager;

    }

}
