package com.SmartHealthRemoteSystem.SHSR.User.Pharmacist;

import com.SmartHealthRemoteSystem.SHSR.Medicine.Medicine;
import com.SmartHealthRemoteSystem.SHSR.Pagination.PaginationInfo;
import com.SmartHealthRemoteSystem.SHSR.Service.DoctorService;
import com.SmartHealthRemoteSystem.SHSR.Service.MedicineService;
import com.SmartHealthRemoteSystem.SHSR.Service.PatientService;
import com.SmartHealthRemoteSystem.SHSR.Service.PharmacistService;
import com.SmartHealthRemoteSystem.SHSR.Service.SensorDataService;
import com.SmartHealthRemoteSystem.SHSR.User.Doctor.Doctor;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.print.Doc;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@RequestMapping("/pharmacist")
@Controller

public class PharmacistController {
    
    private final PharmacistService pharmacistService;
    private final MedicineService medicineService;

    @Autowired
    public PharmacistController(PharmacistService pharmacistService, MedicineService medicineService) {
        this.pharmacistService = pharmacistService;
        this.medicineService = medicineService;
    }

    @GetMapping
    public String pharmacistDashboard(Model model) throws ExecutionException, InterruptedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails = (MyUserDetails) auth.getPrincipal();
        Pharmacist pharmacist = pharmacistService.getPharmacist(myUserDetails.getUsername());
        
        List<Medicine> medicineStock = pharmacistService.getListMedicine();
        model.addAttribute("pharmacist", pharmacist);
        model.addAttribute("medicineStock", medicineStock);
        
