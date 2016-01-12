package com.geopresence.geocell.event;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeocellEventDispatcher {

  private static final Logger Log = LoggerFactory.getLogger(GeocellEventDispatcher.class);

  private static List<GeocellEventListener> listeners =
      new CopyOnWriteArrayList<GeocellEventListener>();

  private GeocellEventDispatcher() {
    // Not instantiable.
  }

  /**
   * Registers a listener to receive events.
   *
   * @param listener the listener.
   */
  public static void addListener(GeocellEventListener listener) {
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
  public static void removeListener(GeocellEventListener listener) {
    listeners.remove(listener);
  }

  /**
   * Dispatches an event to all listeners.
   *
   * @param user      the user.
   * @param eventType the event type.
   * @param params    event parameters.
   */
  public static void dispatchEvent(EventType eventType, Map<String, Object> params) {
    for (GeocellEventListener listener : listeners) {
      try {
        switch (eventType) {
          case occupying_entity_added: {
            listener.occupyingEntityAdded(params);
            break;
          }
          case occupying_entity_vacated: {
            listener.occupyingEntityVacated(params);
            break;
          }
          case proximate_entity_added: {
            listener.proximateEntityAdded(params);
            break;
          }
          case proximate_entity_removed: {
            listener.proximateEntityRemoved(params);
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

    occupying_entity_added,
    occupying_entity_vacated,
    proximate_entity_added,
    proximate_entity_removed,

  }

}
