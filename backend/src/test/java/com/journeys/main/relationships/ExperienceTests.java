/**
 * @team Journeys
 * @file ExperienceTests.java
 * @date January 21st, 2022
 */

package com.journeys.main.relationships;

import com.journeys.main.model.PointOfInterest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ExperienceTests {

    /**
     * Testing the correct behavior of the constructor
     */
    @Test
    public void ExperienceTests_testingConstructor() {
        long id = 1L;
        int order = 1;
        String description = "This is a description";
        PointOfInterest poi = new PointOfInterest();
        List<String> images = new ArrayList<>();

        Experience experience = new Experience(id, order, description, images, poi,null);

        Assertions.assertEquals(id, experience.getId());
        Assertions.assertEquals(order, experience.getOrder());
        Assertions.assertEquals(description, experience.getDescription());
        Assertions.assertEquals(poi, experience.getPointOfInterest());
        Assertions.assertEquals(images, experience.getImages());
    }

    /**
     * Testing the correct behavior of the getter/setter of id
     */
    @Test
    public void ExperienceTests_testingGetSetId() {
        Experience experience = new Experience();
        long id = 1L;
        experience.setId(id);
        Assertions.assertEquals(id, experience.getId());
    }

    /**
     * Testing the correct behavior of the getter/setter of order
     */
    @Test
    public void ExperienceTests_testingGetSetOrder() {
        Experience experience = new Experience();
        int order = 1;
        experience.setOrder(order);
        Assertions.assertEquals(order, experience.getOrder());
    }

    /**
     * Testing the correct behavior of the getter/setter of description
     */
    @Test
    public void ExperienceTests_testingGetSetDescription() {
        Experience experience = new Experience();
        String description = "Description";
        experience.setDescription(description);
        Assertions.assertEquals(description, experience.getDescription());
    }

    /**
     * Testing the correct behavior of the getter/setter of images
     */
    @Test
    public void ExperienceTests_testingGetSetImages() {
        Experience experience = new Experience();
        List<String> images = new ArrayList<>();
        experience.setImages(images);
        Assertions.assertEquals(images, experience.getImages());
    }

    /**
     * Testing the correct behavior of the getter/setter of point of interest
     */
    @Test
    public void ExperienceTests_testingGetSetPOI() {
        Experience experience = new Experience();
        PointOfInterest poi = new PointOfInterest();
        experience.setPointOfInterest(poi);
        Assertions.assertEquals(poi, experience.getPointOfInterest());
    }
}