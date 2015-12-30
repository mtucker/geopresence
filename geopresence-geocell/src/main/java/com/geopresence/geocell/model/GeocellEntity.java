package com.geopresence.geocell.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import com.beoui.geocell.model.Point;
import com.geopresence.model.Entity;

public class GeocellEntity extends Entity {
	
	private List<Geocell> occupiedCells;
	private List<Geocell> proximateCells;
	
	public GeocellEntity(String name, Point location, Double maxProximity){
		
		super(name, location, maxProximity);
		
	}

	public List<Geocell> getOccupiedCells() {
		return occupiedCells;
	}
	public void setOccupiedCells(List<Geocell> occupiedCells) {
		this.occupiedCells = occupiedCells;
	}
	public void addOccupiedCell(Geocell cell){
		
		if(getOccupiedCells() == null){
			
			setOccupiedCells(new CopyOnWriteArrayList<Geocell>());
			
		}
		
		getOccupiedCells().add(cell);
		
		if(cell.getOccupyingEntities() != null && !cell.getOccupyingEntities().contains(this)){
			cell.addOccupyingEntity(this);
		}
		
	}	
	public void vacateOccupiedCell(Geocell cell){
		
		if(getOccupiedCells() != null){

			getOccupiedCells().remove(cell);
			
			if(cell.getOccupyingEntities() != null && cell.getOccupyingEntities().contains(this)){
				cell.vacateOccupyingEntity(this);
			}
			
		}
		
	}
	
	public List<Geocell> getProximateCells() {
		return proximateCells;
	}
	public void setProximateCells(List<Geocell> proximateCells) {
		this.proximateCells = proximateCells;
	}
	public void addProximateCell(Geocell cell){
		
		if(getProximateCells() == null){
			
			setProximateCells(new CopyOnWriteArrayList<Geocell>());
			
		}
		
		getProximateCells().add(cell);
		
		if(cell.getProximateEntities() != null && !cell.getProximateEntities().contains(this)){
			cell.addProximateEntity(this);
		}
		
	}
	public void removeProximateCell(Geocell cell){
		
		if(getProximateCells() != null){

			getProximateCells().remove(cell);
			
			if(cell.getProximateEntities() != null && cell.getProximateEntities().contains(this)){
				cell.removeProximateEntity(this);
			}
			
		}
		
	}
	public void removeProximateCells(List<Geocell> cells){
		
		for(Geocell cell : cells){
			removeProximateCell(cell);
		}
		
	}
	
	public Collection<GeocellEntity> getProximateEntities() {
		
		Set<GeocellEntity> proximateEntities = new HashSet<GeocellEntity>();
		
		if(getProximateCells() != null){
			
			for(Geocell cell : getProximateCells()){
				
				if(cell.getOccupyingEntities() != null){
					
					proximateEntities.addAll(cell.getOccupyingEntities());
				
				}
				
			}
			
		}
		
		proximateEntities.remove(this);		
		return proximateEntities;
		
	}

}
