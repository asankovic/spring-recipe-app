package org.foi.asankovic.spring5recipeapp.repositories;

import org.foi.asankovic.spring5recipeapp.domain.Recipe;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Sankovic on 27.7.2021.
 */
public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}
