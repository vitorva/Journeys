/**
 * @team Journeys
 * @file PointOfInterestExperiences.java
 * @date January 21st, 2022
 */

package com.journeys.main.model.projections;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A projection of an experience belonging to a point of interest
 */
public interface PointOfInterestExperiences {
    String getDescription();

    List<String> getImages();

    PoiSimplified getPointOfInterest();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    LocalDateTime getDate();

    interface PoiSimplified {
        String getId();
        String getName();
    }
}