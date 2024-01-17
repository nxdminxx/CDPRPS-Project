package com.SmartHealthRemoteSystem.SHSR.SendPrescriptions;

import com.SmartHealthRemoteSystem.SHSR.Medicine.Medicine;
import com.SmartHealthRemoteSystem.SHSR.Service.DoctorService;
import com.SmartHealthRemoteSystem.SHSR.Service.MedicineService;
import com.SmartHealthRemoteSystem.SHSR.Service.PatientService;
import com.SmartHealthRemoteSystem.SHSR.Service.PrescriptionService;
import com.SmartHealthRemoteSystem.SHSR.User.Doctor.Doctor;
import com.SmartHealthRemoteSystem.SHSR.User.Patient.Patient;
import com.SmartHealthRemoteSystem.SHSR.ViewDoctorPrescription.PrescribeMedicine;
import com.SmartHealthRemoteSystem.SHSR.ViewDoctorPrescription.Prescription;
import com.SmartHealthRemoteSystem.SHSR.WebConfiguration.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/prescription")
public class SendPrescriptionController {

    private final DoctorService doctorService;
    private final PatientService patientService;
    private final PrescriptionService prescriptionService;
    private final MedicineService medicineService;

    @Autowired
    public SendPrescriptionController(DoctorService doctorService, PatientService patientService, PrescriptionService prescriptionService, MedicineService medicineService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.prescriptionService = prescriptionService;
        this.medicineService = medicineService;
    }

    @GetMapping("/form")
    public String getPrescriptionForm(@RequestParam String patientId, Model model) throws ExecutionException, InterruptedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails= (MyUserDetails) auth.getPrincipal();
        Doctor doctor = doctorService.getDoctor(myUserDetails.getUsername());

        List<Medicine> prescribList = medicineService.getListPrescribe(patientId); //tukar utk dptkan stock medicine list

        model.addAttribute("patientName", patientService.getPatient(patientId).getName());
        model.addAttribute("patientId", patientId);
        model.addAttribute("doctor",doctor);
        model.addAttribute("prescribList", prescribList);
        System.out.println("patient id before"+patientId);
        return "sendPrescriptionForm";
    }

    @GetMapping("/add-prescription")
    public String addMedication(@RequestParam String patientId, Model model) throws ExecutionException, InterruptedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails= (MyUserDetails) auth.getPrincipal();
        Doctor doctor = doctorService.getDoctor(myUserDetails.getUsername());

        List<Medicine> medicineList = medicineService.getListMedicine(); //new method for patientMedicine
    
        model.addAttribute("patientName", patientService.getPatient(patientId).getName());
        model.addAttribute("patientId", patientId);
        model.addAttribute("doctor",doctor);
        model.addAttribute("medicineList", medicineList);
        return "patientMedicine";
    }

    // @GetMapping("/prescribeMedicine")
    // public String addMedication(@RequestParam String patientId, Model model) throws ExecutionException, InterruptedException {
    //     Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    //     MyUserDetails myUserDetails= (MyUserDetails) auth.getPrincipal();
    //     Doctor doctor = doctorService.getDoctor(myUserDetails.getUsername());

    //     List<Medicine> medicineList = medicineService.getListMedicine();
    
    //     model.addAttribute("patientName", patientService.getPatient(patientId).getName());
    //     model.addAttribute("patientId", patientId);
    //     model.addAttribute("doctor",doctor);
    //     model.addAttribute("medicineList", medicineList);
    //     return "patientMedicine";
    // }

    @PostMapping("/prescribemedicine/submit")
    public String prescribeMedicine(Model model,
        @RequestParam Map<String, String> allParams,
        @RequestParam (value= "selectedMedicines") List<String> selectedMedicines,
        @RequestParam String patientId)
        throws ExecutionException, InterruptedException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails = (MyUserDetails) auth.getPrincipal();
        Doctor doctor = doctorService.getDoctor(myUserDetails.getUsername());

        List<PrescribeMedicine> prescribedMedicines = new ArrayList<>();

        for (String medName : selectedMedicines) {
            String quantityParam = "quantity_" + medName.replace(" ", "_"); // Ensure that the parameter name is consistent with the input name attribute
            String quantityStr = allParams.get(quantityParam);
            if (quantityStr != null && !quantityStr.isEmpty()) {
                int quantity = Integer.parseInt(quantityStr);

                // Prescribe medicine and deduct stock
                medicineService.prescribeMedicine(patientId, medName, quantity);

                // Add the prescribed medicine to a new prescription
                prescriptionService.addMedicineToNewPrescription(patientId, doctor.getUserId(), medName, quantity);

                // Add to local list to be displayed later
                prescribedMedicines.add(new PrescribeMedicine(medName, patientId, quantity));
            }
        }

        model.addAttribute("patientId", patientId);
        model.addAttribute("doctor", doctor);
        model.addAttribute("prescribedMedicines", prescribedMedicines);

        // Redirect to the page that displays the prescribed medicines list
        return "PatientMedicine";
    }

    @PostMapping("/form/submit")
    public String submitPrescriptionForm(Model model,
                                         @RequestParam(value = "patientId") String patientId,
                                         @RequestParam(value = "doctorId") String doctorId,
                                         @RequestParam(value = "prescription") String prescription,
                                         @RequestParam(value = "diagnosisAilment") String diagnosisAilment,
                                         @RequestParam(value = "patientMedList") List<String> patientMedList)
            throws ExecutionException, InterruptedException {


        System.out.println("patient id before"+patientId);
        
        Prescription prescription1 = new Prescription(doctorId,
                patientMedList,
                prescription,
                diagnosisAilment);


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails= (MyUserDetails) auth.getPrincipal();
        Doctor doctor = doctorService.getDoctor(myUserDetails.getUsername());
        List<Patient> patientList = doctorService.findAllPatientAssignToDoctor(doctor.getUserId());
        model.addAttribute("patientList", patientList);
        model.addAttribute("doctor",doctor);
        String timeCreated = prescriptionService.createPrescription(prescription1,patientId);
        System.out.println("patient id after"+patientId);
        return "myPatient";
    }

    @GetMapping("/history")
    public String viewPrescriptionHistory(@RequestParam String patientId, Model model) throws ExecutionException, InterruptedException {
        List<Prescription> prescriptionHistory = prescriptionService.getListPrescription(patientId);
        model.addAttribute("prescriptionHistory", prescriptionHistory);
        return "prescriptionHistory";
    }

}
