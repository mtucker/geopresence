package com.geopresence.geocell.test;

import java.util.List;

import com.beoui.geocell.model.BoundingBox;
import org.testng.annotations.Test;

import com.geopresence.geocell.utils.GeocellUtils;

public class GeocellUtilsTest extends GeocellBaseTest {
	
	@Test
	public void testCellGenerationConsistency() throws Exception {
		
		List<String> cells = null;
		
		for(int i = 0; i < 10; i++){

			List<String> newCells = GeocellUtils.generateGeoCell(home);
			
			if(cells != null){
				
				assert newCells.equals(cells);
				
			}
			
			cells = newCells;
			
		}
		
	}
	
	@Test
	public void testChangeProximity() throws Exception {
		
		Double distance = 5000D;
		
		BoundingBox bboxAT5000 = GeocellUtils.boundingCoordinates(home, distance);
    	
    	Integer resolution = 8;
    	
    	String cellNEAt5000 = GeocellUtils.compute(bboxAT5000.getNorthEast(), resolution);
        String cellSWAt5000 = GeocellUtils.compute(bboxAT5000.getSouthWest(), resolution);
        
        List<String> cellsAt5000 = GeocellUtils.getCellsWithinRadius(home, distance);
        
        distance = 3000D;
		
        BoundingBox bboxAT3000 = GeocellUtils.boundingCoordinates(home, distance);
		
		String cellNEAt3000 = GeocellUtils.compute(bboxAT3000.getNorthEast(), resolution);
        String cellSWAt3000 = GeocellUtils.compute(bboxAT3000.getSouthWest(), resolution);
        
        List<String> cellsAt3000 = GeocellUtils.getCellsWithinRadius(home, distance);
        
        assert cellNEAt5000 != cellNEAt3000;
        assert cellSWAt5000 != cellSWAt3000;
        
        assert !cellsAt5000.equals(cellsAt3000);
		
	}
	
	@Test
	public void testProximateCellResolution() throws Exception {
		
		for(Double maxProximity = 5000D; maxProximity >= 10D; maxProximity = maxProximity - 10D){
		
			List<String> proxCells = GeocellUtils.getCellsWithinRadius(home, maxProximity);
			
			assert proxCells != null && proxCells.size() > 0 : "No proximate cells calculated for distance " + maxProximity;
			
			for(String proxCell : proxCells){
				
				Double distanceFromPointToCell = GeocellUtils.pointDistance(proxCell, home);
				assert distanceFromPointToCell <= maxProximity : "Expected max distance of " + maxProximity + ", got " + distanceFromPointToCell;
				
			}
		
		}
		
	}

}
