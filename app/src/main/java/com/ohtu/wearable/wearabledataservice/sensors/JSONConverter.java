package com.ohtu.wearable.wearabledataservice.sensors;

import android.hardware.Sensor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.String.valueOf;

import java.util.List;

/**
 * Created by jannetim on 10/02/15.
 */
public class JSONConverter {

    /**
     * Converts input float[] to JSONObject
     * @param data
     * @return
     * @throws JSONException
     */
    public static JSONObject convertToJSON(float[] data) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        if (data==null) {
            return jsonObject;
        } else {
            char prefix = 'x';
            for (int i = 0; i < data.length; i++) {
                jsonObject.put(valueOf(prefix), data[i]);
                if (prefix == 'z') {
                    prefix = 'a';
                } else {
                    prefix++;
                }
            }
            //Log.d("JSON", jsonObject.toString());
            return jsonObject;
        }
    }

    /**
     * Converts list of sensor objects to readable JSON format
     * @param sensorList List of Sensor-objects
     * @return JSONObject containing information of all if any sensors given in the list
     * @throws JSONException
     */
    public static JSONObject convertSensorListToJSON(List<Sensor> sensorList) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        for (Sensor s : sensorList) {
            String prefix = s.getName();
            jsonObject.put(valueOf(prefix), s.getType());
        }
        return jsonObject;
    }
}