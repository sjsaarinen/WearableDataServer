package com.ohtu.wearable.wearabledataservice;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.ohtu.wearable.wearabledataservice.sensors.SensorsHandler;
import com.ohtu.wearable.wearabledataservice.server.FeedsController;
import com.ohtu.wearable.wearabledataservice.server.SensorHTTPServer;

import java.io.IOException;
import java.util.List;

/**
 * Run SensorHTTPServer as a bound foreground service
 */
public class SensorServerService extends Service {

    private boolean serverStarted = false;
    private boolean serverRunning = false;
    private boolean serviceStarted = false;
    private SensorHTTPServer server;
    private SensorsHandler sensorsHandler;

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        SensorServerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return SensorServerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //if service is not already started start it as a foreground service
        if (!serviceStarted) {
            if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {

                Notification notification = new Notification.Builder(this).build();
                //ToDo: custom icon for notification, when notification is clicked start app
                /*
                Intent notificationIntent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
                Notification notification = new Notification(R.drawable.common_signin_btn_icon_dark, "service running", System.currentTimeMillis());
                notification.setLatestEventInfo(this, "DataServer", "service started", pendingIntent);*/
                startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);
                serviceStarted = true;
            }
        }

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }



    /**
     * start the HTTP server if it's not running,
     * otherwise updates used sensors
     */
    public void startServer(List<Sensor> sensors){
        if (serverStarted && serverRunning){
            if (sensors != null) sensorsHandler.initSensors(sensors);
            Log.w("SERVER", "sensors updated");
        } else if (serverStarted && !serverRunning) {
            tryToStartServer();
        } else {
            sensorsHandler = new SensorsHandler(sensors, this);
            FeedsController feedsController = new FeedsController(sensorsHandler);
            server = new SensorHTTPServer(feedsController);
            tryToStartServer();
        }
    }

    /**
     * Stops the server if it is running
     */
    public void stopServer(){
        if (serverStarted) {
            server.stop();
            serverRunning = false;
            Toast.makeText(this, "Server stopped", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Is server running
     *
     * @return true if server running
     */
    public boolean isRunning(){
        return serverRunning;
    }

    @Override
    public void onDestroy (){
        //if service is destroyed stop server
        sensorsHandler.stopSensors();
        server.stop();
    }

    private void tryToStartServer(){
        try {
            server.start();
            serverStarted = true;
            serverRunning = true;
            //Shows "Server started" message on screen
            Toast.makeText(this, "Server started", Toast.LENGTH_SHORT).show();

        } catch (IOException ioe) {
            //Shows "Server failed to start" message on screen
            Toast.makeText(this, "Server failed to start", Toast.LENGTH_SHORT).show();
        }
    }



}
