package ie.ul.studentmail.ronan.journeyentry;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.util.Locale;


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
        distance = calcDistance(Double.valueOf(startLat),
                                Double.valueOf(startLong),
                                Double.valueOf(endLat),
                                Double.valueOf(endLong));

    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    int getSteps(){
        return steps;
    }

    String getStepDistance() {
        return stepDistance;
    }

    String getJourneyStart() {
        return journeyStart;
    }

    String getJourneyEnd() {
        return journeyEnd;
    }

    String getStartLat() {
        return startLat;
    }

    String getStartLong() {
        return startLong;
    }

    String getEndLat() {
        return endLat;
    }

    String getEndLong() {
        return endLong;
    }

    double getDistance(){
        return distance;
    }

    void setDistance(double distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //Distance between two points on the globe is calculated using the Haversine formula.
    private static double calcDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; //Radius of planet earth!!
        double latDistance = toRad(lat2-lat1);
        double lonDistance = toRad(lon2-lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R*c;
    }

    private static Double toRad(Double value) {
        return value * Math.PI / 180;
    }

    @NonNull
    public String toString(){

        return name+","+steps+","+stepDistance+","+journeyStart+","+journeyEnd+","+
                      startLat+","+startLong+","+endLat+","+endLong+","+String.format(Locale.UK,"%.2f km", distance) + "," + id;

    }
}
