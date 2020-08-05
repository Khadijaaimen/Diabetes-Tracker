package com.example.diabetestracker.javaClass;

public class Medicine {
    int id, dosage;
    String date,time,unit,email, medication;
    //set id
    public void setId(int Id){this.id=Id;}
    //get id
    public int getId(){return id;}
    //set medication
    public void setMedication(String med){this.medication=med;}
    //get medication
    public String getMedication(){return medication;}
    //set dosage
    public void setDosage(int dose){this.dosage=dose;}
    //get dosage
    public int getDosage(){return dosage;}
    //set date
    public void setDate(String date){this.date=date;}
    //get date
    public String getDate(){return date;}
    //set time
    public void setTime(String time){this.time=time;}
    //get time
    public String getTime(){return time;}
    //set unit
    public void setUnit(String unit){this.unit=unit;}
    //get unit
    public String getUnit(){return unit;}
    //set email
    public void setEmail(String emaill){this.email=emaill;}
    //get email
    public String getEmail(){return email;}

}