package com.SmartHealthRemoteSystem.SHSR.Service;

import com.SmartHealthRemoteSystem.SHSR.Repository.SHSRDAO;
import com.SmartHealthRemoteSystem.SHSR.Repository.SubCollectionSHSRDAO;
import com.SmartHealthRemoteSystem.SHSR.ViewDoctorPrescription.Prescription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class PrescriptionService {
    private final SubCollectionSHSRDAO<Prescription> prescriptionRepository;
    // private final SHSRDAO<Prescription> prescriptionRepository;

    @Autowired
    public PrescriptionService(SubCollectionSHSRDAO<Prescription> prescriptionRepository) {

        this.prescriptionRepository = prescriptionRepository;
    }

    public String createPrescription(Prescription prescription, String patientId) throws ExecutionException, InterruptedException {
        System.out.println("patient id inside service "+patientId);
        return prescriptionRepository.save(prescription, patientId);
    }

    public Prescription getPrescription(String prescriptionIdId, String patientId) throws ExecutionException, InterruptedException {
        return prescriptionRepository.get(prescriptionIdId, patientId);
    }
    public List<Prescription> getListPrescription(String patientId) throws ExecutionException, InterruptedException {
        return prescriptionRepository.getAll(patientId);
    }

    public String updatePrescription(Prescription prescription, String patientId) throws ExecutionException, InterruptedException {
        return prescriptionRepository.update(prescription, patientId);
    }

    public String deletePrescription(String prescriptionId, String patientId) throws ExecutionException, InterruptedException {
        return prescriptionRepository.delete(patientId, prescriptionId);
    }
}
