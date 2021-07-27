package org.foi.asankovic.spring5recipeapp.repositories;

import org.foi.asankovic.spring5recipeapp.domain.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by Sankovic on 27.7.2021.
 */
public interface CategoryRepository extends CrudRepository<Category, Long> {

    Optional<Category> findByDescription(String description);
}
