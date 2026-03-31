package com.zoolandia.app.features.waitingRoom.service;

import com.zoolandia.app.features.waitingRoom.service.dto.WaitingRoomCreateDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WaitingRoomServiceImplTest {

    @Test
    public void testSaveWaitingRoomEntry() {
        // Arrange
        WaitingRoomServiceImpl service = new WaitingRoomServiceImpl();
        WaitingRoomCreateDTO dto = new WaitingRoomCreateDTO(
            1L,           // clientId
            2L,           // petId
            "Checkup",    // reasonForVisit
            2,            // priority
            "Regular checkup for vaccination" // notes
        );
        
        // Act
        Long id = service.save(dto);
        
        // Assert
        assertNotNull(id);
        assertTrue(id > 0);
    }
    
    @Test
    public void testSaveNullDto() {
        // Arrange
        WaitingRoomServiceImpl service = new WaitingRoomServiceImpl();
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> service.save(null));
    }
}