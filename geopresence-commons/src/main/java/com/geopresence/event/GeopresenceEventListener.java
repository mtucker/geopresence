package com.geopresence.event;

import java.util.Map;

public interface GeopresenceEventListener {
	
    public void entityIsProximateTo(Map<String, Object> params);

    public void entityIsNoLongerProximateTo(Map<String, Object> params);

}
