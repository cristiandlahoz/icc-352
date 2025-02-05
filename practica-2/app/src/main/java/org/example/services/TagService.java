package org.example.services;

import org.example.models.Tag;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TagService {

    private static final Map<Long, Tag> tags = new HashMap<>();
    public TagService() {

    }

    static { }

    public Collection<Tag> getAllTags() {
        return tags.values();
    }

    public void createTag(Tag tag) {
        tags.put(tag.getTagId(), tag);
    }

    public void deleteTagById(Long id) {
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

}
