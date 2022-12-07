/**
 * @team Journeys
 * @file UserExperience.java
 * @date January 21st, 2022
 */

package com.journeys.main.model.projections;

import com.journeys.main.relationships.Experience;

import java.util.Set;

public interface UserExperience {
    String getUserName();
    Set<JourneyId> getJourneys();
    Set<Experience> getExperiences();

    interface JourneyId{
        String getId();
    }
}