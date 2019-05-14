package com.snapdiet;

public class DataList {
    private long kalori;
    private long berat;

    public DataList(long kalori, long berat) {
        this.kalori = kalori;
        this.berat = berat;
    }

    public DataList() {
    }

    public long getKalori() {
        return kalori;
    }

    public void setKalori(long kalori) {
        this.kalori = kalori;
    }

    public long getBerat() {
        return berat;
    }

    public void setBerat(long berat) {
        this.berat = berat;
    }
}
