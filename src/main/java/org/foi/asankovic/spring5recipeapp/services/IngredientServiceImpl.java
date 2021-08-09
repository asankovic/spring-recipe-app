package org.foi.asankovic.spring5recipeapp.services;

import lombok.extern.slf4j.Slf4j;
import org.foi.asankovic.spring5recipeapp.commands.IngredientCommand;
import org.foi.asankovic.spring5recipeapp.converters.IngredientCommandToIngredient;
import org.foi.asankovic.spring5recipeapp.converters.IngredientToIngredientCommand;
import org.foi.asankovic.spring5recipeapp.domain.Ingredient;
import org.foi.asankovic.spring5recipeapp.domain.Recipe;
import org.foi.asankovic.spring5recipeapp.repositories.RecipeRepository;
import org.foi.asankovic.spring5recipeapp.repositories.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Sankovic on 4.8.2021.
 */
@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService{

    private final RecipeRepository recipeRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    public IngredientServiceImpl(RecipeRepository recipeRepository, IngredientToIngredientCommand ingredientToIngredientCommand, UnitOfMeasureRepository unitOfMeasureRepository, IngredientCommandToIngredient ingredientCommandToIngredient) {
        this.recipeRepository = recipeRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

        if(optionalRecipe.isEmpty()){
            //todo throw error
            log.error("recipe id not found. Id: " + recipeId);
        }

        Recipe recipe = optionalRecipe.get();

        Optional<IngredientCommand> optionalIngredientCommand = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredient -> ingredientToIngredientCommand.convert(ingredient)).findFirst();

        if(optionalIngredientCommand.isEmpty()){
            //todo throw error
            log.error("recipe id not found. Id: " + recipeId);
        }

        return optionalIngredientCommand.get();
    }

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(command.getRecipeId());

        if(optionalRecipe.isEmpty()){
            log.error("Recipe not found for id: " + command.getRecipeId());
            return new IngredientCommand();
        }
        Recipe recipe = optionalRecipe.get();

        Optional<Ingredient> optionalIngredient = recipe.getIngredients()
                .stream().filter(ingredient -> ingredient.getId().equals(command.getId())).findFirst();

        if(optionalIngredient.isPresent()){
            //update
            Ingredient existingIngredient = optionalIngredient.get();
            existingIngredient.setDescription(command.getDescription());
            existingIngredient.setAmount(command.getAmount());
            existingIngredient.setUom(unitOfMeasureRepository
                    .findById(command.getUom().getId())
                    .orElseThrow(() -> new RuntimeException("UOM NOT FOUND"))); //todo
        }
        else {
            //new
            recipe.addIngredient(ingredientCommandToIngredient.convert(command));
        }

        Recipe savedRecipe = recipeRepository.save(recipe);

        Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                .filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId()))
                .findFirst();

        //check by description
        if(!savedIngredientOptional.isPresent()){
            //not totally safe
            savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
                    .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
                    .filter(recipeIngredients -> recipeIngredients.getUom().getId().equals(command.getUom().getId()))
                    .findFirst();
        }

        //todo check for fail
        return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
    }

    @Override
    public void deleteById(Long recipeId, Long ingredientId) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

        if(optionalRecipe.isEmpty()){
            log.error("Recipe not found for id: " + recipeId);
            return;
        }
        Recipe recipe = optionalRecipe.get();
        Optional<Ingredient> optionalIngredient = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .findFirst();

        if(optionalIngredient.isPresent()){
            Ingredient ingredient = optionalIngredient.get();
            ingredient.setRecipe(null);
            recipe.getIngredients().remove(ingredient);
            recipeRepository.save(recipe);
        }
        else {
            log.debug("Recipe Id Not found. Id:" + recipeId);
        }
    }
}
