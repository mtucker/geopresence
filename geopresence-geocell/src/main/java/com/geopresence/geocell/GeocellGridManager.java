package com.geopresence.geocell;

import java.util.ArrayList;
import java.util.List;

import com.beoui.geocell.model.Point;
import com.geopresence.geocell.model.Geocell;
import com.geopresence.geocell.model.GeocellEntity;
import com.geopresence.geocell.model.GeocellGrid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.geopresence.geocell.event.GeocellEventDispatcher;
import com.geopresence.geocell.event.GeocellEventListener;
import com.geopresence.geocell.utils.GeocellUtils;

public class GeocellGridManager {

  private static final Logger log = LoggerFactory.getLogger(GeocellGridManager.class);

  private GeocellGrid grid;

  private static final class GeocellGridManagerContainer {
    private static final GeocellGridManager instance = new GeocellGridManager();
  }

  public static GeocellGridManager getInstance() {
    return GeocellGridManagerContainer.instance;
  }

  private GeocellGridManager() {

    grid = new GeocellGrid();

  }

  public GeocellEntity placeOrMoveEntity(String entityName, Point point, Double maxProximity) {

    if (!grid.containsEntity(entityName)) {
      return placeEntity(entityName, point, maxProximity);
    } else {
      return moveEntity(entityName, point, maxProximity);
    }

  }

  public GeocellEntity placeEntity(String entityName, Point point, Double maxProximity) {

    log.info("Placing Entity '" + entityName + "' at " + point.getLat() + "," + point.getLon() + " in Geocell Grid");

    GeocellEntity entity = new GeocellEntity(entityName, point, maxProximity);

    // Update cells this entity occupies
    List<String> occupiedCells = GeocellUtils.generateGeoCell(point);

    for (String occupiedCellName : occupiedCells) {

      grid.occupyGeocell(entity, occupiedCellName);

    }

    // Update cells in proximity to this entity
    List<String> proximateCells = GeocellUtils.getCellsWithinRadius(point, maxProximity);

    for (String proximateCellName : proximateCells) {

      Geocell proximateCell = grid.findOrCreateGeocell(proximateCellName);
      proximateCell.addProximateEntity(entity);

    }

    return entity;
  }

  public GeocellEntity moveEntity(String entityName, Point point, Double maxProximity) {

    log.info("Moving Entity '" + entityName + "' to " + point.getLat() + "," + point.getLon() + " in Geocell Grid");

    GeocellEntity entity = grid.getEntity(entityName);

    List<Geocell> formerlyOccupiedCells = entity.getOccupiedCells();
    List<String> presentlyOccupiedCellNames = GeocellUtils.generateGeoCell(point);

    List<Geocell> cellsToVacate = new ArrayList<Geocell>();

    for (Geocell formerlyOccupiedCell : formerlyOccupiedCells) {

      if (presentlyOccupiedCellNames.contains(formerlyOccupiedCell.getName())) {

        // If the entity still occupies the cell, remove it from the list

        presentlyOccupiedCellNames.remove(formerlyOccupiedCell.getName());

      } else if (!presentlyOccupiedCellNames.contains(formerlyOccupiedCell.getName())) {

        // If the entity no longer occupies the cell, vacate it and remove it from the list

        cellsToVacate.add(formerlyOccupiedCell);
        presentlyOccupiedCellNames.remove(formerlyOccupiedCell.getName());

      }

    }

    // The presentlyOccupiedCellNames List should now only contain new cells to occupy

    for (String newlyOccupiedCellName : presentlyOccupiedCellNames) {

      grid.occupyGeocell(entity, newlyOccupiedCellName);

    }

    grid.vacateGeocells(entity, cellsToVacate);

    // Update cells in proximity to this entity
    List<Geocell> formerlyProximateCells = entity.getProximateCells();
    List<String> presentlyProximateCellNames = GeocellUtils.getCellsWithinRadius(point, maxProximity);

    List<Geocell> cellsToRemove = new ArrayList<Geocell>();

    if (formerlyProximateCells != null) {

      for (Geocell formerlyProximateCell : formerlyProximateCells) {

        if (presentlyProximateCellNames.contains(formerlyProximateCell.getName())) {

          // If the entity is still in proximity the cell, remove it from the list

          presentlyProximateCellNames.remove(formerlyProximateCell.getName());

        } else if (!presentlyProximateCellNames.contains(formerlyProximateCell.getName())) {

          // If the entity no longer in proximity the cell, vacate it and remove it from the list

          cellsToRemove.add(formerlyProximateCell);

        }

      }

    }

    grid.removeProximateEntity(entity, cellsToRemove);

    // The newlyProximateCellName List should now only contain new cells to mark as in proximity

    for (String newlyProximateCellName : presentlyProximateCellNames) {

      Geocell cell = grid.findOrCreateGeocell(newlyProximateCellName);
      cell.addProximateEntity(entity);

    }

    return entity;
  }

  public void removeEntity(GeocellEntity entity) {

    if (entity != null && grid.containsEntity(entity.getName())) {

      log.info("Removing Entity '" + entity.getName() + "' from Geocell Grid");

    }

    grid.removeEntity(entity);

  }

  public void removeEntity(String entityName) {

    GeocellEntity entity = grid.getEntity(entityName);

    if (entity != null) {
      removeEntity(entity);
    }

  }

  public GeocellGrid getGrid() {

    return grid;

  }

  public Geocell getGeocell(String cellName) {

    return grid.findOrCreateGeocell(cellName);

  }

  public GeocellEntity getGeocellEntity(String entityName) {

    return grid.getEntity(entityName);

  }

  public void registerGeocellEventListener(GeocellEventListener listener) {

    GeocellEventDispatcher.addListener(listener);

  }

}
