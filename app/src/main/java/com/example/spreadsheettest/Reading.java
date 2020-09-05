package com.example.spreadsheettest;

public class Reading {
    private String Time;
    private String X_acc;
    private String Y_acc;
    private String Z_acc;

    public String getTime() {
        return Time;
    }

    public String getX_acc() {
        return X_acc;
    }

    public String getY_acc() {
        return Y_acc;
    }

    public String getZ_acc() {
        return Z_acc;
    }

    public Reading(String time, String x_acc, String y_acc, String z_acc){
        Time = time;
        X_acc = x_acc;
        Y_acc = y_acc;
        Z_acc = z_acc;


    }
}