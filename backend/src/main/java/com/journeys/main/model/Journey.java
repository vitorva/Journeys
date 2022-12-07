/**
 * @team Journeys
 * @file Journey.java
 * @date January 21st, 2022
 */

package com.journeys.main.model;

import com.journeys.main.model.projections.JourneyExperiences;
import com.journeys.main.model.projections.PointOfInterestExperiences;
import com.journeys.main.relationships.Experience;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Node
public class Journey {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    private String id;
    private String title;

    @Relationship(type = "START", direction = Relationship.Direction.OUTGOING)
    private Coordinates start;

    @Relationship(type = "END", direction = Relationship.Direction.OUTGOING)
    private Coordinates end;

    @Relationship(type = "EXPERIENCE", direction = Relationship.Direction.OUTGOING)
    private Set<Experience> experiences;

    public JourneyExperiences getAsExperiences() {
        return new JourneyExperiences() {
            @Override
            public String getId() {
                return Journey.this.getId();
            }

            @Override
            public String getTitle() {
                return Journey.this.getTitle();
            }

            @Override
            public Set<PointOfInterestExperiences> getExperiences() {
                return Journey.this.getExperiences().stream().map(Experience::getAsPOIExperience).collect(Collectors.toSet());
            }
        };
    }
}
