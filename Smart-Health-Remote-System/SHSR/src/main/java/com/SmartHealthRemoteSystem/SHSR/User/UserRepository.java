package com.SmartHealthRemoteSystem.SHSR.User;

import com.SmartHealthRemoteSystem.SHSR.Repository.SHSRDAO;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Repository
public class UserRepository implements SHSRDAO<User> {
    public final String COL_NAME = "User";

    @Override
    public User get(String id) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COL_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        User user;
        user = document.toObject(User.class);
        return user;
    }

    @Override
    public List<User> getAll() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Iterable<DocumentReference> documentReference = dbFirestore.collection(COL_NAME).listDocuments();
        Iterator<DocumentReference> iterator = documentReference.iterator();

        List<User> userList = new ArrayList<>();
        User user;
        while (iterator.hasNext()) {
            DocumentReference documentReference1=iterator.next();
            ApiFuture<DocumentSnapshot> future = documentReference1.get();
            DocumentSnapshot document = future.get();
            user = document.toObject(User.class);
            userList.add(user);
        }
        return userList;
    }

    @Override
    public String save(User user) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(user.getUserId()).set(user);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    @Override
    public String update(User user) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = null;
        //if-else condition is added to check which field does the user update.
        //statement inside if-else condition will add change value into a map key value
        //map key value will be pass to firestore database to be updated.
        if(!(user.getName().isEmpty())){
            collectionsApiFuture = dbFirestore.collection(COL_NAME).document(user.getUserId()).update("name", user.getName());
        }
    
        if(!(user.getContact().isEmpty())){
            collectionsApiFuture = dbFirestore.collection(COL_NAME).document(user.getUserId()).update("contact", user.getContact());
        }

        if (collectionsApiFuture != null) {
            return collectionsApiFuture.get().getUpdateTime().toString();
        } else{
            return Timestamp.now().toString();
        }
    }

    @Override
    public String delete(String id) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(id).delete();
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    /* @Override
    public String updatePat(User user) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference docRef = dbFirestore.collection(COL_NAME).document(user.getUserId());

        Map<String, Object> updates = new HashMap<>();
        if (!user.getName().isEmpty()) {
            updates.put("name", user.getName());
        }
        if (!user.getPassword().isEmpty()) {
            updates.put("password", user.getPassword());
        }
        if (!user.getContact().isEmpty()) {
            updates.put("contact", user.getContact());
        }
        if (!user.getRole().isEmpty()) {
            updates.put("role", user.getRole());
        }

        if (!updates.isEmpty()) {
            ApiFuture<WriteResult> collectionsApiFuture = docRef.update(updates);
            return collectionsApiFuture.get().getUpdateTime().toString();
        } else {
            return Timestamp.now().toString();
        }
    } */

}
