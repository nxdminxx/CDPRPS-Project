package com.SmartHealthRemoteSystem.SHSR.Medicine;

import com.SmartHealthRemoteSystem.SHSR.Repository.SHSRDAO;
import com.SmartHealthRemoteSystem.SHSR.Repository.SubCollectionSHSRDAO;
import com.SmartHealthRemoteSystem.SHSR.Service.PatientService;
import com.SmartHealthRemoteSystem.SHSR.User.Doctor.Doctor;
import com.SmartHealthRemoteSystem.SHSR.User.Patient.Patient;
import com.SmartHealthRemoteSystem.SHSR.User.Patient.PatientRepository;
import com.SmartHealthRemoteSystem.SHSR.User.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.*;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class MedicineRepository implements SHSRDAO<Medicine> {
    private final String COL_NAME = "Medicine";

    @Override
    public Medicine get(String MedId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COL_NAME).document(MedId);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        Medicine tempMedicine;
        if (document.exists()) {
            tempMedicine = document.toObject(Medicine.class);
            return tempMedicine;
        } else {
            return null;
        }
    }

    @Override
    public List<Medicine> getAll() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Iterable<DocumentReference> documentReference = dbFirestore.collection(COL_NAME).listDocuments();
        Iterator<DocumentReference> iterator = documentReference.iterator();

        List<Medicine> medicineList = new ArrayList<>();
        Medicine medicine;
        while (iterator.hasNext()) {
            DocumentReference documentReference1=iterator.next();
            ApiFuture<DocumentSnapshot> future = documentReference1.get();
            DocumentSnapshot document = future.get();
            medicine = document.toObject(Medicine.class);
            medicineList.add(medicine);
        }

        return medicineList;
    }

    @Override
    public String save(Medicine medicine) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        //auto create data ID by firebase
        DocumentReference addedDocRef = dbFirestore.collection(COL_NAME).document();
        medicine.setMedId(addedDocRef.getId());
        ApiFuture<WriteResult> collectionsApiFuture =
                addedDocRef.set(medicine);
        /* ApiFuture<WriteResult> writeResult = addedDocRef.update("timestamp", collectionsApiFuture.get().getUpdateTime()); */

        return addedDocRef.getId();
    }

    @Override
    public String update(Medicine medicine) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference addedDocRef = dbFirestore.collection(COL_NAME).document(medicine.getMedId());
        ApiFuture<WriteResult> collectionsApiFuture = null;
        if(!(medicine.getMedName().isEmpty())){
            collectionsApiFuture =addedDocRef.update("MedName", medicine.getMedName());
        }
        /* if (!(sensorData.getOxygenReading().isEmpty())){
            collectionsApiFuture =  addedDocRef.update("oxygenReading", sensorData.getOxygenReading());
        }
        if (sensorData.getBodyTemperature() != null){
            collectionsApiFuture = addedDocRef.update("bodyTemperature", sensorData.getBodyTemperature());
        }
        if (sensorData.getHeart_Rate() != 0){
            collectionsApiFuture = addedDocRef.update("Heart_Rate", sensorData.getHeart_Rate());
        }//mg, ijat, keng, faruq, din
        if (collectionsApiFuture != null) {
            ApiFuture<WriteResult> writeResult = addedDocRef.update("timestamp", collectionsApiFuture.get().getUpdateTime());
            return writeResult.get().getUpdateTime().toString();
        } */
        return Timestamp.now().toString();
    }

    @Override
    public String delete(String MedId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        if(get(MedId) == null){
            return "The medicine with Id " + MedId + " is not exist.";
        }
        ApiFuture<WriteResult> writeResult = dbFirestore.collection(COL_NAME).document(MedId).delete();
        return "Document with medicine id " + MedId + " has been deleted";
    }

    
}

