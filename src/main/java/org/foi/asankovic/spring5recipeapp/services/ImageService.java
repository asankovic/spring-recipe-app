package org.foi.asankovic.spring5recipeapp.services;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Sankovic on 5.8.2021.
 */
public interface ImageService {
    void saveImage(Long recipeId, MultipartFile image);
}
