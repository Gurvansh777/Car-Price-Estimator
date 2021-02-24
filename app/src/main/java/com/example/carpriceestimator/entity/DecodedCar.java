package com.example.carpriceestimator.entity;

public class DecodedCar {
    private String vin;
    private String make;
    private String manufactureName;
    private String model;
    private String modelYear;
    private String bodyClass;
    private int doors;

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
                '}';
    }
}

