package com.geopresence.client.test;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.testng.annotations.BeforeSuite;

import com.geopresence.model.Point;

public abstract class GeopresenceClientBaseTest {

  protected final String SERVER_NAME = "localhost";
  protected final Integer SERVER_PORT = 5222;

  // 328 6th Ave, Brooklyn, NY
  protected Point home = new Point(40.671435, -73.98144);
  // 14 Wall St, New York, NY
  protected Point work = new Point(40.707612, -74.01075);
  // Hoboken, NY
  protected Point hoboken = new Point(40.7441, -74.0351);

  @BeforeSuite
  public void testServerAvailability() throws Exception {

    // Create the configuration for this new connection
    ConnectionConfiguration serverConnConfig = new ConnectionConfiguration(SERVER_NAME, SERVER_PORT);
    serverConnConfig.setCompressionEnabled(true);
    serverConnConfig.setSASLAuthenticationEnabled(false);

  }

}
