package com.example.janeeta.poweroptimizer;

/**
 * Created by janeeta on 1/24/2016.
 */
public class Device {

    private int deviceId;
    private String deviceName;
    private int roomId;
    private String status;
    private String timerStatus;
    private String isAutomation;
    private String isAttached;

    public String getIsAttached() {
        return isAttached;
    }

    public void setIsAttached(String isAttached) {
        this.isAttached = isAttached;
    }

    public String getIsAutomation() {
        return isAutomation;
    }

    public void setIsAutomation(String isAutomation) {
        this.isAutomation = isAutomation;
    }


    Device(int did,String nam,int rid,String statuss,String timer,String isAuto,String isAttach)
    {
        this.deviceId=did;
        this.deviceName=nam;
        this.roomId=rid;
        this.status=statuss;
        this.timerStatus=timer;
        this.isAutomation=isAuto;
        this.isAttached=isAttach;
    }

    public int getDeviceId(){return this.deviceId;}
    public String getDeviceName(){return this.deviceName;}
    public int getRoomId(){return this.getRoomId();}
    public String getStatus(){return this.status;}
    public String getTimerStatus(){return this.timerStatus;}

}
