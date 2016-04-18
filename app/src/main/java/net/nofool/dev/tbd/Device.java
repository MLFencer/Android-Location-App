package net.nofool.dev.tbd;

//Data for all connected devices
public class Device {
    private String name, id, gcmID;
    private double lon, lat;


    public Device(String name, String gcmID){
        this.name = name;
        this.gcmID = gcmID;
    }

    public String getName(){
        return name;
    }

    public String getID(){
        return id;
    }

    public String getGcmID() {
        return gcmID;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double longitude){
        lon = longitude;
    }

    public double getLat() {
        return lat;
    }
    public void setLat(double latitude){
        lat = latitude;
    }
}