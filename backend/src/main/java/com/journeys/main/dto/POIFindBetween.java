/**
 * @team Journeys
 * @file POIFindBetween.java
 * @date January 21st, 2022
 */

package com.journeys.main.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class POIFindBetween {
    private double startLat;
    private double startLng;
    private double destLat;
    private double destLng;

    private double radius;
}