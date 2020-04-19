package com.example.itemlocator;

/**
 * This class is used for the creation of Product objects to simplify adding them to the database.
 * Includes setter and getters, very self explanatory.
 */
public class Product {

    private String UPC, Name, SideOfAisle, Longitude, Latitude;
    private int Aisle;

    public Product(String UPC, String Name, String SideOfAisle, int Aisle, String Long, String Lat) {

        this.UPC = UPC;
        this.Name = Name;
        this.SideOfAisle = SideOfAisle;
        this.Aisle = Aisle;
        this.Longitude = Long;
        this.Latitude = Lat;
    }

    public Product() {

    }

    public String getUPC() {
        return UPC;
    }

    public void setUPC(String UPC) {
        this.UPC = UPC;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSideOfAisle() {
        return SideOfAisle;
    }

    public void setSideOfAisle(String sideOfAisle) {
        SideOfAisle = sideOfAisle;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public int getAisle() {
        return Aisle;
    }

    public void setAisle(int aisle) {
        Aisle = aisle;
    }
}
