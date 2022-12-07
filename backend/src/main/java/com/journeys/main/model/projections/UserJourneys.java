/**
 * @team Journeys
 * @file UserJourneys.java
 * @date January 21st, 2022
 */

package com.journeys.main.model.projections;

import java.util.Set;

public interface UserJourneys {
    String getUserName();
    Set<JourneyExperiences> getJourneys();
}