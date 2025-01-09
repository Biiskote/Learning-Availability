package fr.isen.learning.interfaces.models;


public class locationModel {
    private String locationId;

    private String name;

    private String addressId;

    private int availability;

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }
    public String getLocationId() {
        return locationId;
    }
    public String getName() {
        return name;
    }
    public String getAddressId() {
        return addressId;
    }
    public int getAvailability() {
        return availability;
    }
}
