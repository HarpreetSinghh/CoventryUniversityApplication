package com.conventry.university.directions;

import java.util.ArrayList;

public class Dec {

    static public String distance;
    static public String duration;

    public double getCur_lat() {
        return cur_lat;
    }

    public void setCur_lat(double cur_lat) {

        this.cur_lat = cur_lat;
    }

    public double getCur_log() {
        return cur_log;
    }

    public void setCur_log(double cur_log) {
        this.cur_log = cur_log;
    }

    public double cur_lat;
    public double cur_log;
    static public double my_lat=17.500010;
    static public double my_long=75.998740;
    static public double item_lat=17.500000;
    static public double item_long=78.689890;
    static public ArrayList<String> html_instructions=new ArrayList<String>();
    static public double last_lat;
    static public double last_long;
    static public ArrayList<String> maneuver=new ArrayList<String>();
    static public ArrayList<String> dis=new ArrayList<String>();
    static public ArrayList<String> dur=new ArrayList<String>();
    static public ArrayList<Double> starting_lat=new ArrayList<Double>();
    static public ArrayList<Double> starting_long=new ArrayList<Double>();
    static public ArrayList<Double> ending_lat=new ArrayList<Double>();
    static public ArrayList<Double> ending_long=new ArrayList<Double>();

    public double getMy_lat() {
        return my_lat;
    }

    public void setMy_lat(double my_lat) {
        this.my_lat = my_lat;
    }


    public double getMy_long() {
        return my_long;
    }

    public void setMy_long(double my_long) {
        this.my_long = my_long;
    }


    public double getItem_lat() {
        return item_lat;
    }

    public void setItem_lat(double item_lat) {
        this.item_lat = item_lat;
    }


    public double getItem_long() {
        return item_long;
    }

    public void setItem_long(double item_long) {
        this.item_long = item_long;
    }



}