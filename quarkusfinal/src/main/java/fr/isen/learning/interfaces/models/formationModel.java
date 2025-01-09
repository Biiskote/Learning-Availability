package fr.isen.learning.interfaces.models;

import java.util.Date;
import fr.isen.learning.interfaces.models.enums.FORMATIONSTATE;

public class formationModel {
    private String formationId;

    private String name;

    private int durationinHours;

    private Date startDate;

    private Date endDate;

    private String locationId;

    private FORMATIONSTATE state;



 public String getFormationId() {
        return formationId;
    }

    public void setFormationId(String formationId) {
        this.formationId = formationId;
    }

    // Getter et Setter pour name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter et Setter pour durationinHours
    public int getDurationinHours() {
        return durationinHours;
    }

    public void setDurationinHours(int durationinHours) {
        this.durationinHours = durationinHours;
    }

    // Getter et Setter pour startDate
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    // Getter et Setter pour endDate
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    // Getter et Setter pour locationId
    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    // Getter et Setter pour state
    public FORMATIONSTATE getState() {
        return state;
    }

    public void setState(FORMATIONSTATE state) {
        this.state=state;
    }


}
