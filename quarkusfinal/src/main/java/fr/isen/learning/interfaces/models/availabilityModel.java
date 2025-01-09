package fr.isen.learning.interfaces.models;


public class availabilityModel {
    private String formationId;

    private int year;

    private char[] timeline;

    private String availabilityId;


     // Getter et Setter pour formationId
     public String getFormationId() {
        return formationId;
    }

    public void setFormationId(String formationId) {
        this.formationId = formationId;
    }

    // Getter et Setter pour year
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    // Getter et Setter pour timeline
    public char[] getTimeline() {
        return timeline;
    }

    public void setTimeline(char[] timeline) {
        this.timeline = timeline;
    }

    // Getter et Setter pour availabilityId
    public String getAvailabilityId() {
        return availabilityId;
    }

    public void setAvailabilityId(String availabilityId) {
        this.availabilityId = availabilityId;
    }
}


