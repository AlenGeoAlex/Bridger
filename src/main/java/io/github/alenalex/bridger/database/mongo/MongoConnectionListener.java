package io.github.alenalex.bridger.database.mongo;

import com.mongodb.event.ServerHeartbeatFailedEvent;
import com.mongodb.event.ServerHeartbeatStartedEvent;
import com.mongodb.event.ServerHeartbeatSucceededEvent;
import com.mongodb.event.ServerMonitorListener;

public class MongoConnectionListener implements ServerMonitorListener {

    private boolean connectionOpen;

    public MongoConnectionListener() {
        this.connectionOpen = false;
    }

    @Override
    public void serverHearbeatStarted(ServerHeartbeatStartedEvent event) {
        connectionOpen = false;
    }

    @Override
    public void serverHeartbeatSucceeded(ServerHeartbeatSucceededEvent event) {
        connectionOpen = true;
    }

    @Override
    public void serverHeartbeatFailed(ServerHeartbeatFailedEvent event) {
        connectionOpen = false;
    }

    public boolean isConnectionOpen() {
        return connectionOpen;
    }
}
