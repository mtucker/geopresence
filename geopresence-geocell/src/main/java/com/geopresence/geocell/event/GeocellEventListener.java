package com.geopresence.geocell.event;

import java.util.Map;

public interface GeocellEventListener {

  public void occupyingEntityAdded(Map<String, Object> params);

  public void occupyingEntityVacated(Map<String, Object> params);

  public void proximateEntityAdded(Map<String, Object> params);

  public void proximateEntityRemoved(Map<String, Object> params);

}
