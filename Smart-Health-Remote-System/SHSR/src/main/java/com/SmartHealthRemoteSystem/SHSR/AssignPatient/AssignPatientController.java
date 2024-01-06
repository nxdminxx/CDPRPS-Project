package com.SmartHealthRemoteSystem.SHSR.AssignPatient;

import com.SmartHealthRemoteSystem.SHSR.Service.AssignPatientServices;
import com.SmartHealthRemoteSystem.SHSR.User.Doctor.Doctor;
import com.SmartHealthRemoteSystem.SHSR.User.Patient.Patient;
import com.SmartHealthRemoteSystem.SHSR.WebConfiguration.MyUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/assignpatient")
public class AssignPatientController {
    public final AssignPatientServices assignPatientServices;

    public AssignPatientController(AssignPatientServices assignPatientServices) {
        this.assignPatientServices = assignPatientServices;
    }

    @GetMapping
    public String AssignPatientForm(Model model) throws ExecutionException, InterruptedException{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails= (MyUserDetails) auth.getPrincipal();
        Doctor doctor = assignPatientServices.getDoctor(myUserDetails.getUsername());
        List<Patient> patientList= assignPatientServices.getListPatient();
        model.addAttribute("patientList", patientList);
        model.addAttribute("doctor",doctor);
        return "assignpatient";
    }
    @PostMapping("/assigntoDoctor")
    public String AssigntoDoctor(Model model, @RequestParam (value= "patientId")String patientID) throws ExecutionException, InterruptedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails= (MyUserDetails) auth.getPrincipal();
        Doctor doctor = assignPatientServices.getDoctor(myUserDetails.getUsername());
        Patient patient =assignPatientServices.getPatient(patientID);
        assignPatientServices.AssignPatient(patientID,doctor.getUserId());
        List<Patient> patientList= assignPatientServices.getListPatient();
        model.addAttribute("patientList",patientList);
        model.addAttribute("doctor",doctor);
        return "assignpatient";
    }
    @PutMapping("/unassignDoctor")
    public String UnassignDoctor(Model model,@RequestParam (value="patientId")String patientID) throws ExecutionException, InterruptedException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails= (MyUserDetails) auth.getPrincipal();
        Doctor doctor = assignPatientServices.getDoctor(myUserDetails.getUsername());
        Patient patient =assignPatientServices.getPatient(patientID);
        assignPatientServices.UnassignDoctor(patientID,doctor.getUserId());
        List<Patient> patientList=assignPatientServices.getListPatient();
        model.addAttribute("patientList",patientList);
        model.addAttribute("doctor",doctor);
        return "assignpatient";

    }
    @PostMapping("/releasepatient")
    public String ReleasePatient(Model model,@RequestParam (value ="patientId")String patientID) throws ExecutionException, InterruptedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails= (MyUserDetails) auth.getPrincipal();
        Doctor doctor = assignPatientServices.getDoctor(myUserDetails.getUsername());
        Patient patient =assignPatientServices.getPatient(patientID);
        assignPatientServices.ReleasePatient(patientID,doctor.getUserId());
        List<Patient> patientList=assignPatientServices.getListPatient();
        model.addAttribute("patientList",patientList);
        model.addAttribute("doctor",doctor);
        return "assignpatient";
    }
}
