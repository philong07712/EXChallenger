package com.example.exchallenger.utils;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by PC0353 on 7/21/2016.
 */
public class JSONUtilParser {

    public String getString(String name, JSONObject obj) {
        try {
            return obj.getString(name);
        } catch (Exception ex) {
            return "";
        }
    }

    public JSONObject getJSONObject(String name, JSONObject obj) {
        try {
            return obj.getJSONObject(name);
        } catch (Exception ex) {
            return null;
        }
    }

    public JSONArray getJSONArray(String name, JSONObject obj) {
        try {
            return obj.getJSONArray(name);
        } catch (Exception ex) {
            return null;
        }
    }

    public JSONObject getJSONObject(String data) {
        try {
            return new JSONObject(data);
        } catch (Exception ex) {
            return null;
        }
    }

    public Object getObject(String name, JSONObject obj) {
        try {
            return obj.get(name);
        } catch (Exception ex) {
            return null;
        }
    }
}
