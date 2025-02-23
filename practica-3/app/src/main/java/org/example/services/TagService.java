package org.example.services;

import java.util.*;
import java.util.stream.Collectors;
import org.example.exceptions.NotFoundException;
import org.example.models.Tag;
import org.example.repository.TagRepository;

public class TagService {

  private final TagRepository tagRepository;

  public TagService(TagRepository tagRepository) {

    this.tagRepository = tagRepository;
  }

  public Collection<Tag> getAllTags() {
    return tagRepository.findAll();
  }

  public Optional<Tag> getTagById(Long tagId) {
    if (tagId == null) {
      throw new IllegalArgumentException("Tag ID cannot be null");
    }
    return tagRepository.findById(tagId);
  }

  public Tag getTagByName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Name cannot be null");
    } else return tagRepository.findByName(name);
  }

  public Tag createTag(String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Tag name cannot be null or empty");
    } else if (tagRepository.findByName(name) != null) {
      return tagRepository.findByName(name);
    } else {
      Tag tag = new Tag(name);
      return tagRepository.save(tag);
    }
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
