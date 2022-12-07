/**
 * @team Journeys
 * @file JourneyTests.java
 * @date January 21st, 2022
 */

package com.journeys.main.model;

import com.journeys.main.relationships.Experience;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class JourneyTests {

    /**
     * Testing the correct behavior of the constructor
     */
    @Test
    public void JourneyTests_testingConstructor() {
        String id = "1", title = "My journey";
        Coordinates start = new Coordinates(), end = new Coordinates();
        Set<Experience> experienceSet = new HashSet<>();
        Journey journey = new Journey(id, title, start, end, experienceSet);

        Assertions.assertEquals(id, journey.getId());
        Assertions.assertEquals(title, journey.getTitle());
        Assertions.assertEquals(start, journey.getStart());
        Assertions.assertEquals(end, journey.getEnd());
        Assertions.assertEquals(experienceSet, journey.getExperiences());
    }

    /**
     * Testing the correct behavior of the getter/setter of id
     */
    @Test
    public void JourneyTests_testingGetSetId() {
        Journey journey = new Journey();
        String id = "1";
        journey.setId(id);
        Assertions.assertEquals(id, journey.getId());
    }

    /**
     * Testing the correct behavior of the getter/setter of title
     */
    @Test
    public void JourneyTests_testingGetSetTitle() {
        Journey journey = new Journey();
        String title = "1";
        journey.setTitle(title);
        Assertions.assertEquals(title, journey.getTitle());
    }

    /**
     * Testing the correct behavior of the getter/setter of start
     */
    @Test
    public void JourneyTests_testingGetSetStart() {
        Journey journey = new Journey();
        Coordinates start = new Coordinates();
        journey.setStart(start);
        Assertions.assertEquals(start, journey.getStart());
    }

    /**
     * Testing the correct behavior of the getter/setter of end
     */
    @Test
    public void JourneyTests_testingGetSetEnd() {
        Journey journey = new Journey();
        Coordinates end = new Coordinates();
        journey.setEnd(end);
        Assertions.assertEquals(end, journey.getEnd());
    }

    /**
     * Testing the correct behavior of the getter/setter of experience
     */
    @Test
    public void JourneyTests_testingGetSetExperience() {
        Journey journey = new Journey();
        Set<Experience> experiences = new HashSet<>();
        journey.setExperiences(experiences);
        Assertions.assertEquals(experiences, journey.getExperiences());
    }
}