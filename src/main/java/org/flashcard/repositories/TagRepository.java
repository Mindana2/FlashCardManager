package org.flashcard.repositories;

import org.flashcard.models.dataclasses.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Data access interface that provides specialized query methods for retrieving
 * tags by their associated user, abstracting the SQL logic via Spring Data JPA.
 */

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    List<Tag> findByUserId(Integer userId);
}
