package com.SmartHealthRemoteSystem.SHSR.Service;

import com.SmartHealthRemoteSystem.SHSR.Medicine.Medicine;
import com.SmartHealthRemoteSystem.SHSR.Medicine.MedicineRepository;
import com.SmartHealthRemoteSystem.SHSR.Repository.SHSRDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class MedicineService {
    
    private final SHSRDAO<Medicine> MedicineRepository;

    public MedicineService() {
        MedicineRepository=new MedicineRepository();
    }

    @Autowired
    public MedicineService(SHSRDAO<Medicine> MedicineRepository) {
        this.MedicineRepository = MedicineRepository;
    }

    public String createMedicine(Medicine medicine) throws ExecutionException, InterruptedException {
        return MedicineRepository.save(medicine);
    }

    public List<Medicine> getListMedicine() throws ExecutionException, InterruptedException {
        return MedicineRepository.getAll();
    }

    public String deleteMedicine(String MedId) throws ExecutionException, InterruptedException {
        return MedicineRepository.delete(MedId);
    }

    public Medicine getMedicine(String medicine) throws ExecutionException, InterruptedException {
        return MedicineRepository.get(medicine);
    }

    public String updateMedicine(Medicine medicine) throws ExecutionException, InterruptedException {
        return MedicineRepository.update(medicine);
    }

    public String stringMedicine(String MedId) throws ExecutionException, InterruptedException {
        Medicine medicine=MedicineRepository.get(MedId);
        return medicine.toString();
    }

    public void addMedicine(String medName, int quantity) throws ExecutionException, InterruptedException {
        // Create a new Medicine object with auto-generated medId
        Medicine newMedicine = new Medicine(medName, quantity);
    
        // Call the service method to add the new medicine to the database
        MedicineRepository.save(newMedicine);
    }

    public List<Medicine> getMedicineList() throws ExecutionException, InterruptedException {
        return MedicineRepository.getAll();
    }

    public String prescribeMedicine(String medName, int quantity, String patientId) throws ExecutionException, InterruptedException {
        Medicine newMedicine = new Medicine(medName, quantity);
        newMedicine.setPatientId(patientId);  // Assuming Medicine class has a patientId field
    
        return MedicineRepository.save(newMedicine);
    }

    public List<Medicine> getListPrescribe(String patientId) throws ExecutionException, InterruptedException {
        List<Medicine> allMedicines = MedicineRepository.getAll();
        return allMedicines.stream()
                .filter(medicine -> patientId.equals(medicine.getPatientId())) // Assuming Medicine has getPatientId method
                .collect(Collectors.toList());
    }
}
