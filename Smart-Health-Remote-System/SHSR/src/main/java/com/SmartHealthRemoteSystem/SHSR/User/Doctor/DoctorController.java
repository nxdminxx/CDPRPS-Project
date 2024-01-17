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
import java.util.stream.Collectors;

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
    public String getPatientListThatAssignedToDoctor(Model model,@RequestParam(defaultValue = "0") int pageNo, 
    @RequestParam(defaultValue = "5") int pageSize, @RequestParam(defaultValue = "") String searchQuery) throws ExecutionException, InterruptedException {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails= (MyUserDetails) auth.getPrincipal();
        Doctor doctor = doctorService.getDoctor(myUserDetails.getUsername());

        List<Patient> allPatients = doctorService.findAllPatientAssignToDoctor(doctor.getUserId());
        // List<Patient> patientList = doctorService.findAllPatientAssignToDoctor(doctor.getUserId());

         if (!searchQuery.isEmpty()) {
        allPatients = allPatients.stream()
                                 .filter(p -> p.getName().toLowerCase().contains(searchQuery.toLowerCase()) 
                                           || p.getUserId().toString().contains(searchQuery))
                                 .collect(Collectors.toList());
    }
    
        int total = allPatients.size();
        int start = Math.min(pageNo * pageSize, total);
        int end = Math.min((pageNo + 1) * pageSize, total);
        int startIndex = pageNo * pageSize;

        List<Patient> patientList = allPatients.subList(start, end);

        //paging end
        model.addAttribute("startIndex", startIndex);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", (total + pageSize - 1) / pageSize);
        model.addAttribute("patientList", patientList);
        model.addAttribute("searchQuery", searchQuery);
        model.addAttribute("doctor", doctor);
        


        return "myPatient";
    }


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



