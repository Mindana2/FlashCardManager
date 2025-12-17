package org.flashcard.models.services;

import org.flashcard.application.dto.TagDTO;
import org.flashcard.application.mapper.TagMapper;
import org.flashcard.models.dataclasses.Tag;
import org.flashcard.models.dataclasses.User;
import org.flashcard.repositories.TagRepository;
import org.flashcard.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/* We use Spring Data JPA to access the database.
 * This class is annotated with @Service, which tells Spring
 * that it is a service-layer component.
 * Spring automatically detects it and creates a bean in the application context,
 * so it can be injected wherever needed.(see main.java)
 * The @Transactional annotation ensures that no database transactions are left unfinished.
 * It automatically aborts any transactions that result in an error.
 * This allows us to write logic without manually handling database transactions.
 */
@Service
@Transactional
public class TagService {

    private final TagRepository tagRepo;
    private final UserRepository userRepo;

    public TagService(TagRepository tagRepo, UserRepository userRepo) {
        this.tagRepo = tagRepo;
        this.userRepo = userRepo;
    }

    public TagDTO createTag(Integer userId, String title, String color) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag title cannot be empty");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Tag tag = new Tag(title.trim(), color, user);
        Tag savedTag = tagRepo.save(tag);
        return TagMapper.toDTO(savedTag);
    }

    public List<TagDTO> getTagsForUser(Integer userId) {
        if (!userRepo.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }

        List<Tag> tags = tagRepo.findByUserId(userId);
        return TagMapper.toDTOList(tags);
    }

}

