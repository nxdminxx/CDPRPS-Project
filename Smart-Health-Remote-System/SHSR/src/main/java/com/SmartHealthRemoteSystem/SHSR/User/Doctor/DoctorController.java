package com.SmartHealthRemoteSystem.SHSR.User.Doctor;

import com.SmartHealthRemoteSystem.SHSR.ReadSensorData.SensorData;
import com.SmartHealthRemoteSystem.SHSR.ReadSensorData.SensorDataRepository;
import com.SmartHealthRemoteSystem.SHSR.Service.DoctorService;
import com.SmartHealthRemoteSystem.SHSR.Service.PatientService;
import com.SmartHealthRemoteSystem.SHSR.Service.SensorDataService;
import com.SmartHealthRemoteSystem.SHSR.User.Patient.Patient;
import com.SmartHealthRemoteSystem.SHSR.WebConfiguration.MyUserDetails;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.Query.Direction;
import com.google.firebase.cloud.FirestoreClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.Doc;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RequestMapping("/doctor")
@Controller
public class DoctorController {
    
    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public String getDoctorDashboard(Model model) throws ExecutionException, InterruptedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails= (MyUserDetails) auth.getPrincipal();
        Doctor doctor = doctorService.getDoctor(myUserDetails.getUsername());
        List<Patient> patientList= doctorService.getListPatient();
        model.addAttribute("patientList", patientList);
        model.addAttribute("doctor",doctor);
        return "doctorDashBoard";
    }

    @GetMapping("/myPatient")
    public String getPatientListThatAssignedToDoctor(Model model, @RequestParam(value = "pageNo") int pageNo) throws ExecutionException, InterruptedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails= (MyUserDetails) auth.getPrincipal();
        Doctor doctor = doctorService.getDoctor(myUserDetails.getUsername());
        List<Patient> patientList = doctorService.findAllPatientAssignToDoctor(doctor.getUserId());

        //paging start
        int pageSize = 5;
        int currentPage = pageNo;
        int start = (currentPage - 1) * pageSize;
        int end = start + pageSize;
        if (start > patientList.size()) {
            start = 0;
        }
        if (end > patientList.size()) {
            end = patientList.size();
        }
        List<Patient> patientsToDisplay = patientList.subList(start, end);
        int totalPages = (int) Math.ceil((double) patientList.size() / pageSize);

        int prevPage = currentPage - 1;
        int nextPage = currentPage + 1;
        if (prevPage < 1) {
        prevPage = 1;
        }
        if (nextPage > totalPages) {
        nextPage = totalPages;
        }
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("prevPage", prevPage);
        //paging end
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPage", totalPages);
        model.addAttribute("patientList", patientsToDisplay);
        model.addAttribute("doctor", doctor);

        return "myPatient";
    }

    // @GetMapping("/sensorDashboard")
    // public String getSensorDashboard(Model model, @RequestParam(value= "patientId") String patientId) throws Exception {
    //     Firestore firestore = FirestoreClient.getFirestore();
    //       Patient patient = doctorService.getPatient(patientId);
    //       SensorDataService sensorDataService = new SensorDataService();
    //       SensorData sensorData = sensorDataService.getSensorData(patient.getSensorDataId());
        
    //       Query query = firestore.collection("SensorData")
    //       .document(patient.getSensorDataId())
    //       .collection("SensorDataHistory").orderBy("#", Direction.DESCENDING).limit(1);
    //       ApiFuture<QuerySnapshot> querySnapshot = query.get();
    //       List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        
    //       int highestDocumentNumber = 0;
    //       if (!documents.isEmpty()) {
    //         highestDocumentNumber = documents.get(0).getLong("#").intValue();
    //       }
          
    //       // Create a new document with the next document number
    //       DocumentReference docRef = firestore.collection("SensorData")
    //       .document(patient.getSensorDataId())
    //       .collection("SensorDataHistory")
    //       .document("sensordata" + String.format("%03d", highestDocumentNumber + 1));
  
    // // Populate the document with the sensor data fields
    //       Map<String, Object> data = new HashMap<>();
    //       data.put("#", highestDocumentNumber +1 );
    //       data.put("Heart_Rate", sensorData.getHeart_Rate());
    //       data.put("bodyTemperature", sensorData.getBodyTemperature());
    //       data.put("ecgReading", sensorData.getEcgReading());
    //       data.put("oxygenReading", sensorData.getOxygenReading());
    //       data.put("sensorDataId", sensorData.getSensorDataId());
    //       data.put("timestamp", sensorData.getTimestamp());
  
    //       model.addAttribute("sensorDataList",sensorData);
    //       model.addAttribute("patientid",patientId);
  
    //       // Write the data to the document
    //       docRef.set(data);

         
       
    //     Iterable<DocumentReference> documentReference = firestore.collection("SensorData")
    //     .document(patient.getSensorDataId())
    //     .collection("SensorDataHistory").listDocuments();
    //     Iterator<DocumentReference> iterator = documentReference.iterator();

    //     List<SensorData> sensorDataList = new ArrayList<>();
    //     SensorData sensorDatahistory;
    //     while (iterator.hasNext()) {
    //         DocumentReference documentReference1=iterator.next();
    //         ApiFuture<DocumentSnapshot> future = documentReference1.get();
    //         DocumentSnapshot document = future.get();
    //         sensorData = document.toObject(SensorData.class);
    //         sensorDataList.add(sensorData);
    //         model.addAttribute("sensorDataListHistory",sensorDataList);
    //         System.out.println("-------------------------------------------------------------------------------");
    //         System.out.println(sensorDataList);
    //         System.out.println("-------------------------------------------------------------------------------");

    //     }

    //     model.addAttribute("success","success");
       
    //       return "sensorDashboard";
    //   }


    // @PostMapping("/create")
    // public String saveSensorData(Model model, @RequestParam(value= "sensordata") String sensordata,
    //  @ModelAttribute("patientid") String patientid) throws Exception {
    //     SensorDataService sensorDataService = new SensorDataService();
    //     Firestore firestore = FirestoreClient.getFirestore();
    //     // Create a new document with the next document number
    //     DocumentReference docRef = firestore.collection("SensorData").document(sensordata);
    //     DocumentReference patientRef = firestore.collection("Patient").document(patientid);

    // Populate the document with the sensor data fields
    
    // Map<String, Object> data = new HashMap<>();
    //     data.put("Heart_Rate", 0);
    //         data.put("bodyTemperature", 0);
    //         data.put("ecgReading", 0);
    //         data.put("oxygenReading", 0);
    //         data.put("sensorDataId", sensordata);
    //         data.put("timestamp", FieldValue.serverTimestamp());

    //         // Write the data to the document
    //         docRef.set(data);

    //         Map<String, Object> updates = new HashMap<>();
    //         updates.put("sensorDataId", sensordata);
    //         patientRef.update(updates);
    //         Patient patient = doctorService.getPatient(patientid);
    //         SensorData sensorData = sensorDataService.getSensorData(patient.getSensorDataId());
    //         model.addAttribute("sensorDataList",sensorData);
    //         model.addAttribute("patientid",patientid);
        
    //         return "redirect:/doctor/sensorDashboard?patientId=" + patientid;
    //     }

    //   @PostMapping("/savehistory")
    //   public String savehistory(Model model, @RequestParam(value= "patientid") String patientId) throws Exception {
    //       Firestore firestore = FirestoreClient.getFirestore();
    //       Patient patient = doctorService.getPatient(patientId);
    //       SensorDataService sensorDataService = new SensorDataService();
    //       SensorData sensorData = sensorDataService.getSensorData(patient.getSensorDataId());
        
    //       Query query = firestore.collection("SensorData")
    //       .document(patient.getSensorDataId())
    //       .collection("SensorDataHistory").orderBy("#", Direction.DESCENDING).limit(1);
    //       ApiFuture<QuerySnapshot> querySnapshot = query.get();
    //       List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        
    //       int highestDocumentNumber = 0;
    //       if (!documents.isEmpty()) {
    //         highestDocumentNumber = documents.get(0).getLong("#").intValue();
    //       }
          
    //       // Create a new document with the next document number
    //       DocumentReference docRef = firestore.collection("SensorData")
    //       .document(patient.getSensorDataId())
    //       .collection("SensorDataHistory")
    //       .document("sensordata" + String.format("%03d", highestDocumentNumber + 1));
  
    // Populate the document with the sensor data fields
        //   Map<String, Object> data = new HashMap<>();
        //   data.put("#", highestDocumentNumber +1 );
        //   data.put("Heart_Rate", sensorData.getHeart_Rate());
        //   data.put("bodyTemperature", sensorData.getBodyTemperature());
        //   data.put("ecgReading", sensorData.getEcgReading());
        //   data.put("oxygenReading", sensorData.getOxygenReading());
        //   data.put("sensorDataId", sensorData.getSensorDataId());
        //   data.put("timestamp", sensorData.getTimestamp());
  
        //   model.addAttribute("sensorDataList",sensorData);
        //   model.addAttribute("patientid",patientId);
  
        //   // Write the data to the document
        //   docRef.set(data);

        // Iterable<DocumentReference> documentReference = firestore.collection("SensorData")
        // .document(patient.getSensorDataId())
        // .collection("SensorDataHistory").listDocuments();
        // Iterator<DocumentReference> iterator = documentReference.iterator();

        // List<SensorData> sensorDataList = new ArrayList<>();
        // SensorData sensorDatahistory;

    //     while (iterator.hasNext()) {
    //         DocumentReference documentReference1=iterator.next();
    //         ApiFuture<DocumentSnapshot> future = documentReference1.get();
    //         DocumentSnapshot document = future.get();
    //         sensorData = document.toObject(SensorData.class);
    //         sensorDataList.add(sensorData);
    //         model.addAttribute("sensorDataListHistory",sensorDataList);
    //         System.out.println("-------------------------------------------------------------------------------");
    //         System.out.println(sensorDataList);
    //         System.out.println("-------------------------------------------------------------------------------");
    //     }
    //     model.addAttribute("success","success");
       
    //       return "sensorDashboard";
    //   }

    //--------------IZZAT-----------------------------------------

    @PostMapping("/create-doctor")
    public String saveDoctor(@RequestBody Doctor doctor)
            throws ExecutionException, InterruptedException {
       String msg = doctorService.createDoctor(doctor);
       return msg;
    }

    @GetMapping("/get-doctor/{doctorId}")
    public Doctor getDoctor(@PathVariable String doctorId) throws ExecutionException, InterruptedException {

        Doctor doctor = doctorService.getDoctor(doctorId);
        if(doctor != null){
            return doctor;
            //display patient data on the web
        }else{
            return null;
            //display error message
        }
    }

    @PutMapping("/update-doctor")
    public void updateDoctor(@RequestBody Doctor doctor) throws ExecutionException, InterruptedException {
        doctorService.updateDoctor(doctor);
    }

    @DeleteMapping("/delete-doctor/{doctorId}")
    public void deleteDoctor(@PathVariable String doctorId) throws ExecutionException, InterruptedException {
        doctorService.deleteDoctor(doctorId);
    }

    @GetMapping("/updateProfile")
    public String updateProfile(Model model) throws ExecutionException, InterruptedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails= (MyUserDetails) auth.getPrincipal();
        Doctor doctor = doctorService.getDoctor(myUserDetails.getUsername());
        model.addAttribute("doctor",doctor);
        return "updateDoctorProfile";
    }

    @PostMapping("/updateProfile/profile")
    public String submitProfile(@ModelAttribute Doctor doctor, @RequestParam("profilePicture") MultipartFile profilePicture) throws ExecutionException, InterruptedException, IOException {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    MyUserDetails myUserDetails = (MyUserDetails) auth.getPrincipal();
    doctor.setUserId(myUserDetails.getUsername());

    if (!profilePicture.isEmpty()) {
      
        byte[] profilePictureBytes = profilePicture.getBytes();
        String base64EncodedProfilePicture = Base64.getEncoder().encodeToString(profilePictureBytes);
        doctor.setProfilePicture(base64EncodedProfilePicture);
    }

    doctorService.updateDoctor(doctor);
    return "redirect:/doctor/updateProfile";
    }   

    @GetMapping("/doctor/profilePicture/{userId}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getProfilePicture(@PathVariable String userId) {
        // You can customize the URL based on your application structure
        String imageUrl = "/images/profile/" + userId;

        Map<String, String> responseData = new HashMap<>();
        responseData.put("profilePictureUrl", imageUrl);

        return ResponseEntity.ok(responseData);
    }


@Autowired
private SensorDataRepository sensorDataRepository;

@GetMapping("/sensorDataList")
public String getSensorDataList(Model model) throws ExecutionException, InterruptedException {
    List<SensorData> sensorDataList = sensorDataRepository.getAll();
    model.addAttribute("sensorDataList", sensorDataList);
    return "sensorDataList";
}

@GetMapping("/sensorHistory")
    public String getHistory(Model model, @RequestParam String patientId) throws Exception, InterruptedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails= (MyUserDetails) auth.getPrincipal();
        Doctor doctor = doctorService.getDoctor(myUserDetails.getUsername());
        
        Patient patient = doctorService.getPatient(patientId);
        SensorDataService sensorDataService = new SensorDataService();
        model.addAttribute("patientid",patientId);
        
    //--------------IZZAT-----------------------------------------
    //if there is no sensor id, will not call sensor data class
        if(patient.getSensorDataId().isEmpty()){
            return "HistorysensorDashboard";  
          }
        SensorData sensorData = sensorDataService.getSensorData(patient.getSensorDataId());
        model.addAttribute("sensorDataList",sensorData);
        return "HistorysensorDashboard";
    }

}



