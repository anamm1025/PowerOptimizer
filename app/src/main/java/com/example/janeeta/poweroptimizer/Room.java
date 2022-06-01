package com.example.janeeta.poweroptimizer;

/**
 * Created by janeeta on 1/24/2016.
 */
public class Room {
    private int id;
    private String name;
    private int noOfdevices;
    private int noOfPersons;


    Room(int ID,String n,int dev,int person)
    {
        this.id=ID;
        this.name=n;
        this.noOfdevices=dev;
        this.noOfPersons=person;
    }

    public int getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public int getNoOfdevices()
    {
        return this.noOfdevices;
    }

    public int getNoOfPersons()
    {return this.noOfPersons;}

}
