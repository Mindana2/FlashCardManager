package org.flashcard.repositories;

import org.flashcard.models.dataclasses.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * This interface is a Spring Data JPA repository for the Deck entity.
 *
 * This is where a lot of magic happens with Spring Data JPA:
 *
 * @Repository marks this interface as a Spring-managed repository.
 * Spring automatically detects it and provides an implementation at runtime.
 *
 * The repository automatically provides access to standard CRUD operations:
 *   save(), findById(), findAll(), delete(), etc., without writing any SQL.
 *
 * Spring AUTOGENERATES SQL queries based on method names! For example:
 *  findByUserId(userId)     :  Returns a list of all tags belonging to a specific user.
 *
 * Very cool
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    // Find all tags for a specific user
    List<Tag> findByUserId(Integer userId);

//    boolean existsByTagTitleAndUserId(String title, Integer userId);
}
