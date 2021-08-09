package org.foi.asankovic.spring5recipeapp.controllers;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.foi.asankovic.spring5recipeapp.commands.RecipeCommand;
import org.foi.asankovic.spring5recipeapp.services.ImageService;
import org.foi.asankovic.spring5recipeapp.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Sankovic on 5.8.2021.
 */
@Controller
public class ImageController {

    private final ImageService imageService;
    private final RecipeService recipeService;

    public ImageController(ImageService imageService, RecipeService recipeService) {
        this.imageService = imageService;
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe/{recipeId}/image")
    public String showForm(Model model, @PathVariable String recipeId){
        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(recipeId)));
        return "recipe/imageUploadForm";
    }

    @PostMapping("/recipe/{recipeId}/image")
    public String handleImage(@PathVariable String recipeId, @RequestParam("imagefile") MultipartFile image){
        imageService.saveImage(Long.valueOf(recipeId), image);
        return "redirect:/recipe/" + recipeId + "/show";
    }

    @GetMapping("/recipe/{recipeId}/recipeImage")
    public void getImage(@PathVariable String recipeId, HttpServletResponse response) throws IOException {
        RecipeCommand command = recipeService.findCommandById(Long.valueOf(recipeId));
        byte[] bytes = null;

        if(command.getImage() != null){
            bytes = new byte[command.getImage().length];

            int i = 0;

            for (byte b : command.getImage())
                bytes[i++] = b;
        }

        response.setContentType("image/jpeg");
        if(bytes != null){
            InputStream is = new ByteArrayInputStream(bytes);

            IOUtils.copy(is, response.getOutputStream());
        }
    }
}
