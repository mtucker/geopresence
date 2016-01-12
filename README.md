# Geopresence

This is an experiment in creating a location-based buddy list for XMPP. Users can monitor within a geographic 
radius and as other users enter and exit that radius, their buddy list is updated so they can communicate with
users near them.

A potential use case for this type of technology is for Internet of Things applications. You could enter a room and be
notified of available services and be able to interact (send messages) to them. For example, perhaps you could control lighting or a/v equipment in a friend's home or interact with a display in a retail environment.

## Things to look at

[Integration Tests](https://github.com/mtucker/geopresence/tree/master/geopresence-integration-tests/src/test/java/com/geopresence/test/integration) demonstrate at a high level how the server is meant to work

[GeopresencePlugin](https://github.com/mtucker/geopresence/blob/master/geopresence-openfire-plugin/src/main/java/com/geopresence/openfire/GeopresencePlugin.java) is the entry point where the Openfire XMPP server initializes the plugin. 

[GeoCellGeopresenceManager](https://github.com/mtucker/geopresence/blob/master/geopresence-geocell/src/main/java/com/geopresence/geocell/GeocellGeopresenceManager.java) manages updates to the GeoCell grid.
