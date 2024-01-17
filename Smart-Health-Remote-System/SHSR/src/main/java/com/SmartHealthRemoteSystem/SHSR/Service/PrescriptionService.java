package com.SmartHealthRemoteSystem.SHSR.Service;

import com.SmartHealthRemoteSystem.SHSR.Repository.SHSRDAO;
import com.SmartHealthRemoteSystem.SHSR.Repository.SubCollectionSHSRDAO;
import com.SmartHealthRemoteSystem.SHSR.ViewDoctorPrescription.PrescribeMedicine;
import com.SmartHealthRemoteSystem.SHSR.ViewDoctorPrescription.Prescription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
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

    public String addMedicineToNewPrescription(String patientId, String doctorId, String medId, int quantity) throws ExecutionException, InterruptedException {
        // Create a new Prescription object
        Prescription newPrescription = new Prescription();
        newPrescription.setDoctorId(doctorId);
        // You can set the timestamp here based on your requirements
        newPrescription.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
        
        // Add the prescribed medicine to the prescription's medicine list
        List<String> medicineList = new ArrayList<>();
        medicineList.add(medId); // Here you could add both medId and quantity or create a custom object that holds both
        newPrescription.setMedicineList(medicineList);
        
        // Optionally, set other details like prescription description or diagnosis
        newPrescription.setPrescriptionDescription("Prescription for " + patientId); // This is an example, set a real description
        newPrescription.setDiagnosisAilmentDescription("Diagnosis for " + patientId); // This is an example, set a real diagnosis
        
        // Save the new prescription in the database
        return createPrescription(newPrescription, patientId);
    }

    public List<PrescribeMedicine> getPrescribedMedicines(String patientId) throws ExecutionException, InterruptedException {
        List<Prescription> prescriptions = getListPrescription(patientId); // Retrieve all prescriptions for the patient
        List<PrescribeMedicine> prescribedMedicines = new ArrayList<>();
    
        // Iterate through all prescriptions
        for (Prescription prescription : prescriptions) {
            // Assuming Prescription contains a list of PrescribeMedicine objects
            // This requires a getPrescribedMedicines method or similar in the Prescription class.
            // List<PrescribeMedicine> prescribeMedicineList = prescription.getPrescribedMedicines(); // This method needs to be implemented in the Prescription class
            
            // Add all PrescribeMedicine objects to the list to be returned
            // prescribedMedicines.addAll(prescribeMedicineList);
        }
        
        return prescribedMedicines;
    }

}
