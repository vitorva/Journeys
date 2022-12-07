/**
 * @team Journeys
 * @file CoordinatesTests.java
 * @date January 21st, 2022
 */

package com.journeys.main.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CoordinatesTests {

    /**
     * Testing the correct behavior of the constructor
     */
    @Test
    public void CoordinatesTests_testingConstructor() {
        long id = 1L;
        double lat = 46.34532, lng = 6.456345;
        Coordinates coordinates = new Coordinates(id, lng, lat);

        Assertions.assertEquals(id, coordinates.getId());
        Assertions.assertEquals(lat, coordinates.getLat());
        Assertions.assertEquals(lng, coordinates.getLng());
    }

    /**
     * Testing the correct behavior of the getter/setter of id
     */
    @Test
    public void CoordinatesTests_testingGetSetId() {
        Coordinates poi = new Coordinates();
        long id = 1L;
        poi.setId(id);
        Assertions.assertEquals(id, poi.getId());
    }

    /**
     * Testing the correct behavior of the getter/setter of latitude
     */
    @Test
    public void CoordinatesTests_testingGetSetLat() {
        Coordinates poi = new Coordinates();
        double lat = 45.46435;
        poi.setLat(lat);
        Assertions.assertEquals(lat, poi.getLat());
    }

    /**
     * Testing the correct behavior of the getter/setter of longitude
     */
    @Test
    public void CoordinatesTests_testingGetSetLng() {
        Coordinates poi = new Coordinates();
        double lng = 6.435353;
        poi.setLng(lng);
        Assertions.assertEquals(lng, poi.getLng());
    }
}