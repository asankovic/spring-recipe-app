package org.foi.asankovic.spring5recipeapp.controllers;

import lombok.extern.slf4j.Slf4j;
import org.foi.asankovic.spring5recipeapp.commands.RecipeCommand;
import org.foi.asankovic.spring5recipeapp.exceptions.NotFoundException;
import org.foi.asankovic.spring5recipeapp.services.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * Created by Sankovic on 2.8.2021.
 */
@Slf4j
@Controller
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/{id}/show")
    public String showById(@PathVariable String id, Model model){
        model.addAttribute("recipe", recipeService.findById(Long.valueOf(id)));
        return "recipe/show";
    }

    @GetMapping("/new")
    public String addNewRecipe(Model model){
        model.addAttribute("recipe", new RecipeCommand());
        return "recipe/recipeForm";
    }

    @PostMapping
    public String saveOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand recipeCommand, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> {
                log.debug(objectError.toString());
            });
            return "recipe/recipeForm";
        }

        RecipeCommand savedRecipe = recipeService.saveRecipeCommand(recipeCommand);
        return "redirect:/recipe/"+ savedRecipe.getId()+ "/show";
    }

    @GetMapping("/{id}/update")
    public String updateRecipe(Model model, @PathVariable String id){
        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(id)));
        return "recipe/recipeForm";
    }

    @GetMapping("/{id}/delete")
    public String deleteRecipe(@PathVariable String id){
        log.debug("Deleting id: " + id);
        recipeService.deleteById(Long.valueOf(id));
        return "redirect:/";
    }

    //@ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFoundException(Exception ex){

        log.error("Handling not found exception!");
        log.error(ex.getMessage());
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("404error");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        modelAndView.addObject("exception", ex);

        return modelAndView;
    }
}
