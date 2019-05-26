package com.snapdiet;


import java.util.Date;

public class PointValue {

    long xValue;
    int kalori, totalKalori, todayDate;
    float berat;
    String tanggal, idInput;

    public PointValue(){

    }

    public PointValue(long xValue, int kalori) {
        this.xValue = xValue;
        this.kalori = kalori;
    }

    public PointValue(long xValue, int kalori, float berat) {
        this.xValue = xValue;
        this.kalori = kalori;
        this.berat = berat;
    }

    public PointValue(long xValue, int kalori, float berat, String tanggal) {
        this.xValue = xValue;
        this.kalori = kalori;
        this.berat = berat;
        this.tanggal = tanggal;
    }

    public PointValue(long xValue, int kalori, float berat, String tanggal, String idInput) {
        this.xValue = xValue;
        this.kalori = kalori;
        this.berat = berat;
        this.tanggal = tanggal;
        this.idInput = idInput;
    }

    public PointValue(long xValue, int kalori, int totalKalori, float berat, String tanggal, String idInput) {
        this.xValue = xValue;
        this.kalori = kalori;
        this.totalKalori = totalKalori;
        this.berat = berat;
        this.tanggal = tanggal;
        this.idInput = idInput;
        this.todayDate = new Date().getDate();
    }

    public String getIdInput() {
        return idInput;
    }

    public void setIdInput(String idInput) {
        this.idInput = idInput;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public float getBerat() {
        return berat;
    }

    public void setBerat(float berat) {
        this.berat = berat;
    }

    public long getxValue() {
        return xValue;
    }

    public void setxValue(long xValue) {
        this.xValue = xValue;
    }

    public int getKalori() {
        return kalori;
    }

    public void setKalori(int kalori) {
        this.kalori = kalori;
    }

    public int getTotalKalori() {
        return totalKalori;
    }

    public void setTotalKalori(int totalKalori) {
        this.totalKalori = totalKalori;
    }

    public int getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(int todayDate) {
        this.todayDate = todayDate;
    }
}
