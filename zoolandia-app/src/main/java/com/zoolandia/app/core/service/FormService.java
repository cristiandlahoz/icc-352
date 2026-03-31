package com.zoolandia.app.core.service;

/**
 * Generic interface for form services that handle CRUD operations.
 * 
 * @param <T> the DTO type for creating/updating forms
 * @param <ID> the ID type for the form entities
 */
public interface FormService<T, ID> {
    
    /**
     * Save a new form using the provided DTO.
     * 
     * @param dto the data transfer object containing form data
     * @return the ID of the saved form
     */
    ID save(T dto);
}