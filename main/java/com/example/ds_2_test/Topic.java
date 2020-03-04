package com.example.ds_2_test;



import java.io.Serializable;

public class Topic implements Serializable {
    private static final long serialVersionUID = 2182282996229745124L;
    private String busline;

    public Topic() {

    }

    public  Topic(String busline){
        this.busline = busline;
    }

    public String getBusline(){
        return this.busline;
    }

    public void setBusline(String busline){
        this.busline=busline;
    }

    public  String toString(){
        return busline ;
    }

}
