package com.geopresence.test.integration;

import com.beoui.geocell.model.Point;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.testng.annotations.BeforeSuite;

public class GeopresenceIntegrationBaseTest {
	
	protected static final String XMPP_SERVER_HOST = "127.0.0.1";
	protected static final Integer XMPP_CLIENT_TO_SERVER_PORT = 5222;
	
	// 328 6th Ave, Brooklyn, NY
	protected Point home = new Point(40.671435, -73.98144);
	// 14 Wall St, New York, NY
	protected Point work = new Point(40.707612, -74.01075);
	// Hoboken, NY
	protected Point hoboken = new Point(40.7441, -74.0351);
	
	@BeforeSuite
	public void testServerAvailability() throws Exception {
		
		// Create the configuration for this new connection
		ConnectionConfiguration serverConnConfig = new ConnectionConfiguration(XMPP_SERVER_HOST, XMPP_CLIENT_TO_SERVER_PORT);
		serverConnConfig.setCompressionEnabled(true);
		serverConnConfig.setSASLAuthenticationEnabled(false);
		
	}

}
