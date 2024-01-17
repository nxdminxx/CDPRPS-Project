package com.SmartHealthRemoteSystem.SHSR.ViewDoctorPrescription;

import com.SmartHealthRemoteSystem.SHSR.Medicine.Medicine;

public class PrescribeMedicine {
    
    private String medName;
    private String patientId;
    private int prescribedQuantity;

    public PrescribeMedicine() {
    }

    public PrescribeMedicine(String medName, String patientId, int prescribedQuantity) {
        this.medName = medName;
        this.patientId = patientId;
        this.prescribedQuantity = prescribedQuantity;
    }

    public PrescribeMedicine(String medName, int prescribedQuantity) {
        this.medName = medName;
        this.prescribedQuantity = prescribedQuantity;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public int getPrescribedQuantity() {
        return prescribedQuantity;
    }

    public void setPrescribedQuantity(int prescribedQuantity) {
        this.prescribedQuantity = prescribedQuantity;
    }
}