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
    private int steps;
    private String distance;
    private String journeyStart;
    private String journeyEnd;

    @Ignore
    public Journey(){

    }
    public Journey(int steps, String distance, String journeyStart, String journeyEnd){
        this.distance = distance;
        this.steps = steps;
        this.journeyStart = journeyStart;
        this.journeyEnd = journeyEnd;
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

    public void setDistance(String distance){
        this.distance = distance;
    }

    public String getDistance() {
        return distance;
    }

    public void setJourneyStart(LocalDateTime journeyStartTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String journeyStart = journeyStartTime.format(formatter);
        this.journeyStart = journeyStart;
    }


    public String getJourneyStart() {
        return journeyStart;
    }



    public String toString(){

        String data = "Steps: " + steps + "\nDistance: " + distance + "\nStarted: " + journeyStart + "\nFinished: " + journeyEnd + "\n";

        return data;

    }

    public String getJourneyEnd() {
        return journeyEnd;
    }

    public void setJourneyEnd(String journeyEnd) {
        this.journeyEnd = journeyEnd;
    }
}
