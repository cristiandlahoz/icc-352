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
