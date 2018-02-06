package com.harryfultz.studentmanager.models;

public class Model {

    private String sms;
    private String[] days;
    private String name, phoneNumber;
    private int value, gender;
    private String[][] absences;

    public Model(String name, String phoneNumber, int value, int g) {
        this.name = name;
        this.value = value;
        this.phoneNumber = phoneNumber;
        this.gender = g;

    }

    public String getSms() {
        return this.sms;
    }

    public void setSms(String value) {
        this.sms = value;
    }

    public String getName() {
        return this.name;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public int getGender() {
        return this.gender;
    }

    public String[] getDays() {
        return this.days;
    }

    public void setDays(int length) {
        days = new String[length];
    }

   public void setAbsences(int days, int hours){
       absences = new String[days][hours];
   }

    public String[][] getAbsences(){
        return this.absences;
    }

}
