package com.example.carpriceestimator.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "car")
public class DecodedCar {
    @NonNull
    @PrimaryKey()
    private String vin = "";
    private String make;
    private String manufactureName;
    private String model;
    private String modelYear;
    private String bodyClass;
    private int doors;
    private float price = 0.0F;
    private String userEmailAddress = "";


    @Ignore
    public DecodedCar(String vin, String make, String manufactureName, String model, String modelYear, String bodyClass, int doors, float price, String userEmailAddress) {
        this.userEmailAddress = userEmailAddress;
        this.vin = vin;
        this.make = make;
        this.manufactureName = manufactureName;
        this.model = model;
        this.modelYear = modelYear;
        this.bodyClass = bodyClass;
        this.doors = doors;
        this.price = price;
    }

    public DecodedCar() {
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getManufactureName() {
        return manufactureName;
    }

    public void setManufactureName(String manufactureName) {
        this.manufactureName = manufactureName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModelYear() {
        return modelYear;
    }

    public void setModelYear(String modelYear) {
        this.modelYear = modelYear;
    }

    public String getBodyClass() {
        return bodyClass;
    }

    public void setBodyClass(String bodyClass) {
        this.bodyClass = bodyClass;
    }

    public int getDoors() {
        return doors;
    }

    public void setDoors(int doors) {
        this.doors = doors;
    }

    public String getUserEmailAddress() {
        return userEmailAddress;
    }

    public void setUserEmailAddress(String userEmailAddress) {
        this.userEmailAddress = userEmailAddress;
    }

    @Override
    public String toString() {
        return "DecodedCar{" +
                "vin='" + vin + '\'' +
                ", make='" + make + '\'' +
                ", manufactureName='" + manufactureName + '\'' +
                ", model='" + model + '\'' +
                ", modelYear='" + modelYear + '\'' +
                ", bodyClass='" + bodyClass + '\'' +
                ", doors=" + doors +
                ", userEmailAddress=" + userEmailAddress +
                '}';
    }
}

