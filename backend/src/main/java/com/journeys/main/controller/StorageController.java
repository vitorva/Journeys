/**
 * @team Journeys
 * @file StorageController.java
 * @date January 21st, 2022
 */

package com.journeys.main.controller;

import com.journeys.main.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/image")
public class StorageController {

    StorageService storageService;

    /**
     * Constructor of StorageService
     * @param service
     */
    @Autowired
    StorageController(StorageService service) {
        this.storageService = service;
    }



    /**
     * Getting image from journey
     * @param journey_id the id of the journey
     * @param filename the name of the file
     * @return a Ressource
     */
    @GetMapping(value = "/journey/{journey_id}/{filename}",
            produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    @ResponseBody
    public Resource getImageFromJourney(@PathVariable String journey_id, @PathVariable String filename) {
        //throws Storage exception
        return storageService.loadAsRessource(journey_id,"", filename);
    }

    /**
     * Getting image from POI
     * @param poi_id the id of the POI
     * @param filename the name of the file
     * @return a Ressource
     */
    @GetMapping(value = "/poi/{poi_id}/{filename}",
            produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    @ResponseBody
    public Resource getImageFromPoi(@PathVariable String poi_id, @PathVariable String filename) {
        //throws Storage exception
        return storageService.loadAsRessource(poi_id,"", filename);
    }
}