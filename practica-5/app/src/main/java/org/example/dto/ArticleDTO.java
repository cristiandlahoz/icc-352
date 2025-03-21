package org.example.dto;

import java.util.Date;
import java.util.List;

import org.example.models.Tag;

public record ArticleDTO(
    String title,
    String content,
    String author,
    List<Tag> tags,
    Date date) {
}
