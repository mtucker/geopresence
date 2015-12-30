package com.geopresence.client.test.util;

import java.util.ArrayList;
import java.util.Collection;

import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RosterEventListener implements RosterListener{
	
	private static final Logger log = LoggerFactory.getLogger(RosterEventListener.class);
	
	private String username;
	
	private Collection<String> entriesAdded = new ArrayList<String>();
	private Collection<String> entriesDeleted = new ArrayList<String>();
	private Collection<String> entriesUpdated = new ArrayList<String>();
	private Collection<String> presenceChanged = new ArrayList<String>();
	
	private Boolean updated = false;
	
	public RosterEventListener(String username){
		
		this.username = username;
		
	}
	
	public Boolean isUpdated() {
		
		return updated;
		
	}

	@Override
	public void entriesAdded(Collection<String> entries) {
		
		StringBuffer entryBuff = new StringBuffer("");
		
		for(String entry : entries){
			entryBuff.append(entry + ",");
		}
		log.info("Entries Added: " + entryBuff.toString());
		
		updated = true;
		entriesAdded.addAll(entries);
		
	}
	public Boolean entryAdded(String entry){
		
		return entriesAdded.contains(entry);
		
	}

	@Override
	public void entriesDeleted(Collection<String> entries) {
		
		StringBuffer entryBuff = new StringBuffer("");
		
		for(String entry : entries){
			entryBuff.append(entry + ",");
		}
		log.info("Entries Deleted: " + entryBuff.toString());
		
		updated = true;
		entriesDeleted.addAll(entries);
		
	}
	public Boolean entryDeleted(String entry){
		
		return entriesDeleted.contains(entry);
		
	}

	@Override
	public void entriesUpdated(Collection<String> entries) {
		
		StringBuffer entryBuff = new StringBuffer("");
		
		for(String entry : entries){
			entryBuff.append(entry + ",");
		}
		log.info("Entries Updated: " + entryBuff.toString());
		
		updated = true;
		entriesUpdated.addAll(entries);
		
	}
	public Boolean entryUpdated(String entry){
		
		return entriesUpdated.contains(entry);
		
	}

	@Override
	public void presenceChanged(Presence presence) {
		
		log.info("Presence Updated: " + presence.getFrom());
		
		updated = true;
		presenceChanged.add(presence.getFrom());
		
	}
	public Boolean presenceChanged(String entry){
		
		return presenceChanged.contains(entry);
		
	}
	
	public Boolean waitForRosterEvent(RosterEventType eventType, Long ms, String entry) throws Exception {
		
		Long start = System.currentTimeMillis();
		
		Boolean eventOccurred = false;
		
		while((System.currentTimeMillis() - start) < ms && !eventOccurred){
			Thread.sleep(2000);
			eventOccurred = eventOccurred(eventType, entry);
		}
		
		if(eventOccurred){
			removeEvent(eventType, entry);
		}
		
		return eventOccurred;
		
	}
	
	public Boolean eventOccurred(RosterEventType type, String entry){
		
		switch(type) {
		
			case ADDED: return entryAdded(entry);
			case DELETED: return entryDeleted(entry);
			case UPDATED: return entryUpdated(entry);
			case PRESENCE: return presenceChanged(entry);
		
		}
		
		return false;
		
	}
	
	public void removeEvent(RosterEventType type, String entry){
		
		switch(type) {
		
			case ADDED: entriesAdded.remove(entry);
			case DELETED: entriesDeleted.remove(entry);
			case UPDATED: entriesUpdated.remove(entry);
			case PRESENCE: presenceChanged.remove(entry);
		
		}
		
	}
	
}
