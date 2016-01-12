package com.geopresence.geocell.test;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.geopresence.geocell.model.Geocell;
import com.geopresence.geocell.model.GeocellEntity;

public class ProximateEntitiesTest extends GeocellBaseTest {

  private static final Logger log = LoggerFactory.getLogger(ProximateEntitiesTest.class);

  private GeocellEntity entity1;
  private GeocellEntity entity2;
  private GeocellEntity entity3;

  @Test
  public void testOneProximateEntity() throws Exception {

    entity1 = gridManager.placeEntity("test1@test.com", home, 5000d);
    entity2 = gridManager.placeEntity("test2@test.com", home, 5000d);

    assert entity1 != null;
    assert entity2 != null;

    List<Geocell> occupiedCells = entity1.getOccupiedCells();
    List<Geocell> proximateCells = entity1.getProximateCells();

    assert occupiedCells != null;
    assert proximateCells != null;

    int proximateCellsResolution = proximateCells.get(0).getName().length();

    Geocell occupiedCell = null;

    for (Geocell cell : occupiedCells) {

      if (cell.getName().length() == proximateCellsResolution) {

        occupiedCell = cell;
        break;

      }
    }

    assert occupiedCell != null;

    assert entity1.getOccupiedCells().contains(occupiedCell);
    assert entity2.getOccupiedCells().contains(occupiedCell);

    assert entity1.getProximateCells().contains(occupiedCell);
    assert entity2.getProximateCells().contains(occupiedCell);

    assert occupiedCell.getOccupyingEntities() != null;
    assert occupiedCell.getOccupyingEntities().contains(entity1);
    assert occupiedCell.getOccupyingEntities().contains(entity2);

    assert entity1.getProximateEntities().size() == 1;
    assert entity2.getProximateEntities().size() == 1;

    gridManager.removeEntity(entity1);
    gridManager.removeEntity(entity2);

  }

  @Test(dependsOnMethods = "testOneProximateEntity")
  public void testOneThousandProximateEntities() throws Exception {

    entity3 = gridManager.placeEntity("test3@test.com", home, 5000d);

    for (int i = 0; i < 1000; i++) {

      gridManager.placeEntity("test" + (i + 3) + "@test.com", home, 5000d);

    }

    assert entity3 != null;
    assert entity3.getProximateEntities().size() == 1000 : "Expected 1000 proximate entities, got " + entity3.getProximateEntities().size();

  }

  //@Test(dependsOnMethods="testOneThousandProximateEntities")
  public void testProximateEntitiesAfterMove() throws Exception {

    assert entity3.getProximateEntities().size() == 1000 : "Expected 1000 proximate entities, got " + entity3.getProximateEntities().size();

    int i = 1;

    for (GeocellEntity proximateEntity : entity3.getProximateEntities()) {

      gridManager.moveEntity(proximateEntity.getName(), hoboken, 5000d);

      if (i++ >= 500) {
        break;
      }

    }

    assert entity3.getProximateEntities().size() == 500 : "Expected 500 proximate entities, got " + entity3.getProximateEntities().size();

  }

}
