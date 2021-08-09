package org.foi.asankovic.spring5recipeapp.services;

import org.foi.asankovic.spring5recipeapp.commands.IngredientCommand;

/**
 * Created by Sankovic on 4.8.2021.
 */
public interface IngredientService {

    IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);

    IngredientCommand saveIngredientCommand(IngredientCommand command);

    void deleteById(Long recipeId, Long ingredientId);
}
