package com.example.carpriceestimator.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PriceResult {
    @SerializedName("resultvalid")
    @Expose
    private int resultValid;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("price")
    @Expose
    private float price;

    public int getResultValid() {
        return resultValid;
    }

    public void setResultValid(int resultValid) {
        this.resultValid = resultValid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "PriceResult{" +
                "resultValid=" + resultValid +
                ", message='" + message + '\'' +
                ", price=" + price +
                '}';
    }
}
