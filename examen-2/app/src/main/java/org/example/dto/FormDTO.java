package org.example.dto;

public record FormDTO(
    String username,
    String fullname,
    String sector,
    String education,
    Double latitude,
    Double longitude,
    Boolean isSynchronized) {}
