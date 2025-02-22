package org.example.services;

import org.example.exceptions.NotFoundException;
import org.example.models.Tag;

import java.util.*;
import java.util.stream.Collectors;

import org.example.repository.ArticleRepository;
import org.example.repository.TagRepository;

public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {

        this.tagRepository = tagRepository;
    }

    /*static {


        tags.put(1L, new Tag( "Java"));
        tags.put(2L, new Tag( "SQL"));
        tags.put(3L, new Tag( "Spring Boot"));
        tags.put(4L, new Tag( "JavaScript"));
        tags.put(5L, new Tag("Seguridad"));
        tags.put(7L, new Tag( "Web Development"));
        tags.put(8L, new Tag( "Diseño de Software"));
        tags.put(9L, new Tag( "SOLID"));
        tags.put(10L, new Tag( "Docker"));
        tags.put(11L, new Tag( "DevOps"));
        tags.put(12L, new Tag( "Machine Learning"));
        tags.put(13L, new Tag( "Python"));
        tags.put(14L, new Tag( "API REST"));
        tags.put(15L, new Tag( "TensorFlow"));
    }*/

    /*public  <Tag> getAllTags() {
        return tags.values();
    }*/

    public Collection<Tag> getAllTags() {
        return tagRepository.findAll();
    }


    public Optional<Tag> getTagById(Long tagId) {
        if (tagId == null) {
            throw new IllegalArgumentException("Tag ID cannot be null");
        }
        return tagRepository.findById(tagId);
    }

    public Tag createTag(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tag name cannot be null or empty");
        }
        Tag tag = new Tag(null, name);
        return tagRepository.save(tag);
    }


    public Tag updateTag(Tag tag) {
        if (tag == null || tag.getTagId() == null) {
            throw new IllegalArgumentException("Tag and ID cannot be null");
        }
        Optional<Tag> existingTag = tagRepository.findById(tag.getTagId());
        if (existingTag.isEmpty()) {
            throw new NotFoundException("Tag not found");
        }
        return tagRepository.update(tag);
    }



    public void deleteTagById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Tag ID cannot be null");
        }
        tagRepository.deleteById(id);
    }


    public List<Tag> getTagsByIds(List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            throw new IllegalArgumentException("Tag ID list cannot be null or empty");
        }
        return tagRepository.findAll().stream()
                .filter(tag -> tagIds.contains(tag.getTagId()))
                .collect(Collectors.toList());
    }



}
