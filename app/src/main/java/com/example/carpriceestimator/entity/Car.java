package com.example.carpriceestimator.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Car {
    @SerializedName("Count")
    @Expose
    private int count;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("SearchCriteria")
    @Expose
    private String searchCriteria;
    @SerializedName("Results")
    @Expose
    private List<CarResult> results = null;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public List<CarResult> getResults() {
        return results;
    }

    public void setResults(List<CarResult> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "Car{" +
                "count=" + count +
                ", message='" + message + '\'' +
                ", searchCriteria='" + searchCriteria + '\'' +
                ", results=" + results +
                '}';
    }
}
