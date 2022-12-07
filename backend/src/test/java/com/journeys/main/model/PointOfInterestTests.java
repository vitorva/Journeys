/**
 * @team Journeys
 * @file PointOfInterestTests.java
 * @date January 21st, 2022
 */

package com.journeys.main.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PointOfInterestTests {

    /**
     * Testing the correct behavior of the constructor
     */
    @Test
    public void PointOfInterestTests_testingConstructor() {
        String name = "Cathédrale de Lausanne", id = "1", url = "/temp.jpg";
        Coordinates coordinates = new Coordinates();

        PointOfInterest poi = new PointOfInterest(id, name, url, coordinates);

        Assertions.assertEquals(name, poi.getName());
        Assertions.assertEquals(id, poi.getId());
        Assertions.assertEquals(url, poi.getUrl());
        Assertions.assertEquals(coordinates, poi.getCoordinates());
    }

    /**
     * Testing the correct behavior of the getter/setter of id
     */
    @Test
    public void PointOfInterestTests_testingGetSetId() {
        PointOfInterest poi = new PointOfInterest();
        String id = "1";
        poi.setId(id);
        Assertions.assertEquals(id, poi.getId());
    }

    /**
     * Testing the correct behavior of the getter/setter of url
     */
    @Test
    public void PointOfInterestTests_testingGetSetUrl() {
        PointOfInterest poi = new PointOfInterest();
        String url = "/1_cathedrale_lausanne.png";
        poi.setUrl(url);
        Assertions.assertEquals(url, poi.getUrl());
    }

    /**
     * Testing the correct behavior of the getter/setter of coordinates
     */
    @Test
    public void PointOfInterestTests_testingGetSetCoordinates() {
        PointOfInterest poi = new PointOfInterest();
        Coordinates coordinates = new Coordinates();
        poi.setCoordinates(coordinates);
        Assertions.assertEquals(coordinates, poi.getCoordinates());
    }

    /**
     * Testing the correct behavior of the getter/setter of name
     */
    @Test
    public void PointOfInterestTests_testingGetSetName() {
        PointOfInterest poi = new PointOfInterest();
        String name = "Port d'Ouchy";
        poi.setName(name);
        Assertions.assertEquals(name, poi.getName());
    }
}