package com.conventry.university.service;


import android.location.Location;

import com.conventry.university.beans.Weather;
import com.conventry.university.utils.AppConstants;
import com.conventry.university.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherService {

    private static String BASE_URL ="http://dataservice.accuweather.com/";

    private static String API_KEY = "P3wAupalPr0fMIKZAUDMPPHCxD0Zzche";

    public static Weather getLocationKey(Location latLng) {
        HttpURLConnection urlConnection = null;
        Weather weather = null;
        try {
            // create connection
            URL urlToRequest = new URL(BASE_URL + "locations/v1/cities/geoposition/search?apikey=" + API_KEY +
                "&q="+ latLng.getLatitude() + "," + latLng.getLongitude());
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String response = convertInputStreamToString(in);
                JSONObject jsonObject = null;
                if(!AppUtils.isBlank(response)) {
                    jsonObject = new JSONObject(response);
                }
                if(jsonObject != null) {
                    weather = new Weather();
                    weather.setCity(jsonObject.get("EnglishName").toString()+ ", " + jsonObject.getJSONObject("Country").get("EnglishName"));
                    return getCurrentWeatherData(Integer.parseInt(jsonObject.get("Key").toString()), weather);
                }
            }
        } catch (Exception e) {
            AppUtils.doLogError("ERROR", e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }


    public static Weather getCurrentWeatherData(int locationKey, Weather weather) {
        HttpURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL(BASE_URL + "/currentconditions/v1/" + locationKey + ".json?language=en&apikey=" + API_KEY + "&details=true&metric=true");
            urlConnection = (HttpURLConnection)
                    urlToRequest.openConnection();
            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(
                        urlConnection.getInputStream());
                JSONArray jsonArray = new JSONArray(convertInputStreamToString(in));
                weather.setCurrentJsonObject(jsonArray);
                return getWeatherData(locationKey, weather);
            }
        } catch (Exception e) {
            AppUtils.doLogError("ERROR", e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    public static Weather getWeatherData(int locationKey, Weather weather) {
        HttpURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL(BASE_URL + "/forecasts/v1/daily/5day/" + locationKey + "?apikey=" + API_KEY + "&details=true&metric=true");
            urlConnection = (HttpURLConnection)
                    urlToRequest.openConnection();
            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(
                        urlConnection.getInputStream());
                JSONObject jsonObject = new JSONObject(convertInputStreamToString(in));
                weather.setJsonObject(jsonObject);
                return weather;
            }
        } catch (Exception e) {
            AppUtils.doLogError("ERROR", e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = AppConstants.BLANK;
        String result = AppConstants.BLANK;
        while((line = bufferedReader.readLine()) != null) {
            result += line;
        }
        return result;
    }
}
