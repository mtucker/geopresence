package com.geopresence.geocell.test;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.testng.annotations.Test;

import com.geopresence.geocell.model.Geocell;
import com.geopresence.geocell.model.GeocellEntity;
import com.geopresence.geocell.utils.GeocellUtils;

public class PlaceAndMoveTests extends GeocellBaseTest {

	private GeocellEntity entity;
	
	@Test
	public void testPlaceEntity() throws Exception {
		
		List<String> expectedOccupiedCells = GeocellUtils.generateGeoCell(home);
		
		entity = gridManager.placeEntity("test@test.com", home, 5000d);
		
		assert entity != null;
		assert entity.getName().equals(ENTITY_NAME);
		
		List<Geocell> occupiedCells = entity.getOccupiedCells();
		
		assertCells(occupiedCells, expectedOccupiedCells);
		
		List<String> expectedProximateCells = GeocellUtils.getCellsWithinRadius(home, 5000d);
		
		List<Geocell> proximateCells = entity.getProximateCells();
		
		assertCells(proximateCells, expectedProximateCells);
		
	}
	
	@Test(dependsOnMethods="testPlaceEntity")
	public void testMoveEntity() throws Exception {
		
		List<String> expectedOccupiedCells = GeocellUtils.generateGeoCell(work);
		
		entity = gridManager.moveEntity("test@test.com", work, 5000d);
		
		assert entity != null;
		assert entity.getName().equals(ENTITY_NAME);
		
		List<Geocell> occupiedCells = entity.getOccupiedCells();
		
		assertCells(occupiedCells, expectedOccupiedCells);
		
		List<String> expectedProximateCells = GeocellUtils.getCellsWithinRadius(home, 5000d);
		
		List<Geocell> proximateCells = entity.getProximateCells();
		
		assertCells(proximateCells, expectedProximateCells);
		
	}
	
	@Test(dependsOnMethods="testMoveEntity")
	public void testRemoveEntity() throws Exception {
		
		List<Geocell> occupiedCells = entity.getOccupiedCells();
		List<Geocell> proximateCells = entity.getProximateCells();
		
		for(Geocell cell : occupiedCells){
			
			assert cell.getOccupyingEntities().contains(entity);
			
		}
		
		for(Geocell cell : proximateCells){
			
			assert cell.getProximateEntities().contains(entity);
			
		}
		
		assert gridManager.getGrid().containsEntity(entity.getName());
		
		gridManager.removeEntity(entity.getName());
		
		for(Geocell cell : occupiedCells){
			
			assert !cell.getOccupyingEntities().contains(entity);
			
		}
		
		for(Geocell cell : proximateCells){
			
			assert !cell.getProximateEntities().contains(entity);
			
		}
		
		assert !gridManager.getGrid().containsEntity(entity.getName());		
		
	}
}
