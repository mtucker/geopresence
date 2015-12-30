package com.geopresence;

public interface GeopresenceManager {

    public void updateEntity(String entityName, Double lat, Double lon, Double maxProximity);

    public void removeEntity(String entityName);

    public boolean entityExists(String entityName);

}
