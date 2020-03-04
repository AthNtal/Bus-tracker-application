package com.example.ds_2_test;



import java.io.Serializable;

public class Value implements Serializable {
    private static final long serialVersionUID = 8547445315842127428L;
    Bus bus ;
    private double latitute;
    private double longitude;

    public Value (){}

    public Value(Bus bus,double latitute,double longitude){
        this.bus = bus;
        this.longitude = longitude;
        this.latitute = latitute;
    }

    public Double getLatitute(){
        return this.latitute;
    }

    public void setLatitute(Double latitute){
        this.latitute=latitute;
    }
    public Double getLongitude(){
        return this.longitude;
    }

    public void setLongitude(Double longitude){
        this.longitude=longitude;
    }

    public  String toString(){
        return bus.toString() + " - "  + latitute + " - " + longitude;
    }

}
