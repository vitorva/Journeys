/**
 * @team Journeys
 * @file UserTests.java
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
public class UserTests {

    /**
     * Testing the correct behavior of the constructor
     */
    @Test
    public void UserTests_testingConstructor() {
        String firstname = "Jean",
                lastname = "Dupont",
                username = "JDupon",
                password = "passw0rd",
                email = "jdupont@email.com";
        Set<Experience> experiences = new HashSet<>();
        Set<Journey> journeys = new HashSet<>();
        User user = new User(username, firstname, lastname, password, email, experiences, journeys);

        Assertions.assertEquals(email, user.getEmail());
        Assertions.assertEquals(firstname, user.getFirstName());
        Assertions.assertEquals(lastname, user.getLastName());
        Assertions.assertEquals(password, user.getPassword());
        Assertions.assertEquals(username, user.getUserName());
        Assertions.assertEquals(journeys, user.getJourneys());
    }

    /**
     * Testing the correct behavior of the getter/setter of username
     */
    @Test
    public void UserTests_testingGetSetUsername() {
        User user = new User();
        String username = "username";
        user.setUserName(username);
        Assertions.assertEquals(username, user.getUserName());
    }

    /**
     * Testing the correct behavior of the getter/setter of firstname
     */
    @Test
    public void UserTests_testingGetSetFirstname() {
        User user = new User();
        String firstname = "firstname";
        user.setFirstName(firstname);
        Assertions.assertEquals(firstname, user.getFirstName());
    }

    /**
     * Testing the correct behavior of the getter/setter of lastname
     */
    @Test
    public void UserTests_testingGetSetLastname() {
        User user = new User();
        String lastname = "lastname";
        user.setLastName(lastname);
        Assertions.assertEquals(lastname, user.getLastName());
    }

    /**
     * Testing the correct behavior of the getter/setter of email
     */
    @Test
    public void UserTests_testingGetSetEmail() {
        User user = new User();
        String email = "email";
        user.setEmail(email);
        Assertions.assertEquals(email, user.getEmail());
    }

    /**
     * Testing the correct behavior of the getter/setter of password
     */
    @Test
    public void UserTests_testingGetSetPassword() {
        User user = new User();
        String password = "password";
        user.setPassword(password);
        Assertions.assertEquals(password, user.getPassword());
    }
}