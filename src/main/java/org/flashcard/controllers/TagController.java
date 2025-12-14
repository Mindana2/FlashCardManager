package org.flashcard.controllers;

import org.flashcard.application.dto.TagDTO;
import org.flashcard.models.services.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Controller
@Transactional
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    public TagDTO createTag(Integer userId, String title, String color) {
        return tagService.createTag(userId, title, color);
    }

    public List<TagDTO> getTagsForUser(Integer userId) {
        return tagService.getTagsForUser(userId);
    }

    public String getTagText(Integer tagId) {
        return tagService.getTagText(tagId);
    }
}
