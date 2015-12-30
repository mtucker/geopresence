package com.geopresence.mongodb;

import com.geopresence.GeopresenceManager;
import com.geopresence.event.GeopresenceEventEmitter;
import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.LinkedList;

public class MongodbGeopresenceManager extends GeopresenceEventEmitter implements GeopresenceManager {

    private MongoClient m;
    private DB db;
    private DBCollection entities;

    public MongodbGeopresenceManager() throws UnknownHostException {

        m = new MongoClient();
        db = m.getDB("geopresence");
        entities = db.getCollection("entities");
        entities.ensureIndex(new BasicDBObject("loc", "2d"), "geospacialIdx");

    }

    // TODO ensure unique entityName
    // TODO standardize entityName vs username vs name
    @Override
    public void updateEntity(String entityName, Double lat, Double lon, Double maxProximity) {

        final BasicDBObject loc = new BasicDBObject("name", entityName);
        loc.put("loc", new double[] {lat, lon});
        entities.update(new BasicDBObject("name", entityName), loc, true, false);

        LinkedList circle = new LinkedList();

        circle.addLast(new double[] { lat, lon });

        circle.addLast(maxProximity);

        final BasicDBObject query
                = new BasicDBObject("loc", new BasicDBObject("$within", new BasicDBObject("$center", circle)))
                    .append("name", new BasicDBObject("$ne", entityName));

        for(DBObject entity : entities.find(query)){

            String proximateEntityName = (String) entity.get("name");
            entityIsProximateTo(entityName, proximateEntityName);

        }

    }

    @Override
    public void removeEntity(String entityName) {

        entities.remove(new BasicDBObject("name", "test@test.com"));

    }

    @Override
    public boolean entityExists(String entityName) {

        return entities.count(new BasicDBObject("name", "test@test.com")) > 0;

    }

}
