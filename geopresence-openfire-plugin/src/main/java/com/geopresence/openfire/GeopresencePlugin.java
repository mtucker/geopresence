package com.geopresence.openfire;

import java.io.File;
import java.util.Map;

import com.geopresence.GeopresenceManager;
import com.geopresence.event.GeopresenceEventDispatcher;
import com.geopresence.event.GeopresenceEventListener;
import com.geopresence.geocell.GeocellGeopresenceManager;
import com.geopresence.openfire.location.LocationEventListener;
import com.geopresence.xmpp.packet.GeoLoc;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.event.SessionEventDispatcher;
import org.jivesoftware.openfire.event.SessionEventListener;
import org.jivesoftware.openfire.event.UserEventDispatcher;
import org.jivesoftware.openfire.event.UserEventListener;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.roster.Roster;
import org.jivesoftware.openfire.roster.RosterManager;
import org.jivesoftware.openfire.session.Session;
import org.jivesoftware.openfire.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.geopresence.openfire.location.LocationPacketInterceptor;
import org.xmpp.packet.JID;

public class GeopresencePlugin implements Plugin, LocationEventListener, SessionEventListener, UserEventListener, GeopresenceEventListener {

	private static final Logger Log = LoggerFactory
			.getLogger(GeopresencePlugin.class);
	
	private LocationPacketInterceptor locationPacketInterceptor;

    private XMPPServer xmppServer;
	private InterceptorManager interceptorManager;
    private RosterManager rosterManager;
	private GeopresenceManager geopresenceManager;

	public GeopresencePlugin() {
		
		locationPacketInterceptor = new LocationPacketInterceptor();
		
		interceptorManager = InterceptorManager.getInstance();
		geopresenceManager = GeocellGeopresenceManager.getInstance();

        xmppServer = XMPPServer.getInstance();

        if(xmppServer != null){
            rosterManager = xmppServer.getRosterManager();
        }
		
	}

    public RosterManager getRosterManager() {

        return this.rosterManager;

    }

	public void initializePlugin(PluginManager manager, File pluginDirectory) {

		Log.info("GeopresencePlugin Initialized...");
		interceptorManager.addInterceptor(locationPacketInterceptor);
        GeopresenceEventDispatcher.addListener(this);
        UserEventDispatcher.addListener(this);
        SessionEventDispatcher.addListener(this);

	}

	public void destroyPlugin() {

		Log.info("GeopresencePlugin Being Destroyed...");
		interceptorManager.removeInterceptor(locationPacketInterceptor);
        GeopresenceEventDispatcher.removeListener(this);
        UserEventDispatcher.removeListener(this);
        SessionEventDispatcher.removeListener(this);

	}

    @Override
    public void locationUpdated(GeoLoc location) {

        String username = location.getFrom().split("@")[0];
        geopresenceManager.updateEntity(username, location.getLat(), location.getLon(), location.getMaxProximity());

    }

    @Override
    public void entityIsProximateTo(Map<String, Object> params) {

        String subjectEntity = (String) params.get("subjectEntity");
        String proximateEntity = (String) params.get("proximateEntity");

        JID proximateEntityJID = new JID(proximateEntity);

        try{

            Log.info("Adding " + proximateEntity + " to " + subjectEntity + "'s Roster");

            Roster roster = getRosterManager().getRoster(subjectEntity);
            roster.createRosterItem(proximateEntityJID, true, false);

        } catch (Exception e) {
            Log.error("An exception occurred adding '" + proximateEntity + "' to the roster of '" + subjectEntity + "'");
            e.printStackTrace();
        }
    }

    @Override
    public void entityIsNoLongerProximateTo(Map<String, Object> params) {

        String subjectEntity = (String) params.get("subjectEntity");
        String noLongerProximateEntity = (String) params.get("noLongerProximateEntity");

        JID proximateEntityJID = new JID(noLongerProximateEntity);

        try{

            Log.info("Removing " + noLongerProximateEntity + " from " + subjectEntity + "'s Roster");

            Roster roster = getRosterManager().getRoster(subjectEntity);
            roster.deleteRosterItem(proximateEntityJID, true);

        } catch (Exception e) {
            Log.error("An exception occurred removing '" + noLongerProximateEntity + "' from the roster of '" + subjectEntity + "'");
            e.printStackTrace();
        }
    }

    @Override
    public void sessionCreated(Session session) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sessionDestroyed(Session session) {
        String username = session.getAddress().toBareJID().split("@")[0];;
        geopresenceManager.removeEntity(username);
    }

    @Override
    public void anonymousSessionCreated(Session session) {
        // TODO Auto-generated method stub

    }

    @Override
    public void anonymousSessionDestroyed(Session session) {
        // TODO Auto-generated method stub

    }

    @Override
    public void resourceBound(Session session) {
        // TODO Auto-generated method stub

    }

    @Override
    public void userCreated(User user, Map<String, Object> stringObjectMap) {
    }

    @Override
    public void userDeleting(User user, Map<String, Object> stringObjectMap) {
        String username = user.getUsername();
        geopresenceManager.removeEntity(username);
    }

    @Override
    public void userModified(User user, Map<String, Object> stringObjectMap) {
    }
}
