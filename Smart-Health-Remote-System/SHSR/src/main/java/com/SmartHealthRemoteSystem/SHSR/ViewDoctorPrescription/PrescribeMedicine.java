package com.SmartHealthRemoteSystem.SHSR.ViewDoctorPrescription;

public class PrescribeMedicine {
    
    private String medName;
    private int quantity;
    private int prescribedQuantity;

    public PrescribeMedicine() {
    }

    public PrescribeMedicine(String medName, int quantity, int prescribedQuantity) {
        this.medName = medName;
        this.quantity = quantity;
        this.prescribedQuantity = prescribedQuantity;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrescribedQuantity() {
        return prescribedQuantity;
    }

    public void setPrescribedQuantity(int prescribedQuantity) {
        this.prescribedQuantity = prescribedQuantity;
    }
}