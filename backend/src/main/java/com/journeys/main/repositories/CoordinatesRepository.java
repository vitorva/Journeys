/**
 * @team Journeys
 * @file CoordinatesRepository.java
 * @date January 21st, 2022
 */

package com.journeys.main.repositories;

import com.journeys.main.model.Coordinates;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.Optional;

public interface CoordinatesRepository extends Neo4jRepository<Coordinates, Long> {

    /**
     * Checking if coordinates exist
     * @param lat the latitude of Coordinate
     * @param lng the longitude of Coordinate
     * @return the Coordinate object
     */
    @Query(
            "MATCH (c:Coordinates{lat:$lat,lng:$lng}) RETURN c LIMIT 1"
    )
    Optional<Coordinates> findExisting(double lat, double lng);
}