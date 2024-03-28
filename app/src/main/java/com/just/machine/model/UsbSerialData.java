package com.just.machine.model;

public class UsbSerialData {

    private String bloodPressureState;
    private String ecgState;
    private String bloodOxyState;
    private int batteryLevel;

    private String bloodHigh;
    private String bloodLow;
    private String bloodHighFront;
    private String bloodLowFront;
    private String bloodHighBehind;
    private String bloodLowBehind;
    private String bloodState;

    private String bloodOxygen;
    private String circleCount;

    public String getBloodPressureState() {
        return bloodPressureState;
    }

    public void setBloodPressureState(String bloodPressure) {
        this.bloodPressureState = bloodPressure;
    }

    public String getEcgState() {
        return ecgState;
    }

    public void setEcgState(String ecgState) {
        this.ecgState = ecgState;
    }

    public String getBloodOxyState() {
        return bloodOxyState;
    }

    public void setBloodOxyState(String bloodOxyState) {
        this.bloodOxyState = bloodOxyState;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public String getBloodLow() {
        return bloodLow;
    }

    public void setBloodLow(String bloodLow) {
        this.bloodLow = bloodLow;
    }

    public String getBloodHigh() {
        return bloodHigh;
    }

    public void setBloodHigh(String bloodHigh) {
        this.bloodHigh = bloodHigh;
    }

    public String getBloodState() {
        return bloodState;
    }

    public void setBloodState(String bloodState) {
        this.bloodState = bloodState;
    }

    public String getBloodHighFront() {
        return bloodHighFront;
    }

    public void setBloodHighFront(String bloodHighFront) {
        this.bloodHighFront = bloodHighFront;
    }

    public String getBloodLowFront() {
        return bloodLowFront;
    }

    public void setBloodLowFront(String bloodLowFront) {
        this.bloodLowFront = bloodLowFront;
    }

    public String getBloodHighBehind() {
        return bloodHighBehind;
    }

    public void setBloodHighBehind(String bloodHighBehind) {
        this.bloodHighBehind = bloodHighBehind;
    }

    public String getBloodLowBehind() {
        return bloodLowBehind;
    }

    public void setBloodLowBehind(String bloodLowBehind) {
        this.bloodLowBehind = bloodLowBehind;
    }

    public String getBloodOxygen() {
        return bloodOxygen;
    }

    public void setBloodOxygen(String bloodOxygen) {
        this.bloodOxygen = bloodOxygen;
    }

    public String getCircleCount() {
        return circleCount;
    }

    public void setCircleCount(String circleCount) {
        this.circleCount = circleCount;
    }
}
