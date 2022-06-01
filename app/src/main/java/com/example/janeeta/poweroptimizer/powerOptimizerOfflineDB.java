package com.example.janeeta.poweroptimizer;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Anam Sadiq on 10-01-2016.
 */

public  class  powerOptimizerOfflineDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "powerOptimizerOfflineDatabase";
    private static final int DATABASE_VERSION = 7;

    public static final String TABLE_NAME_ROOM = "Room";
    public static final String COLUMN_OF_ROOM_TABLE_ROOMID = "RoomID";
    public static final String COLUMN_OF_ROOM_TABLE_ROOMNAME = "RoomName";
    public static final String COLUMN_OF_ROOM_TABLE_NOOFDEVICES = "NoOfDevices";
    public static final String COLUMN_OF_ROOM_TABLE_NOOFPERSONS = "NoOfPersons";


    public static final String TABLE_NAME_APPLIANCE = "Appliance";
    public static final String COLUMN_OF_APPLIANCE_TABLE_APPLIANCEID = "ApplianceID";
    public static final String COLUMN_OF_APPLIANCE_TABLE_APPLIANCENAME = "Name";
    public static final String COLUMN_OF_APPLIANCE_TABLE_STATUS = "Status";
    public static final String COLUMN_OF_APPLIANCE_TABLE_TIMERSTATUS = "TimerStatus";
    public static final String COLUMN_OF_APPLIANCE_TABLE_ROOMID = "RoomID";
    public static final String COLUMN_OF_APPLIANCE_TABLE_AUTOMATION = "Automation";
    public static final String COLUMN_OF_APPLIANCE_TABLE_ATTACHED = "Attached";


    public static final String TABLE_NAME_TIMER = "Timer";
    public static final String COLUMN_OF_TIMER_TABLE_TIMERID = "TimerID";
    public static final String COLUMN_OF_TIMER_TABLE_APPLIANCEID = "ApplianceID";
    public static final String COLUMN_OF_TIMER_TABLE_DATE = "Date";
    public static final String COLUMN_OF_TIMER_TABLE_TIME = "Time";
    public static final String COLUMN_OF_TIMER_TABLE_TARGETACTION = "TargetAction";


    public powerOptimizerOfflineDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("CREATE TABLE " + TABLE_NAME_ROOM + "(" +
                        COLUMN_OF_ROOM_TABLE_ROOMID + " INTEGER PRIMARY KEY, " +
                        COLUMN_OF_ROOM_TABLE_ROOMNAME + " TEXT, " +
                        COLUMN_OF_ROOM_TABLE_NOOFDEVICES + " INTEGER, " +
                        COLUMN_OF_ROOM_TABLE_NOOFPERSONS + " INTEGER);"
        );


        db.execSQL("CREATE TABLE " + TABLE_NAME_APPLIANCE + "(" +
                        COLUMN_OF_APPLIANCE_TABLE_APPLIANCEID + " INTEGER PRIMARY KEY, " +
                        COLUMN_OF_APPLIANCE_TABLE_APPLIANCENAME + " TEXT, " +
                        COLUMN_OF_APPLIANCE_TABLE_STATUS + " TEXT, " +
                        COLUMN_OF_APPLIANCE_TABLE_TIMERSTATUS + " TEXT, " +
                        COLUMN_OF_APPLIANCE_TABLE_ROOMID + " INTEGER, " +
                        COLUMN_OF_APPLIANCE_TABLE_AUTOMATION + " TEXT, " +
                        COLUMN_OF_APPLIANCE_TABLE_ATTACHED + " TEXT " + ");"

        );


        db.execSQL("CREATE TABLE " + TABLE_NAME_TIMER + "(" +
                        COLUMN_OF_TIMER_TABLE_TIMERID + " INTEGER PRIMARY KEY, " +
                        COLUMN_OF_TIMER_TABLE_APPLIANCEID + " INTEGER, " +
                        COLUMN_OF_TIMER_TABLE_TIME + " TEXT, " +
                        COLUMN_OF_TIMER_TABLE_DATE + " TEXT, " +
                        COLUMN_OF_TIMER_TABLE_TARGETACTION + " TEXT);"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ROOM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_APPLIANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TIMER);
        onCreate(db);
    }

    public void emptyAllTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME_ROOM);
        db.execSQL("DELETE FROM " + TABLE_NAME_APPLIANCE);
        db.execSQL("DELETE FROM " + TABLE_NAME_TIMER);
    }

    public Cursor getAllRooms() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME_ROOM, null);
        return res;
    }

    public Cursor getAllAppliances() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME_APPLIANCE, null);
        return res;
    }

    public Cursor getAllTimers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME_TIMER, null);
        return res;
    }


    public boolean insertRowInRoomTable(int id, String name, int noOfAppliances, int noOfPersons) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_OF_ROOM_TABLE_ROOMID, id);
        contentValues.put(COLUMN_OF_ROOM_TABLE_ROOMNAME, name);
        contentValues.put(COLUMN_OF_ROOM_TABLE_NOOFDEVICES, noOfAppliances);
        contentValues.put(COLUMN_OF_ROOM_TABLE_NOOFPERSONS, noOfPersons);

        db.insert(TABLE_NAME_ROOM, null, contentValues);
        return true;
    }


    public boolean insertRowInApplianceTable(int id, String name, String status, String timerStatus, int roomId, String Auto, String Attach) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_OF_APPLIANCE_TABLE_APPLIANCEID, id);
        contentValues.put(COLUMN_OF_APPLIANCE_TABLE_APPLIANCENAME, name);
        contentValues.put(COLUMN_OF_APPLIANCE_TABLE_STATUS, status);
        contentValues.put(COLUMN_OF_APPLIANCE_TABLE_TIMERSTATUS, timerStatus);
        contentValues.put(COLUMN_OF_APPLIANCE_TABLE_ROOMID, roomId);
        contentValues.put(COLUMN_OF_APPLIANCE_TABLE_AUTOMATION, Auto);
        contentValues.put(COLUMN_OF_APPLIANCE_TABLE_ATTACHED, Attach);

        db.insert(TABLE_NAME_APPLIANCE, null, contentValues);
        return true;
    }


    public boolean insertRowInTimerTable(int id, int applianceId, String date, String time, String targetAction) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_OF_TIMER_TABLE_TIMERID, id);
        contentValues.put(COLUMN_OF_TIMER_TABLE_APPLIANCEID, applianceId);
        contentValues.put(COLUMN_OF_TIMER_TABLE_DATE, date);
        contentValues.put(COLUMN_OF_TIMER_TABLE_TIME, time);
        contentValues.put(COLUMN_OF_TIMER_TABLE_TARGETACTION, targetAction);

        db.insert(TABLE_NAME_TIMER, null, contentValues);
        return true;
    }

    public Room getSpecificRoom(int ID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_ROOM + " where RoomID=" + ID + "", null);
        //Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_ROOM + " WHERE _id=" + ID, null );

        cursor.moveToFirst();

        int index = cursor.getColumnIndex(COLUMN_OF_ROOM_TABLE_ROOMID);
        int id = cursor.getInt(index);
        int index1 = cursor.getColumnIndex(COLUMN_OF_ROOM_TABLE_ROOMNAME);
        String name = cursor.getString(index1);
        int index2 = cursor.getColumnIndex(COLUMN_OF_ROOM_TABLE_NOOFDEVICES);
        int noOfDevice = Integer.parseInt(cursor.getString(index2));
        int index3 = cursor.getColumnIndex(COLUMN_OF_ROOM_TABLE_NOOFPERSONS);
        int noOfPerson = Integer.parseInt(cursor.getString(index3));

        Room room = new Room(id, name, noOfDevice, noOfPerson);
        return room;

    }

    public Cursor getSpecificTimerInfo(int deviceID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_TIMER + " where " + COLUMN_OF_TIMER_TABLE_APPLIANCEID + "=" + deviceID + "", null);
        cursor.moveToFirst();
        return cursor;
    }

    public ArrayList<Device> getAllDevicesOfARoom(int roomId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_APPLIANCE + " where " + COLUMN_OF_APPLIANCE_TABLE_ROOMID + "=" + roomId + "", null);
        ArrayList<Device> deviceList = new ArrayList<Device>();
        while (cursor.moveToNext()) {

            int index = cursor.getColumnIndex(COLUMN_OF_APPLIANCE_TABLE_APPLIANCEID);
            int did = cursor.getInt(index);
            int index1 = cursor.getColumnIndex(COLUMN_OF_APPLIANCE_TABLE_APPLIANCENAME);
            String name = cursor.getString(index1);
            int index2 = cursor.getColumnIndex(COLUMN_OF_APPLIANCE_TABLE_STATUS);
            String status = cursor.getString(index2);
            int index3 = cursor.getColumnIndex(COLUMN_OF_APPLIANCE_TABLE_TIMERSTATUS);
            String timer = cursor.getString(index3);
            int index4 = cursor.getColumnIndex(COLUMN_OF_APPLIANCE_TABLE_ROOMID);
            int rid = cursor.getInt(index4);
            int index5 = cursor.getColumnIndex(COLUMN_OF_APPLIANCE_TABLE_AUTOMATION);
            String auto = cursor.getString(index5);
            int index6 = cursor.getColumnIndex(COLUMN_OF_APPLIANCE_TABLE_ATTACHED);
            String attach = cursor.getString(index6);

            Device dev = new Device(did, name, rid, status, timer, auto, attach);
            deviceList.add(dev);
        }

        return deviceList;
    }

    public Device getSpecificDevice(int ID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_APPLIANCE + " where " + COLUMN_OF_TIMER_TABLE_APPLIANCEID + "=" + ID + "", null);
        Device dev;
        cursor.moveToFirst();

        int index = cursor.getColumnIndex(COLUMN_OF_APPLIANCE_TABLE_APPLIANCEID);
        int did = cursor.getInt(index);
        int index1 = cursor.getColumnIndex(COLUMN_OF_APPLIANCE_TABLE_APPLIANCENAME);
        String name = cursor.getString(index1);
        int index2 = cursor.getColumnIndex(COLUMN_OF_APPLIANCE_TABLE_STATUS);
        String status = cursor.getString(index2);
        int index3 = cursor.getColumnIndex(COLUMN_OF_APPLIANCE_TABLE_TIMERSTATUS);
        String timer = cursor.getString(index3);
        int index4 = cursor.getColumnIndex(COLUMN_OF_APPLIANCE_TABLE_ROOMID);
        int rid = cursor.getInt(index4);
        int index5 = cursor.getColumnIndex(COLUMN_OF_APPLIANCE_TABLE_AUTOMATION);
        String auto = cursor.getString(index5);
        int index6 = cursor.getColumnIndex(COLUMN_OF_APPLIANCE_TABLE_ATTACHED);
        String attach = cursor.getString(index6);

        dev = new Device(did, name, rid, status, timer, auto, attach);

        //dev=new Device(did,name,rid,status,timer);
        return dev;

    }

    public ArrayList<Device> getAllDevices() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_APPLIANCE, null);
        ArrayList<Device> deviceList = new ArrayList<Device>();
        while (cursor.moveToNext()) {

            int index = cursor.getColumnIndex(COLUMN_OF_APPLIANCE_TABLE_APPLIANCEID);
            int did = cursor.getInt(index);
            int index1 = cursor.getColumnIndex(COLUMN_OF_APPLIANCE_TABLE_APPLIANCENAME);
            String name = cursor.getString(index1);
            int index2 = cursor.getColumnIndex(COLUMN_OF_APPLIANCE_TABLE_STATUS);
            String status = cursor.getString(index2);
            int index3 = cursor.getColumnIndex(COLUMN_OF_APPLIANCE_TABLE_TIMERSTATUS);
            String timer = cursor.getString(index3);
            int index4 = cursor.getColumnIndex(COLUMN_OF_APPLIANCE_TABLE_ROOMID);
            int rid = cursor.getInt(index4);
            int index5 = cursor.getColumnIndex(COLUMN_OF_APPLIANCE_TABLE_AUTOMATION);
            String auto = cursor.getString(index5);
            int index6 = cursor.getColumnIndex(COLUMN_OF_APPLIANCE_TABLE_ATTACHED);
            String attach = cursor.getString(index6);

            Device dev = new Device(did, name, rid, status, timer, auto, attach);
            deviceList.add(dev);
        }

        return deviceList;
    }

    public boolean isDeviceAttached(int ID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_APPLIANCE + " where " + COLUMN_OF_TIMER_TABLE_APPLIANCEID + "=" + ID + "", null);
        Device dev;
        cursor.moveToFirst();

        int index6 = cursor.getColumnIndex(COLUMN_OF_APPLIANCE_TABLE_ATTACHED);
        String attach = cursor.getString(index6);

        if (attach.contains("YES") || attach.contains("yes"))
            return true;
        else
            return false;
    }

    public void changeDeviceStatus(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_APPLIANCE + " SET " + COLUMN_OF_APPLIANCE_TABLE_STATUS + "= '" + status + "' WHERE " + COLUMN_OF_APPLIANCE_TABLE_APPLIANCEID + "=" + id;
        db.execSQL(query);

    }


    public void changeAutomationStatus(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_APPLIANCE + " SET " + COLUMN_OF_APPLIANCE_TABLE_AUTOMATION + "= '" + status + "' WHERE " + COLUMN_OF_APPLIANCE_TABLE_APPLIANCEID + "=" + id;
        db.execSQL(query);

    }


    public void changeAttachmentStatus(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_APPLIANCE + " SET " + COLUMN_OF_APPLIANCE_TABLE_ATTACHED + "= '" + status + "' WHERE " + COLUMN_OF_APPLIANCE_TABLE_APPLIANCEID + "=" + id;
        db.execSQL(query);

        if (status == "NO") {
            query = "UPDATE " + TABLE_NAME_APPLIANCE + " SET " + COLUMN_OF_APPLIANCE_TABLE_AUTOMATION + "= 'OFF'," + COLUMN_OF_APPLIANCE_TABLE_TIMERSTATUS + "= 'OFF'  WHERE " + COLUMN_OF_APPLIANCE_TABLE_APPLIANCEID + "=" + id;
            db.execSQL(query);

        }

    }


    public void turnOnAutomationForAllAppliacnes() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_APPLIANCE + " SET " + COLUMN_OF_APPLIANCE_TABLE_AUTOMATION + "= 'ON' WHERE " + COLUMN_OF_APPLIANCE_TABLE_ATTACHED + "='YES'";
        db.execSQL(query);

    }

    public void turnOffAutomationForAllAppliacnes() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_APPLIANCE + " SET " + COLUMN_OF_APPLIANCE_TABLE_AUTOMATION + "= 'OFF' WHERE " + COLUMN_OF_APPLIANCE_TABLE_ATTACHED + "='YES'";
        db.execSQL(query);

    }

    public void turnOnAutomationOfRoom(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_APPLIANCE + " SET " + COLUMN_OF_APPLIANCE_TABLE_AUTOMATION + "= 'ON' WHERE " + COLUMN_OF_APPLIANCE_TABLE_ATTACHED + "='YES' AND " + COLUMN_OF_APPLIANCE_TABLE_ROOMID + "=" + id;
        db.execSQL(query);

    }

    public void turnOffAutomationOfRoom(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_APPLIANCE + " SET " + COLUMN_OF_APPLIANCE_TABLE_AUTOMATION + "= 'OFF' WHERE " + COLUMN_OF_APPLIANCE_TABLE_ATTACHED + "='YES' AND " + COLUMN_OF_APPLIANCE_TABLE_ROOMID + "=" + id;
        db.execSQL(query);

    }

    public void updateTimer(int deviceID, String target, String time, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_TIMER + " SET " + COLUMN_OF_TIMER_TABLE_TARGETACTION + "= '" + target + "'," + COLUMN_OF_TIMER_TABLE_TIME + "= '" + time + "'," + COLUMN_OF_TIMER_TABLE_DATE + "= '" + date + "' WHERE " + COLUMN_OF_TIMER_TABLE_APPLIANCEID + "=" + deviceID;
        db.execSQL(query);

    }


    public void changeCount(int count, int id)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_ROOM + " SET " + COLUMN_OF_ROOM_TABLE_NOOFPERSONS + "= " + count + " WHERE " + COLUMN_OF_ROOM_TABLE_ROOMID + "=" + id;
        db.execSQL(query);

    }

    public void addTimer(int deviceID, String target, String time, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_OF_TIMER_TABLE_APPLIANCEID, deviceID);
        contentValues.put(COLUMN_OF_TIMER_TABLE_TARGETACTION, target);
        contentValues.put(COLUMN_OF_TIMER_TABLE_TIME, time);
        contentValues.put(COLUMN_OF_TIMER_TABLE_DATE, date);

        db.insert(TABLE_NAME_TIMER, null, contentValues);

        String query = "UPDATE " + TABLE_NAME_APPLIANCE + " SET " + COLUMN_OF_APPLIANCE_TABLE_TIMERSTATUS + "= 'ON' WHERE " + COLUMN_OF_APPLIANCE_TABLE_APPLIANCEID + "=" + deviceID;
        db.execSQL(query);


    }

    public void deleteTimer(int deviceID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_APPLIANCE + " SET " + COLUMN_OF_APPLIANCE_TABLE_TIMERSTATUS + "= 'OFF' WHERE " + COLUMN_OF_APPLIANCE_TABLE_APPLIANCEID + "=" + deviceID;
        db.execSQL(query);
        query = "DELETE FROM " + TABLE_NAME_TIMER+ " WHERE " + COLUMN_OF_TIMER_TABLE_APPLIANCEID + " = "+deviceID;
        db.execSQL(query);


    }


}
