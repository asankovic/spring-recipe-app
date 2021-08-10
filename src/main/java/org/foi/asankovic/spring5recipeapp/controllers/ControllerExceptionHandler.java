package org.foi.asankovic.spring5recipeapp.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Sankovic on 10.8.2021.
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(NumberFormatException.class)
    public ModelAndView handleNumberFormatException(Exception ex){

        log.error("Handling number format exception!");
        log.error(ex.getMessage());
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("400error");
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        modelAndView.addObject("exception", ex);

        return modelAndView;
    }
}
