package com.geopresence.geocell;

import com.beoui.geocell.model.Point;
import com.geopresence.GeopresenceManager;
import com.geopresence.event.GeopresenceEventDispatcher;
import com.geopresence.event.GeopresenceEventEmitter;
import com.geopresence.geocell.event.GeocellEventDispatcher;
import com.geopresence.geocell.event.GeocellEventListener;
import com.geopresence.geocell.model.Geocell;
import com.geopresence.geocell.model.GeocellEntity;
import com.geopresence.geocell.utils.GeocellUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeocellGeopresenceManager extends GeopresenceEventEmitter implements GeopresenceManager, GeocellEventListener {

  private static final Logger log = LoggerFactory.getLogger(GeopresenceManager.class);

  private GeocellGridManager gridManager;

  private static final class GeocellGeopresenceManagerContainer {
    private static final GeocellGeopresenceManager instance = new GeocellGeopresenceManager();
  }

  public static GeocellGeopresenceManager getInstance() {
    return GeocellGeopresenceManagerContainer.instance;
  }

  private GeocellGeopresenceManager() {

    gridManager = GeocellGridManager.getInstance();
    GeocellEventDispatcher.addListener(this);

  }

  public void updateEntity(String entityName, Double lat, Double lon, Double maxProximity) {

    Point point = new Point(lat, lon);

    gridManager.placeOrMoveEntity(entityName, point, maxProximity);

  }

  public void removeEntity(String entityName) {

    gridManager.removeEntity(entityName);

  }

  public boolean entityExists(String entityName) {

    return gridManager.getGrid().containsEntity(entityName);

  }

  @Override
  public void occupyingEntityAdded(Map<String, Object> params) {

    // Add new occupant of cell to rosters of proximate users

    String username = (String) params.get("entityName");

    String cellName = (String) params.get("cellName");

    Geocell cell = gridManager.getGeocell(cellName);

    List<GeocellEntity> entities = cell.getProximateEntities();

    if (entities != null) {

      for (GeocellEntity entity : entities) {

        try {

          if (!entity.getName().equals(username)) {

            entityIsProximateTo(entity.getName(), username);

          }

        } catch (Exception e) {
          log.error("An exception occurred dispatching proximate event. User '" + username + "' is now proximate to '" + entity.getName() + "'");
          e.printStackTrace();
        }

      }

    }

  }

  @Override
  public void occupyingEntityVacated(Map<String, Object> params) {

    // Remove occupant from rosters of proximate users

    String username = (String) params.get("entityName");

    String cellName = (String) params.get("cellName");

    Geocell cell = gridManager.getGeocell(cellName);
    GeocellEntity vacatingEntity = gridManager.getGeocellEntity(username);

    List<GeocellEntity> entities = cell.getProximateEntities();

    if (entities != null) {

      for (GeocellEntity entity : entities) {

        try {

          if (!entity.getName().equals(username) && !entity.getProximateEntities().contains(vacatingEntity)) {

            entityIsNoLongerProximateTo(username, entity.getName());

          }

        } catch (Exception e) {
          log.error("An exception occurred dispatching proximate event. User '" + username + "' is no longer proximate to '" + entity.getName() + "'");
          e.printStackTrace();
        }

      }

    }

  }

  @Override
  public void proximateEntityAdded(Map<String, Object> params) {

    // Add occupants of proximate cell to roster

    String username = (String) params.get("entityName");
    String cellName = (String) params.get("cellName");

    Geocell cell = gridManager.getGeocell(cellName);
    GeocellEntity proximateEntity = gridManager.getGeocellEntity(username);

    List<GeocellEntity> entities = cell.getOccupyingEntities();

    if (entities != null) {

      for (GeocellEntity entity : entities) {

        try {

          if (!entity.getName().equals(username) && GeocellUtils.distance(entity.getLocation(), proximateEntity.getLocation()) <= proximateEntity.getMaxProximity()) {

            entityIsProximateTo(entity.getName(), username);

          }

        } catch (Exception e) {
          log.error("An exception occurred dispatching proximate event. User '" + entity.getName() + "' is now proximate to '" + username + "'");
          e.printStackTrace();
        }

      }

    }

  }

  @Override
  public void proximateEntityRemoved(Map<String, Object> params) {

    // Remove occupants of proximate cell from roster

    String username = (String) params.get("entityName");
    String cellName = (String) params.get("cellName");

    Geocell cell = gridManager.getGeocell(cellName);

    List<GeocellEntity> entities = cell.getOccupyingEntities();

    if (entities != null) {

      for (GeocellEntity entity : entities) {

        try {

          if (!entity.getName().equals(username)) {

            entityIsNoLongerProximateTo(username, entity.getName());

          }

        } catch (Exception e) {
          log.error("An exception occurred dispatching proximate event. User '" + entity.getName() + "' is no longer proximate to '" + username + "'");
          e.printStackTrace();
        }

      }

    }

  }


}
