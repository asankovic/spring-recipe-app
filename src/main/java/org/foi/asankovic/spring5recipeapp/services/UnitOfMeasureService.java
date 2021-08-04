package org.foi.asankovic.spring5recipeapp.services;

import org.foi.asankovic.spring5recipeapp.commands.UnitOfMeasureCommand;
import org.foi.asankovic.spring5recipeapp.domain.UnitOfMeasure;

import java.util.Set;

/**
 * Created by Sankovic on 4.8.2021.
 */
public interface UnitOfMeasureService {

    Set<UnitOfMeasureCommand> getAllUoms();

}
