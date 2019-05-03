package com.example.lying.weightrecord;

public class Weight {
    private String Date = "";
    private String SpecificDate = "";
    private String SpecificTime = "";
    private String Name = "";
    private String Weight = "";

    public Weight(String Date,String SpecificDate,String SpecificTime,String Name,String Weight){
        this.Date = Date;
        this.SpecificDate = SpecificDate;
        this.SpecificTime = SpecificTime;
        this.Name = Name;
        this.Weight = Weight;
    }

    public String getDate() {
        return Date;
    }

    public String getName() {
        return Name;
    }

    public String getSpecificDate() {
        return SpecificDate;
    }

    public String getSpecificTime() {
        return SpecificTime;
    }

    public String getWeight() {
        return Weight;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setSpecificDate(String specificDate) {
        SpecificDate = specificDate;
    }

    public void setSpecificTime(String specificTime) {
        SpecificTime = specificTime;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }
}
