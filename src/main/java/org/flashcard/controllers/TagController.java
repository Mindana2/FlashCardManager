package org.flashcard.controllers;

import org.flashcard.application.dto.TagDTO;
import org.flashcard.models.services.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/* We use Spring Data JPA to access the database.
 * This class is annotated with @Controller, which tells Spring
 * that it is a controller-layer component.
 * Spring automatically detects it and creates a bean in the application context,
 * so it can be injected wherever needed.(see main.java)
 */
@Controller
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


}
