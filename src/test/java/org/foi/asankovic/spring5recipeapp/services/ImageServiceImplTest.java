package org.foi.asankovic.spring5recipeapp.services;

import org.foi.asankovic.spring5recipeapp.domain.Recipe;
import org.foi.asankovic.spring5recipeapp.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;

    @InjectMocks
    ImageServiceImpl imageService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void saveImage() throws IOException {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Optional<Recipe> optionalRecipe = Optional.of(recipe);
        MultipartFile multipartFile = new MockMultipartFile("imagefile",
                "testing.txt", "text/plain",
                "Test file".getBytes());

        when(recipeRepository.findById(anyLong())).thenReturn(optionalRecipe);

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        imageService.saveImage(1L, multipartFile);

        verify(recipeRepository).findById(anyLong());
        verify(recipeRepository).save(argumentCaptor.capture());
        Recipe savedRecipe = argumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().length);

    }
}