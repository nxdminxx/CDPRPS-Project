// package com.SmartHealthRemoteSystem.SHSR.ProvideDiagnosis;
// import com.SmartHealthRemoteSystem.SHSR.Service.DiagnosisService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;
// import java.util.concurrent.ExecutionException;

// @RestController
// @RequestMapping("/api/diagnosis") // Base path for all operations related to diagnoses
// public class DiagnosisRequestController {

//     private final DiagnosisService diagnosisService;

//     //@Autowired
//     public DiagnosisRequestController(DiagnosisService diagnosisService) {
//         this.diagnosisService = diagnosisService;
//     }
// // 
//     // Endpoint to add a new diagnosis
//     @PostMapping("/diagnosisRequest")
//     public ResponseEntity<Diagnosis> addDiagnosis(@RequestBody Diagnosis diagnosis) throws ExecutionException, InterruptedException {
//     Diagnosis savedDiagnosis = diagnosisService.saveDiagnosis(diagnosis);
//     return ResponseEntity.ok(savedDiagnosis); // Returns the saved diagnosis
// }


 

//     // Endpoint to retrieve all diagnoses
//     @GetMapping
//     public ResponseEntity<List<Diagnosis>> getAllDiagnoses() throws ExecutionException, InterruptedException {
//         List<Diagnosis> diagnoses = diagnosisService.getAllDiagnoses();
//         return ResponseEntity.ok(diagnoses); // Returns the list of diagnoses
//     }


    
    
    
// }