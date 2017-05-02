package com.conventry.university.beans;


import org.json.JSONArray;
import org.json.JSONObject;

public class Weather {

    private String city;

    private JSONArray currentJsonObject;

    private JSONObject jsonObject;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JSONArray getCurrentJsonObject() {
        return currentJsonObject;
    }

    public void setCurrentJsonObject(JSONArray currentJsonObject) {
        this.currentJsonObject = currentJsonObject;
    }
}
