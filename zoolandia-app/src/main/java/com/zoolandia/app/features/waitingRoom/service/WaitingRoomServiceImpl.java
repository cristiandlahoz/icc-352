package com.zoolandia.app.features.waitingRoom.service;

import com.zoolandia.app.core.service.FormService;
import com.zoolandia.app.features.waitingRoom.service.dto.WaitingRoomCreateDTO;
import com.zoolandia.app.features.waitingRoom.model.WaitingRoom;

/**
 * Implementation of FormService for WaitingRoom management.
 * Handles the creation and management of waiting room entries.
 */
public class WaitingRoomServiceImpl implements FormService<WaitingRoomCreateDTO, Long> {
    
    // Field declarations
    private static Long nextId = 1L;
    
    /**
     * Save a new waiting room entry using the provided DTO.
     * 
     * @param dto the data transfer object containing waiting room data
     * @return the ID of the saved waiting room entry
     */
    @Override
    public Long save(WaitingRoomCreateDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("WaitingRoomCreateDTO cannot be null");
        }
        
        // Create new WaitingRoom entity from DTO
        WaitingRoom waitingRoom = new WaitingRoom(
            dto.clientId(), 
            dto.petId(), 
            dto.reasonForVisit(), 
            dto.priority(), 
            dto.notes()
        );
        
        // Simulate saving to database by assigning an ID
        waitingRoom.setId(nextId++);
        
        // Return the generated ID
        return waitingRoom.getId();
    }
}