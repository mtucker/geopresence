package com.geopresence.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class GeopresenceEventDispatcher {

  private static final Logger Log = LoggerFactory.getLogger(GeopresenceEventDispatcher.class);

  private static List<GeopresenceEventListener> listeners =
      new CopyOnWriteArrayList<GeopresenceEventListener>();

  private GeopresenceEventDispatcher() {
    // Not instantiable.
  }

  /**
   * Registers a listener to receive events.
   *
   * @param listener the listener.
   */
  public static void addListener(GeopresenceEventListener listener) {
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
  public static void removeListener(GeopresenceEventListener listener) {
    listeners.remove(listener);
  }

  /**
   * Dispatches an event to all listeners.
   *
   * @param eventType the event type.
   * @param params    event parameters.
   */
  public static void dispatchEvent(EventType eventType, Map<String, Object> params) {
    for (GeopresenceEventListener listener : listeners) {
      try {
        switch (eventType) {
          case entity_is_proximate_to: {
            listener.entityIsProximateTo(params);
            break;
          }
          case entity_is_no_longer_proximate_to: {
            listener.entityIsNoLongerProximateTo(params);
            break;
          }
          default:
            break;
        }
      } catch (Exception e) {
        Log.error(e.getMessage(), e);
      }
    }
  }

  /**
   * Represents valid event types.
   */
  public enum EventType {

    entity_is_proximate_to,
    entity_is_no_longer_proximate_to

  }

}
