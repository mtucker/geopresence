package com.geopresence.model;

import java.awt.*;

import com.beoui.geocell.model.Point;

public class Entity {
	
	private String name;
	private Point location;
	private Double maxProximity;
	
	public Entity(String name, Point location, Double maxProximity){
		
		this.name = name;
		this.location = location;
		this.maxProximity = maxProximity;
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Point getLocation() {
		return location;
	}
	public void setLocation(Point location) {
		this.location = location;
	}

	public Double getMaxProximity() {
		return maxProximity;
	}
	public void setMaxProximity(Double maxProximity) {
		this.maxProximity = maxProximity;
	}

	public String toString(){
		return name;
	}

}
