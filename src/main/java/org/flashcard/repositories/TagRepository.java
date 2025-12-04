package org.flashcard.repositories;

import org.flashcard.models.dataclasses.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    // Find all tags for a specific user
    List<Tag> findByUserId(Integer userId);

//    boolean existsByTagTitleAndUserId(String title, Integer userId);
}
