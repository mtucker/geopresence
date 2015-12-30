package com.geopresence.openfire.test;

import java.util.Date;

import com.geopresence.GeopresenceManager;
import com.geopresence.geocell.GeocellGeopresenceManager;
import org.jivesoftware.openfire.event.UserEventDispatcher;
import org.jivesoftware.openfire.event.UserEventDispatcher.EventType;
import org.jivesoftware.openfire.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.geopresence.openfire.test.mock.MockUser;

public class UserUpdateTest extends GeopresencePluginBaseTest {
	
	private static final Logger log = LoggerFactory.getLogger(UserUpdateTest.class);

	private GeopresenceManager geopresenceManager;
	
	@BeforeClass
	public void setup() throws Exception {
		
		geopresenceManager = GeocellGeopresenceManager.getInstance();
		
	}
	
	@Test
	public void testDeleteUser() throws Exception {
		
		User user = new MockUser("test1@test.com", "Test User 1", "test@test.com", new Date(), new Date());

        geopresenceManager.updateEntity("test1@test.com", home.getLat(), home.getLon(), 5000d);
		
		assert geopresenceManager.entityExists(user.getUsername());
		
		UserEventDispatcher.dispatchEvent(user, EventType.user_deleting, null);

        assert !geopresenceManager.entityExists(user.getUsername());
		
	}

}
