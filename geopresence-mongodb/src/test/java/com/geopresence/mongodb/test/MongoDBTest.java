package com.geopresence.mongodb.test;

import com.mongodb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class MongoDBTest extends MongodbGeopresenceBaseTest {

  private static final Logger log = LoggerFactory.getLogger(MongoDBTest.class);

  private MongoClient m;

  @Test
  public void testMongoDBConnectivity() throws Exception {

    m = new MongoClient();
    assert m != null : "MongoClient could not be instantiated.";

    DB db = m.getDB("geopresence");
    assert db != null : "DB 'geopresence' could not be found.";


  }

}
