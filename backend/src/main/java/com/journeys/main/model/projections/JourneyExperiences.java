/**
 * @team Journeys
 * @file JourneyExperiences.java
 * @date January 21st, 2022
 */

package com.journeys.main.model.projections;

import java.util.Set;

/**
 * A projection of journeys containing
 * Experiences
 */
public interface JourneyExperiences {
    String getId();

    String getTitle();

    Set<PointOfInterestExperiences> getExperiences();
}