package com.geopresence.openfire.test;

import com.beoui.geocell.model.Point;
import com.geopresence.openfire.test.mock.MockGeopresencePlugin;
import org.testng.annotations.BeforeClass;

public class GeopresencePluginBaseTest {

  // 328 6th Ave, Brooklyn, NY
  protected Point home = new Point(40.671435, -73.98144);
  // 14 Wall St, New York, NY
  protected Point work = new Point(40.707612, -74.01075);
  // Hoboken, NY
  protected Point hoboken = new Point(40.7441, -74.0351);

  protected MockGeopresencePlugin mockGeopresencePlugin;

  @BeforeClass
  public void initializePlugin() throws Exception {

    this.mockGeopresencePlugin = new MockGeopresencePlugin();
    this.mockGeopresencePlugin.initializePlugin(null, null);

  }


}
