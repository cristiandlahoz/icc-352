package com.zoolandia.app.features.waitingRoom.example;

import com.zoolandia.app.features.waitingRoom.service.WaitingRoomServiceImpl;
import com.zoolandia.app.features.waitingRoom.service.dto.WaitingRoomCreateDTO;

/**
 * Example demonstrating how the WaitingRoomServiceImpl would be used
 * with AutoForm in the AddPatientToWaitingRoomDialog component.
 */
public class WaitingRoomExample {
    
    public static void main(String[] args) {
        // Initialize the service
        WaitingRoomServiceImpl waitingRoomService = new WaitingRoomServiceImpl();
        
        // Example 1: High priority emergency
        WaitingRoomCreateDTO emergencyDto = new WaitingRoomCreateDTO(
            101L,                    // clientId
            202L,                    // petId
            "Emergency - Accident",  // reasonForVisit
            3,                       // priority (highest)
            "Dog hit by car, needs immediate attention"  // notes
        );
        
        Long emergencyId = waitingRoomService.save(emergencyDto);
        System.out.println("Emergency case saved with ID: " + emergencyId);
        
        // Example 2: Regular checkup
        WaitingRoomCreateDTO checkupDto = new WaitingRoomCreateDTO(
            103L,                    // clientId
            204L,                    // petId
            "Regular checkup",       // reasonForVisit
            1,                       // priority (lowest)
            null                     // notes (optional)
        );
        
        Long checkupId = waitingRoomService.save(checkupDto);
        System.out.println("Regular checkup saved with ID: " + checkupId);
        
        // Example 3: Vaccination appointment
        WaitingRoomCreateDTO vaccinationDto = new WaitingRoomCreateDTO(
            105L,                    // clientId
            206L,                    // petId
            "Vaccination",           // reasonForVisit
            2,                       // priority (medium)
            "Annual vaccination - already scheduled"  // notes
        );
        
        Long vaccinationId = waitingRoomService.save(vaccinationDto);
        System.out.println("Vaccination appointment saved with ID: " + vaccinationId);
        
        System.out.println("\nAll entries successfully saved to waiting room!");
        System.out.println("AutoForm can now render the form properly using the FormService interface.");
    }
}