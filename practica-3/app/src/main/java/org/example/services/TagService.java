package org.example.services;

import org.example.exceptions.NotFoundException;
import org.example.models.Tag;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TagService {

    private static final Map<Long, Tag> tags = new HashMap<>();

    public TagService() {

    }

    static {


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
    }

    /*public  <Tag> getAllTags() {
        return tags.values();
    }*/

    public static Collection<Tag> getAllTags() {
        return tags.values();
    }


    public Tag getTagById(Long tagId) {
        if (tagId == null) {
            throw new IllegalArgumentException("The argument 'tagId' cannot be null.");
        }
        if (!tags.containsKey(tagId)) {
            throw new IllegalArgumentException("Tag not found for the provided ID.");
        }
        return tags.get(tagId);
    }

    public Tag createTag(String name) {
        Tag tag = new Tag(name);
        tags.put(tag.getTagId(), tag);
        return tag;
    }


    public void updateTag(Tag tag) {
        if (tag == null)
            throw new IllegalArgumentException("Tag cannot be null");
        else if (!tags.containsKey(tag.getTagId()))
            throw new NotFoundException("Tag cannot be found");
        else {
            Tag myTag = tags.get(tag.getTagId());
            myTag.setName(tag.getName());
            tags.put(myTag.getTagId(), myTag);
        }
    }


    public void deleteTagById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        } else if (!tags.containsKey(id)) {
            throw new NotFoundException("Tag cannot be found");
        } else
            tags.remove(id);
    }

    public static List<Tag> getTagsByIds(List<Long> tagIds) {
        return getAllTags().stream()
                .filter(tag -> tagIds.contains(tag.getTagId()))
                .collect(Collectors.toList());
    }



}
