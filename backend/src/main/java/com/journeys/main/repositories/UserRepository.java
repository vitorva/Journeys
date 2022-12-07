/**
 * @team Journeys
 * @file UserRepository.java
 * @date January 21st, 2022
 */

package com.journeys.main.repositories;

import com.journeys.main.model.User;
import com.journeys.main.model.projections.UserDetails;
import com.journeys.main.model.projections.UserExperience;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends Neo4jRepository<User, String> {

    /**
     * Finding a user by its username
     * @param s the username
     * @return the UserDetails object
     */
    Optional<UserDetails> findByUserName(String s);

    /**
     * Query updating a single experience
     * @param poiId the id of the point of interest
     * @param userName the username of the User
     * @param description the description of the experience
     * @param imageNames a list of name of files
     * @return
     */
    @Query(
            "MATCH (poi:POI{id:$poiId})-[r:EXPERIENCE]-(u:User{userName:$userName})\n" +
            "SET r.description = $description\n" +
            "SET r.images = $imageNames\n" +
            "return u, r, poi"
    )
    UserExperience updateSingleExperience(String poiId, String userName, String description, List<String> imageNames);

    /**
     * Query getting a single experience
     * @param userName the username
     * @return A list of UserExperience
     */
    @Query(
            "MATCH p=(user:User{userName:$userName})-[*0..1]->(:POI)-[:LOCATED_AT]-(:Coordinates) RETURN p LIMIT 25"
    )
    Collection<UserExperience> getSingleExperiencesFrom(String userName);
}
