package org.foi.asankovic.spring5recipeapp.services;

import lombok.extern.slf4j.Slf4j;
import org.foi.asankovic.spring5recipeapp.domain.Recipe;
import org.foi.asankovic.spring5recipeapp.repositories.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by Sankovic on 5.8.2021.
 */
@Slf4j
@Service
public class ImageServiceImpl implements ImageService{

    private final RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void saveImage(Long recipeId, MultipartFile image) {
        try {
                Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

                if(optionalRecipe.isPresent()){
                    Recipe recipe = optionalRecipe.get();

                    Byte[] bytes = new Byte[image.getBytes().length];

                    int i = 0;

                    for(byte b : image.getBytes())
                        bytes[i++] = b;

                    recipe.setImage(bytes);

                    recipeRepository.save(recipe);
                }
            } catch (IOException e) {
                     e.printStackTrace();
            }
    }
}
