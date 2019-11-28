package ie.ul.studentmail.ronan.journeyentry;

public class SelectableJourney extends Journey{
    private boolean isSelected = false;


    public SelectableJourney(Journey journey,boolean isSelected) {
        super(journey.getName(),journey.getSteps(),journey.getStepDistance(), journey.getJourneyStart(), journey.getJourneyEnd(), journey.getStartLat(),
                journey.getStartLong(), journey.getEndLat(), journey.getEndLong());
        this.isSelected = isSelected;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}