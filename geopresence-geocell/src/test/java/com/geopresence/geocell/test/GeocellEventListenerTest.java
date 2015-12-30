package com.geopresence.geocell.test;

import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.geopresence.geocell.model.GeocellEntity;
import com.geopresence.geocell.event.GeocellEventListener;
import com.geopresence.geocell.utils.GeocellUtils;

public class GeocellEventListenerTest extends GeocellBaseTest {
	
	private GeocellEntity entity;
	private List<String> expectedOccupiedCells;
	private List<String> expectedProximateCells;
	
	@Test
	public void testGeocellEventListenerForAddedEntity() throws Exception {
		
		gridManager.registerGeocellEventListener(new TestListener());
		
		expectedOccupiedCells = GeocellUtils.generateGeoCell(home);
		expectedProximateCells = GeocellUtils.getCellsWithinRadius(home, 5000d);
		
		entity = gridManager.placeEntity("test@test.com", home, 5000d);
		
		assert expectedOccupiedCells.size() == 0 : "Expected list size 0, got " + expectedOccupiedCells.size();
		assert expectedProximateCells.size() == 0 : "Expected list size 0, got " + expectedOccupiedCells.size();;
		
		
	}
	
	@Test
	public void testGeocellEventListenerForRemovedEntity() throws Exception {
		
		gridManager.registerGeocellEventListener(new TestListener());
		
		expectedOccupiedCells = GeocellUtils.generateGeoCell(home);
		expectedProximateCells = GeocellUtils.getCellsWithinRadius(home, 5000d);
		
		gridManager.removeEntity("test@test.com");
		
		assert expectedOccupiedCells.size() == 0 : "Expected list size 0, got " + expectedOccupiedCells.size();
		assert expectedProximateCells.size() == 0 : "Expected list size 0, got " + expectedProximateCells.size();;
		
	}
	
	private class TestListener implements GeocellEventListener {

		@Override
		public void occupyingEntityAdded(Map<String, Object> params) {
			
			String entityName = (String) params.get("entityName");
			String cellName = (String) params.get("cellName");
			
			if(entityName.equals("test@test.com")){
				expectedOccupiedCells.remove(cellName);
			}
			
		}

		@Override
		public void occupyingEntityVacated(Map<String, Object> params) {
			
			String entityName = (String) params.get("entityName");
			String cellName = (String) params.get("cellName");
			
			if(entityName.equals("test@test.com")){
				expectedOccupiedCells.remove(cellName);
			}
			
		}

		@Override
		public void proximateEntityAdded(Map<String, Object> params) {

			String entityName = (String) params.get("entityName");
			String cellName = (String) params.get("cellName");
			
			if(entityName.equals("test@test.com")){
				expectedProximateCells.remove(cellName);
			}
			
		}

		@Override
		public void proximateEntityRemoved(Map<String, Object> params) {
			
			String entityName = (String) params.get("entityName");
			String cellName = (String) params.get("cellName");
			
			if(entityName.equals("test@test.com")){
				expectedProximateCells.remove(cellName);
			}
			
		}
		
		
		
	}

}
