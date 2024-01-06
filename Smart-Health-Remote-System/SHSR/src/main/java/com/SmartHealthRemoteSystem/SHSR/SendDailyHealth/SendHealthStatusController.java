package com.SmartHealthRemoteSystem.SHSR.SendDailyHealth;

import com.SmartHealthRemoteSystem.SHSR.Service.DoctorService;
import com.SmartHealthRemoteSystem.SHSR.Service.HealthStatusService;
import com.SmartHealthRemoteSystem.SHSR.Service.PatientService;
import com.SmartHealthRemoteSystem.SHSR.Service.SensorDataService;
import com.SmartHealthRemoteSystem.SHSR.User.Doctor.Doctor;
import com.SmartHealthRemoteSystem.SHSR.User.Patient.Patient;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/Health-status")
public class SendHealthStatusController {
    private final HealthStatusService healthStatusService;
    private final PatientService patientService;
    private final SensorDataService sensorDataService;
    private final DoctorService doctorService;
    public SendHealthStatusController(HealthStatusService healthStatusService, PatientService patientService, SensorDataService sensorDataService, DoctorService doctorService) {
        this.healthStatusService = healthStatusService;
        this.patientService=patientService;
        this.sensorDataService=sensorDataService;
        this.doctorService = doctorService;
    }

    @PostMapping("/sendHealthStatus")
    public String sendHealthStatus(@RequestParam(value = "symptom") String symptom,
                                   @RequestParam(value="patientId") String patientId,
                                   @RequestParam (value = "doctorId")String doctorId,
                                   Model model) throws ExecutionException, InterruptedException {


        String sensorId=patientService.getPatientSensorId(patientId);
        //symptom+="\n"+ sensorDataService.stringSensorData(sensorId);
        HealthStatus healthStatus=new HealthStatus(symptom,doctorId);
        healthStatusService.createHealthStatus(healthStatus,patientId);

        Patient patient=patientService.getPatient(patientId);
        Doctor doctor=doctorService.getDoctor(patient.getAssigned_doctor());
        model.addAttribute(patient);
        model.addAttribute(doctor);
        return "patientDashBoard";
    }

    @PostMapping("/viewHealthStatusForm")
    public String healthStatusForm(@RequestParam (value = "patientId") String patientId, Model model) throws ExecutionException, InterruptedException {
//        List<HealthStatus>  healthStatus=healthStatusService.getListHealthStatus(patientID);
//        HealthStatus firstHealthStatus=healthStatus.get(0);

        Patient patient=patientService.getPatient(patientId);
        Doctor doctor=doctorService.getDoctor(patient.getAssigned_doctor());
        model.addAttribute("patient", patient);
        model.addAttribute("doctor", doctor);

        return "sendDailyHealthSymptom";
    }
}