        return "PharmacistDashboard";
    }

    @GetMapping("/updateProfile")
    public String updateProfile(Model model) throws ExecutionException, InterruptedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails = (MyUserDetails) auth.getPrincipal();
        Pharmacist pharmacist = pharmacistService.getPharmacist(myUserDetails.getUsername());
        model.addAttribute("pharmacist", pharmacist);
        return "PharmacistProfile";
    }

    @PostMapping("/updateProfile/profile")
        public String submitProfile(@ModelAttribute Pharmacist pharmacist, @RequestParam("profilePicture") MultipartFile profilePicture) throws ExecutionException, InterruptedException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails = (MyUserDetails) auth.getPrincipal();
        pharmacist.setUserId(myUserDetails.getUsername());

        if (!profilePicture.isEmpty()) {
        
            byte[] profilePictureBytes = profilePicture.getBytes();
            String base64EncodedProfilePicture = Base64.getEncoder().encodeToString(profilePictureBytes);
            pharmacist.setProfilePicture(base64EncodedProfilePicture);
        }

        pharmacistService.updatePharmacist(pharmacist);
        return "redirect:/pharmacist/updateProfile";
    }   

    @GetMapping("/pharmacist/profilePicture/{userId}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getProfilePicture(@PathVariable String userId) {
       // Retrieve the profile picture data (base64-encoded string)
        // You might want to replace this with your actual logic to get the profile picture data
        String profilePictureData = "base64-encoded-image-data"; 

        Map<String, String> responseData = new HashMap<>();
        responseData.put("profilePicture", profilePictureData);

    return ResponseEntity.ok(responseData);
    }

    @GetMapping("/viewMedicineList")
    public String viewMedicineList(Model model) {
        try {
            List<Medicine> medicineList = pharmacistService.getListMedicine();
            model.addAttribute("medicineList", medicineList);
            return "viewMedicineList";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/editMedicine/{medId}")
    public String editMedicine(@PathVariable String medId, Model model) {
        try {
            Medicine medicine = pharmacistService.getMedicine(medId);
            model.addAttribute("medicine", medicine);
            return "editMedicine";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/updateMedicine/{medId}")
    public String updateMedicine(@PathVariable String medId, @ModelAttribute Medicine updatedMedicine, RedirectAttributes redirectAttributes) {
        try {
            updatedMedicine.setMedId(medId);
            String updateResult = pharmacistService.updateMedicine(updatedMedicine);
            LocalDateTime now = LocalDateTime.now();
    
            // Format the timestamp
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM dd yyyy HH:mm:ss");
            String formattedTimestamp = now.format(formatter);
    
            if (updateResult.equals("Medicine updated successfully")) {
                String successMessage = "Medicine updated successfully at " + formattedTimestamp;
                redirectAttributes.addFlashAttribute("successMessage", successMessage);
                return "redirect:/pharmacist/viewMedicineList";
            } else {
                String errorMessage = "Error updating medicine: " + updateResult;
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/pharmacist/editMedicine/" + medId;
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating medicine.");
            return "redirect:/pharmacist/editMedicine/" + medId;
        }
    }

    @DeleteMapping("/deleteMedicine/{medId}")
    public String deleteSelectedMedicine(@PathVariable("medIdToBeDelete") String medId, Model model) {
        try {
            // Call your service method to delete the medicine based on medId
            String deleteMessage = medicineService.deleteMedicine(medId);
    
            if (deleteMessage.startsWith("Error")) {
                // If deletion was unsuccessful, add an error message to the model
                model.addAttribute("errorMessage", deleteMessage);
            } else {
                // If deletion was successful, add a success message to the model
                model.addAttribute("successMessage", deleteMessage);
            }
    
            // Update the model with the appropriate lists
            List<Medicine> medicineList = medicineService.getMedicineList();
            model.addAttribute("medicineList", medicineList);
    
            // Debug statement to log the deletion message to the console
            System.out.println("Medicine Deletion Message: " + deleteMessage);
    
            return "viewMedicineList";  // Assuming this is the view for displaying medicine list
        } catch (Exception e) {
            // Log the exception for future reference
            e.printStackTrace();
    
            // Add the error message to the model
            String errorMessage = "Error deleting medicine. Please check the logs for more information.";
            model.addAttribute("errorMessage", errorMessage);
    
            // Debug statement to log the exception to the console
            System.out.println("Exception while deleting medicine: " + e.getMessage());
    
            // Return to the medicine list view with the error message
            return "viewMedicineList";  // Assuming this is the view for displaying medicine list
        }
    }

    @PostMapping("/addStock")
    public String addStock(@RequestParam String medId, @RequestParam int quantity, RedirectAttributes redirectAttributes) throws ExecutionException, InterruptedException {
        try {
            Medicine medicine = medicineService.getMedicine(medId);
            if (medicine != null) {
                medicine.setQuantity(medicine.getQuantity() + quantity);
                medicineService.updateMedicine(medicine);
                
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM dd yyyy HH:mm:ss");
                String formattedTimestamp = dateFormat.format(now);
                String successMessage = "Stock added successfully at " + formattedTimestamp;
    
                redirectAttributes.addFlashAttribute("successMessage", successMessage);
            }
            return "redirect:/pharmacist";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding stock.");
            return "redirect:/pharmacist";
        }
    }

    @GetMapping("/addMedicineForm")
    public String showAddMedicineForm(Model model) {
        model.addAttribute("newMedicine", new Medicine());
        return "addMedicineForm";
    }

    @PostMapping("/addMedicine")
    public String addMedicine(@ModelAttribute("newMedicine") Medicine newMedicine, RedirectAttributes redirectAttributes) {
        try {
            // Check if medicine with the same name already exists
            List<Medicine> existingMedicines = medicineService.getListMedicine();
    
            for (Medicine existingMedicine : existingMedicines) {
                if (Objects.equals(existingMedicine.getMedName(), newMedicine.getMedName())) {
                    String errorMessage = "Error adding medicine. Medicine with name '" + newMedicine.getMedName() + "' already exists.";
                    redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                    return "redirect:/pharmacist/viewMedicineList";
                }
            }
    
            // Call the service method to add the new medicine
            medicineService.createMedicine(newMedicine);
    
            // Get current timestamp for last update
            Date timestamp = new Date();
    
            // Format the timestamp
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM dd yyyy HH:mm:ss");
            String formattedTimestamp = dateFormat.format(timestamp);
    
            // Set timestamp as message too
            String message = "Medicine added successfully at " + formattedTimestamp;
    
            // Use RedirectAttributes to add flash attribute
            redirectAttributes.addFlashAttribute("successMessage", message);
    
            return "redirect:/pharmacist/viewMedicineList";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding medicine.");
            return "redirect:/pharmacist/viewMedicineList";
        }
    }
}