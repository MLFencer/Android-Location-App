package net.nofool.dev.tbd;

//Data for all connected devices
public class Device {
    private String name, id, gcmID, location;
    private String[] apps = new String[5];
    private String[] times = new String[5];

    public Device(){}

    public String getName(){
        return name;
    }

    public String getID(){
        return id;
    }

    public String getGcmID() {
        return gcmID;
    }

    public String getLocation() {
        return location;
    }

    public String[] getApps() {
        return apps;
    }

    public void addLocation(String loc){
        location = loc;
    }

    public void addApp(String app, String time){
        if (apps[0]==(null)){
            apps[0] = app;
            times[0]= time;
        }else if (apps[1]==(null)){
            apps[1]=app;
            times[1]= time;
        }else if (apps[2]==(null)){
            apps[2]=app;
            times[2]= time;
        }else if (apps[3]==(null)){
            apps[3]=app;
            times[3]= time;
        }else if (apps[4]==(null)){
            apps[4]=app;
            times[4]= time;
        }else {
            apps[0] = apps[1];
            times[0]= times[1];
            apps[1] = apps[2];
            times[1]= times[2];
            apps[2] = apps[3];
            times[2]= times[3];
            apps[3] = apps[4];
            times[3]= times[4];
            apps[4] = app;
            times[4]= time;
        }
    }

    public String toString(){
        return "Device Name: "+name+", Device ID: "+id+", Device GCM ID: "+gcmID+", Last 5 active apps and time they were activated: "+
                apps[0]+", "+times[0]+", "+apps[1]+", "+times[1]+", "+apps[2]+", "+times[2]+", "+apps[3]+", "+times[3]+", "+
                apps[4]+", "+times[4]+";";
    }
}