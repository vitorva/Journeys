/**
 * @team Journeys
 * @file PointOfInterest.java
 * @date January 21st, 2022
 */

package com.journeys.main.model;

import com.journeys.main.model.projections.PointOfInterestExperiences;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Node("POI")
public class PointOfInterest {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    private String id;
    private String name;

    private String url;

    @Relationship(type = "LOCATED_AT", direction = Relationship.Direction.OUTGOING)
    private Coordinates coordinates;

    public PointOfInterestExperiences.PoiSimplified getSimplified() {
        return new PointOfInterestExperiences.PoiSimplified() {
            @Override
            public String getId() {
                return PointOfInterest.this.getId();
            }

            @Override
            public String getName() {
                return PointOfInterest.this.getName();
            }
        };
    }
}