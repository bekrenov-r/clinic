package com.bekrenov.clinic.integrationtests.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class JSONUtil {
    public static void setProperty(JSONObject json, String key, String value) {
        setProperty(json, key, (Object) value);
    }

    public static void setProperty(JSONObject json, String key, JSONObject value){
        setProperty(json, key, (Object) value);
    }

    public static void setProperty(JSONObject json, String key, Object value){
        try {
            boolean isNestedProperty = key.contains(".") && !json.has(key);
            if(isNestedProperty){
                String[] keys = key.split("\\.");
                setNestedProperty(json, keys, value);
            } else {
                json.put(key, value);
            }
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean isJsonArray(String str){
        try {
            new JSONArray(str);
            return true;
        } catch(JSONException ex){
            return false;
        }
    }

    private static JSONObject setNestedProperty(JSONObject json, String[] keys, Object value) throws JSONException {
        if(keys.length == 1){
            json.put(keys[0], value);
            return json;
        } else {
            return json.put(
                    keys[0],
                    setNestedProperty(json.getJSONObject(keys[0]), Arrays.copyOfRange(keys, 1, keys.length), value)
            );
        }
    }
}
