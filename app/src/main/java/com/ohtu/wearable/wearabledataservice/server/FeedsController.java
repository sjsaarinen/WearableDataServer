package com.ohtu.wearable.wearabledataservice.server;

import android.util.Log;

import com.ohtu.wearable.wearabledataservice.sensors.SensorsHandler;

import org.json.JSONException;

import java.util.regex.Pattern;

/**
 * Returns device sensors data as JSON in NanoHTTPD.Response based on uri
 */
public class FeedsController {

    private SensorsHandler sensorsHandler;

    //regex matchers for routing by uri
    private static String LIST = "\\/?";
    private static String SENSOR_DATA = "\\/\\d*";
    private static String SENSOR_DB = "\\/\\d*\\/";

    public FeedsController(SensorsHandler sensorsHandler){

        this.sensorsHandler = sensorsHandler;
    }

    /**
     * Returns NanoHTTPD.Response containing sensor data as JSON
     * If uri is feeds or feeds/ returns list of all available sensors
     * If uri is feeds/[sensorId] returns data from that sensor if sensor is available
     * if sensor is not availabe returns NOT FOUND response
     *
     * @param uri, URI
     * @param method, HTTP method
     * @return
     */
    public NanoHTTPD.Response getResponse(String uri, String method) {

        int sensor;

        if (uri.matches(LIST) && method.equals("GET")){
            Log.d("FeedsController", "Matches LIST");
            return listResponse();
        } else if (uri.matches(SENSOR_DATA) && method.equals("GET")){
            Log.d("FeedsController", "Matches SENSOR_DATA");
            try {
                sensor = Integer.parseInt(uri.substring(1));
                Log.d("FeedsController", "" + sensor);
                if (sensorsHandler.sensorIsActive(sensor)){
                    return sensorDataResponse(sensor);
                }
                return notFoundResponse();
            } catch (NumberFormatException e){
                return notFoundResponse();
            }
        } else if (uri.matches(SENSOR_DB) && method.equals("GET")){
            Log.d("FeedsController", "Matches SENSOR_DB");
            try {
                Log.d("FeedsController", "parsed uri " + uri.substring(1, uri.length() - 1));
                sensor = Integer.parseInt(uri.substring(1, uri.length() - 1));
                Log.d("FeedsController", "" + sensor);
                return sensorDataResponseFromDb(sensor);
            } catch (NumberFormatException e){
                return notFoundResponse();
            }
        } else {
            Log.d("FeedsController", "no match");
            return notFoundResponse();
        }

        //try to parse sensor number from feeds/[sensor] if not integer catch exception
        /*
        if (uri.length() > 1){
            try {
                sensor = Integer.parseInt(uri.substring(1));
            } catch (NumberFormatException e){
            }
        }*/



        //if uri is / or empty return list of sensor
        //else if list of sensors contain sensor number parsed from uri return sensor data
        //otherwise return not found
        /*
        if ((uri.equalsIgnoreCase("/") || uri.isEmpty()) && method.equals("GET")) {
            return listResponse();
        } else if ( sensorsHandler.sensorIsActive(sensor) && uri.equalsIgnoreCase("/" + sensor) && method.equals("GET")){
            return sensorDataResponse(sensor);
        } else if (method.equals("POST")) {
            return new NanoHTTPD.Response(NanoHTTPD.Response.Status.METHOD_NOT_ALLOWED, NanoHTTPD.MIME_PLAINTEXT, "Not implemented");
        } else if ( sensorsHandler.sensorIsActive(sensor) && uri.equalsIgnoreCase("/" + sensor + "/")) {
            return sensorDataResponseFromDb(sensor);
        } else {
            return notFoundResponse();
        }*/

    }

    private NanoHTTPD.Response listResponse(){
        try {
            return new NanoHTTPD.Response(sensorsHandler.getSensorsList().toString());
        } catch (JSONException e) {
            return new NanoHTTPD.Response(NanoHTTPD.Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, "Error");
        }
    }

    private NanoHTTPD.Response sensorDataResponse(int sensor){
        try {
            return new NanoHTTPD.Response(sensorsHandler.getSensorData(sensor).toString());
        } catch (JSONException e) {
            return errorResponse(e.toString());
        }
    }

    private NanoHTTPD.Response sensorDataResponseFromDb(int sensor){
        try {
            return new NanoHTTPD.Response(sensorsHandler.getAllSensorDataFromDb(sensor).toString());
        } catch (JSONException e) {
            return errorResponse(e.toString());
        }
    }

    //Returns NOT FOUND HTTP response
    private NanoHTTPD.Response notFoundResponse(){
        return new NanoHTTPD.Response(NanoHTTPD.Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "Not Found");
    }

    private NanoHTTPD.Response errorResponse(String error){
        return new NanoHTTPD.Response(NanoHTTPD.Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, error);
    }

}