package com.geopresence.geocell.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.geopresence.geocell.model.Geocell;

public class GeocellCollectionUtils extends CollectionUtils {
	
	public static List<Geocell> clone(List<Geocell> cells){
		
		List<Geocell> clonedList = new ArrayList<Geocell>();
		clonedList.addAll(cells);
		
		return clonedList;
		
	}

}
