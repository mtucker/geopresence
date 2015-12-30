package com.geopresence.geocell.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.geopresence.geocell.event.GeocellEventDispatcher;

public class Geocell {
	
	private String name;
	private List<GeocellEntity> occupyingEntities;
	private List<GeocellEntity> proximateEntities;
	
	public Geocell(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public List<GeocellEntity> getOccupyingEntities() {
		return occupyingEntities;
	}
	public void setOccupyingEntities(List<GeocellEntity> occupyingEntities) {
		this.occupyingEntities = occupyingEntities;
	}
	public void addOccupyingEntity(GeocellEntity entity){
		
		if(getOccupyingEntities() == null){
			
			setOccupyingEntities(new ArrayList<GeocellEntity>());
			
		}
		
		getOccupyingEntities().add(entity);
		
		if(entity.getOccupiedCells() == null || !entity.getOccupiedCells().contains(this)){
			entity.addOccupiedCell(this);
		}
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("entityName", entity.getName());
		params.put("cellName", getName());
		GeocellEventDispatcher.dispatchEvent(GeocellEventDispatcher.EventType.occupying_entity_added, params);
		
	}
	public void vacateOccupyingEntity(GeocellEntity entity){
		
		if(getOccupyingEntities() != null){

			getOccupyingEntities().remove(entity);
			
			if(entity.getOccupiedCells() != null && entity.getOccupiedCells().contains(this)){
				entity.vacateOccupiedCell(this);
			}
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("entityName", entity.getName());
			params.put("cellName", getName());
			GeocellEventDispatcher.dispatchEvent(GeocellEventDispatcher.EventType.occupying_entity_vacated, params);
			
		}
		
	}
	
	public List<GeocellEntity> getProximateEntities() {
		return proximateEntities;
	}
	public void setProximateEntities(List<GeocellEntity> proximateEntities) {
		this.proximateEntities = proximateEntities;
	}
	public void addProximateEntity(GeocellEntity entity){
		
		if(getProximateEntities() == null){
			
			setProximateEntities(new ArrayList<GeocellEntity>());
			
		}
		
		getProximateEntities().add(entity);
		
		if(entity.getProximateCells() == null || !entity.getProximateCells().contains(this)){
			entity.addProximateCell(this);
		}
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("entityName", entity.getName());
		params.put("cellName", getName());
		GeocellEventDispatcher.dispatchEvent(GeocellEventDispatcher.EventType.proximate_entity_added, params);
		
	}
	public void removeProximateEntity(GeocellEntity entity){
		
		if(getProximateEntities() != null){

			getProximateEntities().remove(entity);
			
			if(entity.getProximateCells() != null && entity.getProximateCells().contains(this)){
				entity.removeProximateCell(this);
			}
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("entityName", entity.getName());
			params.put("cellName", getName());
			GeocellEventDispatcher.dispatchEvent(GeocellEventDispatcher.EventType.proximate_entity_removed, params);
			
		}
		
	}
	
	public String toString(){
		return name;
	}

}
