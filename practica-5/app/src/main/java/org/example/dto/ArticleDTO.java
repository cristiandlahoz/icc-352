package org.example.dto;

import java.util.Date;
import java.util.List;

import org.example.models.Tag;

public record ArticleDTO(
    Long articleId,
    String title,
    String content,
    String author,
    List<Tag> tags,
    Date date) {
}
