package com.snapdiet;


import java.util.Date;

public class PointValue {

    Date xValue;
    int kalori, totalKalori, todayDate;
    float berat;
    String tanggal, idInput, namaMakanan;

    public PointValue(){

    }

    public PointValue(Date xValue, int kalori) {
        this.xValue = xValue;
        this.kalori = kalori;
    }

    public PointValue(Date xValue, int kalori, float berat) {
        this.xValue = xValue;
        this.kalori = kalori;
        this.berat = berat;
    }

    public PointValue(Date xValue, int kalori, float berat, String tanggal) {
        this.xValue = xValue;
        this.kalori = kalori;
        this.berat = berat;
        this.tanggal = tanggal;
    }

    public PointValue(Date xValue, int kalori, float berat, String tanggal, String idInput) {
        this.xValue = xValue;
        this.kalori = kalori;
        this.berat = berat;
        this.tanggal = tanggal;
        this.idInput = idInput;
    }

    public PointValue(Date xValue, int kalori, int totalKalori, float berat, String tanggal, String idInput) {
        this.xValue = xValue;
        this.kalori = kalori;
        this.totalKalori = totalKalori;
        this.berat = berat;
        this.tanggal = tanggal;
        this.idInput = idInput;
        this.todayDate = new Date().getDate();
    }

    public PointValue(Date xValue, int kalori, int totalKalori, float berat, String tanggal, String idInput, String namaMakanan) {
        this.xValue = xValue;
        this.kalori = kalori;
        this.totalKalori = totalKalori;
        this.todayDate = new Date().getDate();
        this.berat = berat;
        this.tanggal = tanggal;
        this.idInput = idInput;
        this.namaMakanan = namaMakanan;
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

    public Date getxValue() {
        return xValue;
    }

    public void setxValue(Date xValue) {
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

    public String getNamaMakanan() {
        return namaMakanan;
    }

    public void setNamaMakanan(String namaMakanan) {
        this.namaMakanan = namaMakanan;
    }
}
