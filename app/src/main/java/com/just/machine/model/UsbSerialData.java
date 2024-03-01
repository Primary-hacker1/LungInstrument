package com.just.machine.model;

public class UsbSerialData {

    private String bloodPressure;
    private String ecg;
    private String bloodOxy;
    private int batteryLevel;

    private String bloodHigh;
    private String bloodLow;
    private String bloodState;

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getEcg() {
        return ecg;
    }

    public void setEcg(String ecg) {
        this.ecg = ecg;
    }

    public String getBloodOxy() {
        return bloodOxy;
    }

    public void setBloodOxy(String bloodOxy) {
        this.bloodOxy = bloodOxy;
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
}
