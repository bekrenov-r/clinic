package com.bekrenov.clinic.integrationtests.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class TestUtil {

    public static String prepareRequestBody(String resourcePath, Map<String, String> properties) throws JSONException {
        JSONObject json = new JSONObject(getResourceAsString(resourcePath));
        properties.forEach((key, value) -> JSONUtil.setProperty(json, key, value));
        return json.toString();
    }

    public static String prepareRequestBody(String resourcePath, String property, String value) throws JSONException {
        JSONObject json = new JSONObject(getResourceAsString(resourcePath));
        JSONUtil.setProperty(json, property, value);
        return json.toString();
    }

    public static String prepareRequestBody(String resourcePath, String property, JSONObject value) throws JSONException {
        JSONObject json = new JSONObject(getResourceAsString(resourcePath));
        JSONUtil.setProperty(json, property, value);
        return json.toString();
    }

    public static String prepareRequestBody(String resourcePath, String property, Object value) throws JSONException {
        JSONObject json = new JSONObject(getResourceAsString(resourcePath));
        JSONUtil.setProperty(json, property, value);
        return json.toString();
    }

    public static String getResourceAsString(String path) {
        try(InputStream is = TestUtil.class.getResourceAsStream(path)){
            return new String(is.readAllBytes());
        } catch(IOException ex){
            throw new RuntimeException(ex);
        }
    }
}
