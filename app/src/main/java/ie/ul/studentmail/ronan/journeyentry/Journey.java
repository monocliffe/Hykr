package ie.ul.studentmail.ronan.journeyentry;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Entity(tableName = "journeys")
public class Journey {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private int steps;
    private String stepDistance;
    private String journeyStart;
    private String journeyEnd;
    private String startLat;
    private String startLong;
    private String endLat;
    private String endLong;
    private double distance;

    @Ignore
    public Journey(){


    }
    public Journey(String name, int steps, String stepDistance, String journeyStart, String journeyEnd, String startLat, String startLong, String endLat, String endLong){
        this.name = name;
        this.stepDistance = stepDistance;
        this.steps = steps;
        this.journeyStart = journeyStart;
        this.journeyEnd = journeyEnd;
        this.startLat = startLat;
        this.startLong = startLong;
        this.endLat = endLat;
        this.endLong = endLong;
        distance = convertDistance(startLat,startLong,endLat,endLong);

    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setSteps(int steps){
        this.steps=steps;
    }

    public int getSteps(){
        return steps;
    }

    public void setStepDistance(String stepDistance){
        this.stepDistance = stepDistance;
    }

    public String getStepDistance() {
        return stepDistance;
    }

    public void setJourneyStart(LocalDateTime journeyStartTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String journeyStart = journeyStartTime.format(formatter);
        this.journeyStart = journeyStart;
    }


    public String getJourneyStart() {
        return journeyStart;
    }


    public String getJourneyEnd() {
        return journeyEnd;
    }

    public void setJourneyEnd(String journeyEnd) {
        this.journeyEnd = journeyEnd;
    }

    public String getStartLat() {
        return startLat;
    }

    public void setStartLat(String startLat) {
        this.startLat = startLat;
    }

    public String getStartLong() {
        return startLong;
    }

    public void setStartLong(String startLong) {
        this.startLong = startLong;
    }

    public String getEndLat() {
        return endLat;
    }

    public void setEndLat(String endLat) {
        this.endLat = endLat;
    }

    public String getEndLong() {
        return endLong;
    }

    public void setEndLong(String endLong) {
        this.endLong = endLong;
    }

    public double getDistance(){
        return distance;
    }
    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public double convertDistance(String lat1Str, String lng1Str, String lat2Str, String lng2Str){
        double lat1, lat2, lng1, lng2;

        lat1=Double.parseDouble(lat1Str);
        lng1=Double.parseDouble(lng1Str);
        lat2=Double.parseDouble(lat2Str);
        lng2=Double.parseDouble(lng2Str);

        if ((lat1 == lat2) && (lng1 == lng2)) {
            return 0;
        }
        double theta = lng1 - lng2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);

        dist = dist * 1.609344;
        System.out.println("dist: " + dist);
        return (dist);
    }

    public String toString(){

        return name+","+steps+","+stepDistance+","+journeyStart+","+journeyEnd+","+
                      startLat+","+startLong+","+endLat+","+endLong+","+String.format("%.2f km", distance);

    }


}
