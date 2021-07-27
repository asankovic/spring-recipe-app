package org.foi.asankovic.spring5recipeapp.controllers;

import org.foi.asankovic.spring5recipeapp.domain.Category;
import org.foi.asankovic.spring5recipeapp.domain.UnitOfMeasure;
import org.foi.asankovic.spring5recipeapp.repositories.CategoryRepository;
import org.foi.asankovic.spring5recipeapp.repositories.UnitOfMeasureRepository;
import org.foi.asankovic.spring5recipeapp.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
public class IndexController {

    private RecipeService recipeService;

    @Autowired
    public IndexController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"","/","/index"})
    public String index(Model model){
        model.addAttribute("recipes", recipeService.getAllRecipes());
        return "index";
    }
}
