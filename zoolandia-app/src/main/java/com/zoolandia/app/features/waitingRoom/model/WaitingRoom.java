package com.zoolandia.app.features.waitingRoom.model;

import java.time.LocalDateTime;

/**
 * Entity representing a waiting room entry.
 */
public class WaitingRoom {
    private Long id;
    private Long clientId;
    private Long petId;
    private String reasonForVisit;
    private Integer priority;
    private String notes;
    private LocalDateTime createdAt;
    
    public WaitingRoom() {
        this.createdAt = LocalDateTime.now();
    }
    
    public WaitingRoom(Long clientId, Long petId, String reasonForVisit, Integer priority, String notes) {
        this();
        this.clientId = clientId;
        this.petId = petId;
        this.reasonForVisit = reasonForVisit;
        this.priority = priority;
        this.notes = notes;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    
    public Long getPetId() { return petId; }
    public void setPetId(Long petId) { this.petId = petId; }
    
    public String getReasonForVisit() { return reasonForVisit; }
    public void setReasonForVisit(String reasonForVisit) { this.reasonForVisit = reasonForVisit; }
    
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}