package org.foi.asankovic.spring5recipeapp.services;

import org.foi.asankovic.spring5recipeapp.domain.Recipe;

import java.util.Set;

/**
 * Created by Sankovic on 27.7.2021.
 */
public interface RecipeService {

    public Set<Recipe> getAllRecipes();
}
