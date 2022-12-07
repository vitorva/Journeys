package com.journeys.main.repositories;

import com.journeys.main.model.Journey;
import com.journeys.main.model.projections.JourneyExperiences;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface JourneyRepository extends Neo4jRepository<Journey,String> {

    /**
     * Query getting experiences from a journey
     * @param id the id of the journey
     * @return a collection of JourneyExperience
     */
    @Query(
            "MATCH (coords:Coordinates)<-[l:LOCATED_AT]-(poi:POI)-[r:EXPERIENCE]-(j:Journey{id:$id}) WITH j,poi,r ORDER BY r.order\n" +
            "RETURN j,collect(r),collect(poi)"
    )
    Collection<JourneyExperiences> getExperiencesFromJourney(String id);

    /**
     * Query getting experiences from a point of interest
     * @param id the id of the POI
     * @return a collection of JourneyExperience
     */
    @Query(
            "MATCH (coords:Coordinates)<-[l:LOCATED_AT]-(poi:POI{id:$id})-[r:EXPERIENCE]-(j:Journey)\n" +
            "RETURN j,r,poi ORDER BY j.title"
    )
    Collection<JourneyExperiences> getExperiencesFromPoi(String id);

    /**
     * Finding the journey's creator by the id of the journey
     * @param id
     * @return
     */
    @Query(
            "MATCH (u:User)-[r:CREATED]->(j:Journey{id:$id}) RETURN u.userName"
    )
    Optional<String> findJourneyCreator(String id);

    /**
     * Query getting experiences
     * @param poiId the id of the point of interest
     * @param journeyId the id of the journey
     * @return a JourneyExperience object
     */
    @Query(
            "MATCH (poi:POI{id:$poiId})<-[r:EXPERIENCE]-(j:Journey{id:$journeyId}) return poi, r, j"
    )
    JourneyExperiences getExperience(String poiId, String journeyId);

    /**
     * Query getting a single experience
     * @param userName the username of the User
     * @param poiId the id of the point of interest
     * @return 0 if experience does not exist, else more
     */
    @Query(
            "MATCH (poi:POI{id:$poiId})<-[r:EXPERIENCE]-(u:User{userName:$userName}) return count(r)"
    )
    Integer getSingleExperience(String userName, String poiId);

    /**
     * Query updating the experience from a journey
     * @param poiId the id of the point of interest
     * @param journeyId the id of the journey
     * @param description the description of the experiences
     * @param imageName a list of filename of images
     * @return
     */
    @Query(
            "MATCH (poi:POI{id:$poiId})-[r:EXPERIENCE]-(j:Journey{id:$journeyId})\n" +
            "SET r.description = $description\n" +
            "SET r.images = $imageName\n" +
            "SET r.date = $date\n" +
            "return j, r, poi"
    )
    JourneyExperiences updateExperience(String poiId, String journeyId, String description, List<String> imageName, LocalDateTime date);

    /**
     *
     * @param idJourney
     * @param idPoi
     * @return
     */
    @Query(
            "MATCH (j:Journey{id:$idJourney})-[e:EXPERIENCE]->(p:POI{id:$idPoi}) return count(e)"
    )
    Integer checkExistenceExperienceFromJourney(String idJourney, String idPoi);

    /**
     * Query checking if relation existe between POI and user
     * @param userName the username of the user
     * @param idPoi the id of the point of interest
     * @return 1 if exist, 0 else
     */
    @Query(
            "MATCH (u:User{userName:$userName})-[e:EXPERIENCE]->(p:POI{id:$idPoi}) return count(e)"
    )
    Integer checkExistenceSingleExperience(String userName, String idPoi);

    /**
     * Deleting an experience from a journey
     * @param idJourney the id of the journey
     * @param idPoi the id of the POI
     */
    @Query(
            "MATCH (j:Journey{id:$idJourney})-[e:EXPERIENCE]->(p:POI{id:$idPoi}) delete e"
    )
    void deleteExperienceFromJourney(String idJourney, String idPoi);

    /**
     * Deleting a single experience
     * @param userName the username of the User
     * @param idPoi the id of the POI
     */
    @Query(
            "MATCH (u:User{userName:$userName})-[e:EXPERIENCE]->(p:POI{id:$idPoi}) delete e"
    )
    void deleteExperience(String userName, String idPoi);
}