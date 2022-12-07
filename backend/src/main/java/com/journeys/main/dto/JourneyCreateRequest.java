/**
 * @team Journeys
 * @file JourneyCreateRequest.java
 * @date January 21st, 2022
 */

package com.journeys.main.dto;

import com.journeys.main.model.Coordinates;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JourneyCreateRequest {

    private String title;
    private List<String> orderedPOIsArray;
    private Coordinates start;
    private Coordinates end;

    /**
     * Check validity
     * @return true if valid, false otherwise
     */
    public boolean IsValid() {
        return title != null && orderedPOIsArray != null && orderedPOIsArray.size() > 0 && start != null && end != null;
    }
}