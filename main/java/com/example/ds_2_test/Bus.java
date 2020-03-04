package com.example.ds_2_test;

import java.io.Serializable;

public class Bus implements Serializable {
    private static final long serialVersionUID = 3430010033531418612L;
    private String lineNumber;
    private String routerCode;
    private String vehicleId;
    private String lineName;
    private String buslineId;
    private String info;
    private String routeType;

    public  Bus(){}

    public Bus(String lineNumber, String routerCode, String vehicleId, String lineName, String buslineId, String info,String routeType){
        this.lineNumber = lineNumber;
        this.routerCode = routerCode;
        this.vehicleId = vehicleId;
        this.lineName = lineName;
        this.buslineId = buslineId;
        this.info = info;
        this.routeType = routeType;
    }

    public String getRouteType(){
        return this.routeType;
    }

    public void setRouteType(String routeType){
        this.routeType=routeType;
    }

    public String getLineNumber(){
        return this.lineNumber;
    }

    public void setLineNumber(String lineNumber){
        this.lineNumber=lineNumber;
    }

    public String getRouterCode(){
        return this.routerCode;
    }

    public void setRouterCode(String routerCode){
        this.routerCode=routerCode;
    }

    public String getVehicleId(){
        return this.vehicleId;
    }

    public void setVehicleId(String vehicleId){
        this.vehicleId=vehicleId;
    }

    public String getLineName(){
        return this.lineName;
    }

    public void setLineName(String lineName){
        this.lineName=lineName;
    }

    public String getBuslineId(){
        return this.buslineId;
    }

    public void setBuslineId(String buslineId){
        this.buslineId=buslineId;
    }

    public String getInfo(){
        return this.info;
    }

    public void setInfo(String info){
        this.info=info;
    }
    public String toString() {
        return  lineNumber +"-" +routerCode +"-" + vehicleId +"-" +  lineName +"-" + buslineId +"-" + info + "- " + routeType;
    }
}
