package com.geopresence.spark.plugin;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.Workspace;
import org.jivesoftware.spark.component.tabbedPane.SparkTabbedPane;
import org.jivesoftware.spark.plugin.Plugin;
import org.jivesoftware.spark.ui.DataFormUI;

import com.geopresence.xmpp.packet.GeoLoc;


public class GeopresenceSparkPlugin implements Plugin {

  private DataFormUI dataForm;

  @Override
  public boolean canShutDown() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void initialize() {
    System.out.println("Initializing GeopresenceSparkPlugin...");

    addTabToSpark();
  }

  @Override
  public void shutdown() {
    // TODO Auto-generated method stub

  }

  @Override
  public void uninstall() {
    // TODO Auto-generated method stub

  }

  /**
   * Adds a tab to Spark
   */
  private void addTabToSpark() {
    // Get Workspace UI from SparkManager
    Workspace workspace = SparkManager.getWorkspace();

    // Retrieve the Tabbed Pane from the WorkspaceUI.
    SparkTabbedPane tabbedPane = workspace.getWorkspacePane();

    // Add own Tab.
    FormField lat = new FormField("lat");
    lat.setType(FormField.TYPE_TEXT_SINGLE);
    lat.setLabel("Latitude");

    FormField lon = new FormField("lon");
    lon.setType(FormField.TYPE_TEXT_SINGLE);
    lon.setLabel("Longitude");

    FormField maxProximity = new FormField("maxProximity");
    maxProximity.setType(FormField.TYPE_TEXT_SINGLE);
    maxProximity.setLabel("Max Proximity");

    Form form = new Form(Form.TYPE_FORM);
    form.addField(lat);
    form.addField(lon);
    form.addField(maxProximity);

    dataForm = new DataFormUI(form);

    JButton button = new JButton("Submit");
    button.addActionListener(new LocationUpdateListener());

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(2, 1));
    panel.add(dataForm);
    panel.add(button);

    tabbedPane.addTab("Location", null, panel);
  }

  private class LocationUpdateListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent event) {

      System.out.println("Location Update Button Pressed...");
      System.out.println("Event: " + event.getActionCommand());

      if (event.getActionCommand().equals("Submit")) {

        Form form = dataForm.getFilledForm();

        String latString = form.getField("lat").getValues().next();
        String lonString = form.getField("lon").getValues().next();
        String maxProximityString = form.getField("maxProximity").getValues().next();

        Double lat = Double.valueOf(latString);
        Double lon = Double.valueOf(lonString);
        Double maxProximity = Double.valueOf(maxProximityString);

        GeoLoc geoLoc = new GeoLoc();
        geoLoc.setLat(lat);
        geoLoc.setLon(lon);
        geoLoc.setMaxProximity(maxProximity);
        geoLoc.setType(Type.SET);

        SparkManager.getConnection().sendPacket(geoLoc);

      }

    }


  }

}
