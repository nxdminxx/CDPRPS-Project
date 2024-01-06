package com.SmartHealthRemoteSystem.SHSR.Medicine;

public class Medicine {
    private String MedName;
    private String MedId;
    private int Quantity;

    public Medicine(){
    }

    public Medicine(String MedName){
        this.MedName = MedName;
    }

    public Medicine(String MedName, int Quantity){
        this.MedName = MedName;
        this.Quantity = Quantity;
    }

    public Medicine(String MedName, String MedId, int Quantity){
        this.MedName = MedName;
        this.MedId = MedId;
        this.Quantity = Quantity;
    }

    public void setMedName(String MedName){
        this.MedName = MedName;
    }

    public String getMedName(){
        return MedName;
    }

    public void setMedId(String MedId){
        this.MedId = MedId;
    }

    public String getMedId(){
        return MedId;
    }

    public void setQuantity(int Quantity){
        this.Quantity = Quantity;
    }

    public int getQuantity(){
        return Quantity;
    }
}
