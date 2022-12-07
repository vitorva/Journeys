/**
 * @team Journeys
 * @file Coordinates.java
 * @date January 21st, 2022
 */

package com.journeys.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Node("Coordinates")
public class Coordinates {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;
    private Double lng;
    private Double lat;
}