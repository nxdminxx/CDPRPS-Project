package com.SmartHealthRemoteSystem.SHSR.SendPrescriptions;

import com.SmartHealthRemoteSystem.SHSR.Medicine.Medicine;
import com.SmartHealthRemoteSystem.SHSR.Service.DoctorService;
import com.SmartHealthRemoteSystem.SHSR.Service.MedicineService;
import com.SmartHealthRemoteSystem.SHSR.Service.PatientService;
import com.SmartHealthRemoteSystem.SHSR.Service.PrescriptionService;
import com.SmartHealthRemoteSystem.SHSR.User.Doctor.Doctor;
import com.SmartHealthRemoteSystem.SHSR.User.Patient.Patient;
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

        List<Medicine> medicineList = medicineService.getListMedicine();
        model.addAttribute("patientName", patientService.getPatient(patientId).getName());
        model.addAttribute("patientId", patientId);
        model.addAttribute("doctor",doctor);
        model.addAttribute("medicineList", medicineList);
        System.out.println("patient id before"+patientId);
        return "sendPrescriptionForm";
    }

    @GetMapping("/add-medicine")
    public String addMedication(@RequestParam String patientId, Model model) throws ExecutionException, InterruptedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails= (MyUserDetails) auth.getPrincipal();
        Doctor doctor = doctorService.getDoctor(myUserDetails.getUsername());

        List<Medicine> medicineList = medicineService.getListMedicine();
        model.addAttribute("patientName", patientService.getPatient(patientId).getName());
        model.addAttribute("patientId", patientId);
        model.addAttribute("doctor",doctor);
        model.addAttribute("medicineList", medicineList);
        return "addMedicine";
    }

    @PostMapping("/add-medicine/submit")
    public String sumbitMedicineForm(Model model,@RequestParam String patientId, @RequestParam(value = "addMed") String addMed, @RequestParam(value = "quantity") int quantity) 
            throws ExecutionException, InterruptedException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails= (MyUserDetails) auth.getPrincipal();
        Doctor doctor = doctorService.getDoctor(myUserDetails.getUsername());

        Medicine medicine1 = new Medicine(addMed, quantity);
        //Medicine medicine2 = new Medicine(quantity);
        medicineService.createMedicine(medicine1);
        //medicineService.createMedicine(medicine2);

        System.out.println("patient id before: "+patientId);

        List<Medicine> medicineList = medicineService.getListMedicine();
        
        model.addAttribute("patientName", patientService.getPatient(patientId).getName());
        model.addAttribute("patientId", patientId);
        model.addAttribute("doctor",doctor);
        model.addAttribute("medicineList", medicineList);
        return "sendPrescriptionForm";
    }
    

   /*  @PostMapping("/add-medicine/sendToPatient")
    public String submitPatientMedicine(Model model,
                                         @RequestParam(value = "patientId") String patientId,
                                         @RequestParam(value = "doctorId") String doctorId,
                                         @RequestParam(value = "prescription") String prescription,
                                         @RequestParam(value = "diagnosisAilment") String diagnosisAilment,
                                         @RequestParam(value = "medicine") List<String> medicineList)
            throws ExecutionException, InterruptedException {
        if(medicineList.size()==0){
            //default value if user didn't input any list of medicine
        }else{
            //if user input any medicine in the list
            //shift to the left by 1 index
            medicineList.remove(0);

        }
       /*  Medicine medicine1 = new Medicine(medicineList); 


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails= (MyUserDetails) auth.getPrincipal();
        Doctor doctor = doctorService.getDoctor(myUserDetails.getUsername());
        List<Patient> patientList = doctorService.findAllPatientAssignToDoctor(doctor.getUserId());
        model.addAttribute("patientList", patientList);
        model.addAttribute("doctor",doctor);
        // String timeCreated = prescriptionService.createPrescription(prescription1,patientId);
        return "sendPrescriptionForm";
    } */

    @PostMapping("/form/submit")
    public String submitPrescriptionForm(Model model,
                                         @RequestParam(value = "patientId") String patientId,
                                         @RequestParam(value = "doctorId") String doctorId,
                                         @RequestParam(value = "prescription") String prescription,
                                         @RequestParam(value = "diagnosisAilment") String diagnosisAilment,
                                         @RequestParam(value = "patientMedList") List<String> patientMedList)
            throws ExecutionException, InterruptedException {
        /* if(medicineList.size()==0){
            //default value if user didn't input any list of medicine
        }else{
            //if user input any medicine in the list
            //shift to the left by 1 index
            medicineList.remove(0);

        } */

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

}
