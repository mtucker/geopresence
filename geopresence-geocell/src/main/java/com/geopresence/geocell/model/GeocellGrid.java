package com.geopresence.geocell.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeocellGrid {

  private Map<String, Geocell> geocells;
  private Map<String, GeocellEntity> entities;

  public void setGeocells(Map<String, Geocell> geocells) {
    this.geocells = geocells;
  }

  public Map<String, Geocell> getGeocells() {
    return geocells;
  }

  public void addGeocell(Geocell cell) {

    if (this.geocells == null) {

      setGeocells(new HashMap<String, Geocell>());

    }

    getGeocells().put(cell.getName(), cell);

  }

  public Geocell getGeocell(String cellName) {

    if (getGeocells() != null) {
      return getGeocells().get(cellName);
    }

    return null;

  }

  public Geocell findOrCreateGeocell(String cellName) {

    if (getGeocell(cellName) == null) {

      addGeocell(new Geocell(cellName));

    }

    return getGeocell(cellName);

  }

  public void setEntities(Map<String, GeocellEntity> entities) {
    this.entities = entities;
  }

  public Map<String, GeocellEntity> getEntities() {
    return entities;
  }

  public void addEntity(GeocellEntity entity) {

    if (this.entities == null) {

      setEntities(new HashMap<String, GeocellEntity>());

    }

    getEntities().put(entity.getName(), entity);
  }

  public void removeEntity(GeocellEntity entity) {

    if (entity.getOccupiedCells() != null && entity.getOccupiedCells().size() > 0) {
      vacateGeocells(entity, entity.getOccupiedCells());
    }

    if (entity.getProximateCells() != null && entity.getProximateCells().size() > 0) {
      removeProximateEntity(entity, entity.getProximateCells());
    }

    getEntities().remove(entity.getName());

  }

  public GeocellEntity getEntity(String entityName) {

    if (getEntities() != null) {
      return getEntities().get(entityName);
    }

    return null;

  }

  public Boolean containsEntity(String entityName) {

    return getEntities() != null && getEntities().containsKey(entityName);

  }

  public void occupyGeocell(GeocellEntity entity, String cellName) {

    Geocell cell = findOrCreateGeocell(cellName);
    cell.addOccupyingEntity(entity);

    if (!containsEntity(entity.getName())) {
      addEntity(entity);
    }
  }

  public void vacateGeocell(GeocellEntity entity, Geocell cell) {

    cell.vacateOccupyingEntity(entity);

    if (entity.getOccupiedCells() == null || entity.getOccupiedCells().size() == 0) {
      removeEntity(entity);
    }

  }

  public void vacateGeocells(GeocellEntity entity, List<Geocell> cells) {

    for (Geocell cell : cells) {
      vacateGeocell(entity, cell);
    }

  }

  public void removeProximateEntity(GeocellEntity entity, Geocell cell) {

    cell.removeProximateEntity(entity);

  }

  public void removeProximateEntity(GeocellEntity entity, List<Geocell> cells) {

    for (Geocell cell : cells) {
      removeProximateEntity(entity, cell);
    }

  }

}
