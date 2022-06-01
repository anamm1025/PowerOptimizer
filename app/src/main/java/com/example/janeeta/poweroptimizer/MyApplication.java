package com.example.janeeta.poweroptimizer;

import android.app.Application;

/**
 * Created by janeeta on 1/16/2016.
 */
public class MyApplication extends Application {

    private int selectedRoomId;
    private String selectedRoomName;
    private int noOfPersons;
    private int noOfDevices;
    private String selectedDevice;
    private int deviceId;
    private String username;
    private String password;
    private String checkSummary;//this will indicate whether the clicked check summary button is for the room or building

    public String getCheckSummary() {
        return checkSummary;
    }

    public void setCheckSummary(String checkSummary) {
        this.checkSummary = checkSummary;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pas) {
        this.password = pas;
    }


    public String getDeviceName() {
        return selectedDevice;
    }

    public void setDeviceName(String name) {
        this.selectedDevice = name;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int id) {
        this.deviceId = id;
    }

    public int getRoomId() {
        return this.selectedRoomId;
    }

    public void setRoomId(int id) {
        this.selectedRoomId = id;
    }

    public String getRoomName() {
        return this.selectedRoomName;
    }

    public void setRoomName(String name) {
        this.selectedRoomName = name;
    }

    public int getNoOfPersons() {
        return noOfPersons;
    }

    public void setNoOfPersons(int n) {
        this.noOfPersons = n;
    }

    public int getNoOfDevices() {
        return noOfDevices;
    }

    public void setNoOfDevices(int n) {
        this.noOfDevices = n;
    }

}