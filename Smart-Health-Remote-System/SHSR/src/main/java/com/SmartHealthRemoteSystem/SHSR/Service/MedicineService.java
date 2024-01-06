package com.SmartHealthRemoteSystem.SHSR.Service;

import com.SmartHealthRemoteSystem.SHSR.Medicine.Medicine;
import com.SmartHealthRemoteSystem.SHSR.Medicine.MedicineRepository;
import com.SmartHealthRemoteSystem.SHSR.Repository.SHSRDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

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
}
