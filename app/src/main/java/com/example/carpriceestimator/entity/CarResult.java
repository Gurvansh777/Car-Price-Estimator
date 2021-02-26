package com.example.carpriceestimator.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CarResult {
    @SerializedName("Value")
    @Expose
    private Object value;
    @SerializedName("ValueId")
    @Expose
    private String valueId;
    @SerializedName("Variable")
    @Expose
    private String variable;
    @SerializedName("VariableId")
    @Expose
    private int variableId;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public int getVariableId() {
        return variableId;
    }

    public void setVariableId(int variableId) {
        this.variableId = variableId;
    }

    @Override
    public String toString() {
        return "CarResult{" +
                "value=" + value +
                ", valueId='" + valueId + '\'' +
                ", variable='" + variable + '\'' +
                ", variableId=" + variableId +
                '}';
    }
}
