/**
 * @team Journeys
 * @file User.java
 * @date January 21st, 2022
 */

package com.journeys.main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.journeys.main.model.projections.JourneyExperiences;
import com.journeys.main.model.projections.UserDetails;
import com.journeys.main.model.projections.UserJourneys;
import com.journeys.main.relationships.Experience;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Node
public class User {

    @Id
    private String userName;
    private String firstName;
    private String lastName;
    private String password;
    private String email;

    @Relationship(type = "EXPERIENCE", direction = Relationship.Direction.OUTGOING)
    private Set<Experience> experiences;

    /**
     * List of journeys created by the user
     */
    @Relationship(type = "CREATED", direction = Relationship.Direction.OUTGOING)
    private Set<Journey> journeys;

    /**
     * List of POIS the user visited individually (not part of a journey)
     */
    @JsonIgnore
    public UserDetails getAsDetails() {
        return new UserDetails() {
            @Override
            public String getUserName() {
                return User.this.getUserName();
            }

            @Override
            public String getFirstName() {
                return User.this.getFirstName();
            }

            @Override
            public String getLastName() {
                return User.this.getLastName();
            }

            @Override
            public String getEmail() {
                return User.this.getEmail();
            }
        };
    }

    @JsonIgnore
    public UserJourneys getAsJourneys() {
        return new UserJourneys() {
            @Override
            public String getUserName() {
                return User.this.getUserName();
            }

            @Override
            public Set<JourneyExperiences> getJourneys() {
                return User.this.journeys.stream().map(Journey::getAsExperiences).collect(Collectors.toSet());
            }
        };
    }
}
