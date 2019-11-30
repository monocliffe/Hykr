package ie.ul.studentmail.ronan.journeyentry;

class SelectableJourney extends Journey{
    private boolean isSelected;


    SelectableJourney(Journey journey, boolean isSelected) {
        super(journey.getName(),journey.getSteps(),journey.getStepDistance(), journey.getJourneyStart(), journey.getJourneyEnd(), journey.getStartLat(),
                journey.getStartLong(), journey.getEndLat(), journey.getEndLong());
        this.isSelected = isSelected;
    }


    boolean isSelected() {
        return isSelected;
    }

    void setSelected(boolean selected) {
        isSelected = selected;
    }
}