/**
 * @team Journeys
 * @file Experience.java
 * @date January 21st, 2022
 */

package com.journeys.main.relationships;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.journeys.main.model.PointOfInterest;
import com.journeys.main.model.projections.PointOfInterestExperiences;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.driver.internal.shaded.reactor.util.annotation.Nullable;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RelationshipProperties
public class Experience {
    @Id
    @GeneratedValue
    private Long id;
    private int order;

    @Nullable
    private String description;
    @Nullable
    private List<String> images;

    @TargetNode
    private PointOfInterest pointOfInterest;

    @Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDateTime  date;

    @JsonIgnore
    public PointOfInterestExperiences getAsPOIExperience() {
        return new PointOfInterestExperiences() {
            @Override
            public String getDescription() {
                return Experience.this.getDescription();
            }

            @Override
            public List<String> getImages() {
                return Experience.this.getImages();
            }

            @Override
            public PoiSimplified getPointOfInterest() {
                return Experience.this.getPointOfInterest().getSimplified();
            }

            @Override
            public LocalDateTime getDate() {
                return Experience.this.getDate();
            }
        };
    }
}