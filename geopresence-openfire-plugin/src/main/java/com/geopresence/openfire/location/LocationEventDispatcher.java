package com.geopresence.openfire.location;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jivesoftware.openfire.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.geopresence.xmpp.packet.GeoLoc;


public class LocationEventDispatcher {

	private static final Logger Log = LoggerFactory.getLogger(LocationEventDispatcher.class);

    private static List<LocationEventListener> listeners =
            new CopyOnWriteArrayList<LocationEventListener>();

    private LocationEventDispatcher() {
        // Not instantiable.
    }

    /**
     * Registers a listener to receive events.
     *
     * @param listener the listener.
     */
    public static void addListener(LocationEventListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        listeners.add(listener);
    }

    /**
     * Unregisters a listener to receive events.
     *
     * @param listener the listener.
     */
    public static void removeListener(LocationEventListener listener) {
        listeners.remove(listener);
    }

    /**
     * Dispatches an event to all listeners.
     *
     * @param user the user.
     * @param eventType the event type.
     * @param params event parameters.
     */
    public static void dispatchEvent(GeoLoc location, EventType eventType) {
        for (LocationEventListener listener : listeners) {
            try {
                switch (eventType) {
                    case location_updated: {
                        listener.locationUpdated(location);
                        break;
                    }
                    default:
                        break;
                }
            }
            catch (Exception e) {
                Log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * Represents valid event types.
     */
    public enum EventType {

        /**
         * A user's location has been updated.
         */
        location_updated,
        
    }
}