package com.geopresence.openfire.location;

import org.dom4j.Element;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.IQ;
import org.xmpp.packet.Packet;

import com.geopresence.xmpp.packet.GeoLoc;

/**
 * A sample plugin for Openfire.
 */
public class LocationPacketInterceptor implements PacketInterceptor {

	private static final Logger log = LoggerFactory.getLogger(LocationPacketInterceptor.class);

	private static final String NAMESPACE_GEOLOC = "http://jabber.org/protocol/geoloc";

	public LocationPacketInterceptor() {
		
	}
	
	public void interceptPacket(Packet packet, Session session,
			boolean incoming, boolean processed) throws PacketRejectedException {

		if (incoming && !processed && packet instanceof IQ) {

			IQ iq = (IQ) packet;

			Element childElement = iq.getChildElement();
			if (childElement == null) {
				return;
			}

			String namespace = childElement.getNamespaceURI();

			if (namespace.equals(NAMESPACE_GEOLOC)) {

				if (iq.getType().equals(IQ.Type.set)) {

					log.info("Intercepted Location Update...");
					
					try {
						
						Double lat = Double
								.valueOf(childElement.element("lat").getText());
						Double lon = Double
								.valueOf(childElement.element("lon").getText());
						Double maxProximity = Double
								.valueOf(childElement.element("maxProximity").getText());
						
						GeoLoc geoLoc = new GeoLoc();
						geoLoc.setFrom(iq.getFrom().toFullJID());
						geoLoc.setLat(lat);
						geoLoc.setLon(lon);
						geoLoc.setMaxProximity(maxProximity);
						
						LocationEventDispatcher.dispatchEvent(geoLoc,
								LocationEventDispatcher.EventType.location_updated);

					} catch (Exception e) {
						log.error("An error occurred updating location of user:");
						e.printStackTrace();
					}

				}

			}

		}

	}
}
