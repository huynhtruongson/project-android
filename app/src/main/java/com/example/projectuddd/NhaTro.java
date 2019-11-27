package com.example.projectuddd;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class NhaTro implements Serializable {
    private String giatien;
    private String tieude;
    private String diachi;
    private String quan;
    private String sodienthoai;
    private String dientich;
    private String mota;
    private String ten;
    private String user;
    private String hinh;
    private String check;
    private String check2;
    private String key;


    public NhaTro() {
    }

    public NhaTro(String giatien, String tieude, String diachi,String quan, String sodienthoai, String dientich, String mota, String ten, String user, String hinh, String check,String check2) {
        this.giatien = giatien;
        this.tieude = tieude;
        this.diachi = diachi;
        this.quan = quan;
        this.sodienthoai = sodienthoai;
        this.dientich = dientich;
        this.mota = mota;
        this.ten = ten;
        this.user = user;
        this.hinh = hinh;
        this.check = check;
        this.check2=check2;
    }

    public String getGiatien() {
        return giatien;
    }

    public void setGiatien(String giatien) {
        this.giatien = giatien;
    }

    public String getTieude() {
        return tieude;
    }

    public void setTieude(String tieude) {
        this.tieude = tieude;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getQuan() {
        return quan;
    }

    public void setQuan(String quan) {
        this.quan = quan;
    }

    public String getSodienthoai() {
        return sodienthoai;
    }

    public void setSodienthoai(String sodienthoai) {
        this.sodienthoai = sodienthoai;
    }

    public String getDientich() {
        return dientich;
    }

    public void setDientich(String dientich) {
        this.dientich = dientich;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getHinh() {
        return hinh;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getCheck2() {
        return check2;
    }

    public void setCheck2(String check2) {
        this.check2 = check2;
    }

    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}
