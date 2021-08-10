package org.foi.asankovic.spring5recipeapp.controllers;

import org.foi.asankovic.spring5recipeapp.commands.RecipeCommand;
import org.foi.asankovic.spring5recipeapp.domain.Recipe;
import org.foi.asankovic.spring5recipeapp.services.ImageService;
import org.foi.asankovic.spring5recipeapp.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    @Mock
    RecipeService recipeService;

    @Mock
    ImageService imageService;

    @InjectMocks
    ImageController imageController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(imageController)
                .setControllerAdvice(ControllerExceptionHandler.class)
                .build();
    }

    @Test
    void showForm() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

        mockMvc.perform(get("/recipe/1/image"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/imageUploadForm"))
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService).findCommandById(anyLong());
    }

    @Test
    void handleImage() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("imagefile",
                "testing.txt", "text/plain",
                "Test file".getBytes());

        mockMvc.perform(multipart("/recipe/1/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/recipe/1/show"));

        verify(imageService).saveImage(anyLong(), any());
    }

    @Test
    void getImage() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId(1L);

        String s = "Test text";
        Byte[] bytes = new Byte[s.getBytes().length];

        int i = 0;

        for (byte b : s.getBytes())
            bytes[i++] = b;

        command.setImage(bytes);

        when(recipeService.findCommandById(anyLong())).thenReturn(command);

        MockHttpServletResponse response = mockMvc.perform(get("/recipe/1/recipeImage"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertEquals(response.getContentAsByteArray().length, s.getBytes().length);
    }

    @Test
    void getImageNumberFormatException() throws Exception {

        mockMvc.perform(get("/recipe/asdf/recipeImage"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }
}