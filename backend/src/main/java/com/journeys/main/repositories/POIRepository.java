/**
 * @team Journeys
 * @file POIRepository.java
 * @date January 21st, 2022
 */

package com.journeys.main.repositories;

import com.journeys.main.model.PointOfInterest;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface POIRepository extends Neo4jRepository<PointOfInterest, String> {

    /**
     * Getting every POIs between two coordinates
     * @param fstLat the latitude of the first coordinate
     * @param fstLng the longitude of the first coordinate
     * @param sndLat the latitude of the second coordinate
     * @param sndLng the longitude of the second coordinate
     * @return a list of point of interests
     */
    @Query(value =
            "MATCH (p:POI) -[l:LOCATED_AT]-> (coordinates:Coordinates) \n" +
                    " WHERE \n" +
                    " coordinates.lat <= $fstLat AND \n" +
                    " coordinates.lng >= $fstLng AND \n" +
                    " coordinates.lat >= $sndLat AND \n" +
                    " coordinates.lng <= $sndLng \n" +
                    " RETURN p,l,coordinates ORDER BY p.name")
    Collection<PointOfInterest> getBetween(
            Double fstLat,
            Double fstLng,
            Double sndLat,
            Double sndLng);

    /**
     * Query getting every POI
     * @return the list of POI
     */
    @Query(value =
            "MATCH (poi:POI) -[l:LOCATED_AT] -> (coordinates:Coordinates) RETURN poi, l, coordinates ORDER BY poi.name"
    )
    Collection<PointOfInterest> getPOI();

    /**
     * Query get a POI
     * @param uuid the unique ID of the point of interest
     * @return a PointOfInterest
     */
    @Query(value =
            "MATCH (poi:POI{id:$uuid}) -[l:LOCATED_AT] -> (coordinates:Coordinates) RETURN poi, l, coordinates"
    )
    Optional<PointOfInterest> getSinglePoi(String uuid);
}