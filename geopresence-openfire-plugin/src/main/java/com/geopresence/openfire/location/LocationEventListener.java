package com.geopresence.openfire.location;

import com.geopresence.xmpp.packet.GeoLoc;

public interface LocationEventListener {

  /**
   * A user's geopresence was updated.
   *
   * @param geopresence
   * @param params      event parameters.
   */
  public void locationUpdated(GeoLoc location);

}