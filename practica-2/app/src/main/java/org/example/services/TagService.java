package org.example.services;

import org.example.exceptions.NotFoundException;
import org.example.models.Tag;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TagService {

    private static final Map<Long, Tag> tags = new HashMap<>();

    public TagService() {

    }

    static {
        tags.put(1L, new Tag(1L, "Java"));
        tags.put(2L, new Tag(2L, "SQL"));
        tags.put(3L, new Tag(3L, "Spring Boot"));
        tags.put(4L, new Tag(4L, "JavaScript"));
        tags.put(5L, new Tag(5L, "Optimización"));
        tags.put(6L, new Tag(6L, "Seguridad"));
        tags.put(7L, new Tag(7L, "Web Development"));
        tags.put(8L, new Tag(8L, "Diseño de Software"));
        tags.put(9L, new Tag(9L, "SOLID"));
        tags.put(10L, new Tag(10L, "Docker"));
        tags.put(11L, new Tag(11L, "DevOps"));
        tags.put(12L, new Tag(12L, "Machine Learning"));
        tags.put(13L, new Tag(13L, "Python"));
        tags.put(14L, new Tag(14L, "API REST"));
        tags.put(15L, new Tag(15L, "TensorFlow"));
    }

    public Collection<Tag> getAllTags() {
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

    public void createTag(Tag tag) {
        tags.put(tag.getTagId(), tag);
    }

    public void updateTag(Tag tag) {
        if (tag == null)
            throw new IllegalArgumentException("Tag cannot be null");
        else if (!tags.containsKey(tag.getTagId()))
            throw new NotFoundException("Tag cannot be found");
        else {
            Tag myTag = tags.get(tag.getTagId());
            myTag.setTagName(tag.getTagName());
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

}
