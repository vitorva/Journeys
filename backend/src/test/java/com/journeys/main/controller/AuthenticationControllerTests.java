/**
 * @team Journeys
 * @file AuthenticationControllerTests.java
 * @date January 21st, 2022
 */

package com.journeys.main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.journeys.main.dto.AuthenticationRequest;
import com.journeys.main.dto.AuthenticationResponse;
import com.journeys.main.exceptions.user.UserIllegalDataException;
import com.journeys.main.dto.APIResponse;
import com.journeys.main.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureMockMvc
@SpringBootTest
public class AuthenticationControllerTests extends BaseControllerTest {

    private MockMvc mvc;

    @MockBean
    private AuthenticationController authenticationController;

    static User mockUser;

    @Autowired
    ObjectMapper mapper;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentationContextProvider){
        this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .build();
    }

    /**
     * Initializing the mock data with dummy User
     */
    @BeforeAll
    public static void Init(){
        mockUser = new User(
                "testUser",
                "testFirstName",
                "testLastName",
                "testPassword",
                "testEmail",
                new HashSet<>(),
                new HashSet<>()
        );
    }

    /**
     * Testing the authentication of a user
     * @throws Exception
     */
    @Test
    public void AuthenticateUser() throws Exception{
        Date now = new Date();

        AuthenticationRequest testReq = new AuthenticationRequest("testUser","testPassword");
        AuthenticationResponse response = new AuthenticationResponse(mockUser.getAsDetails(), now);
        ResponseEntity r = new ResponseEntity(response, HttpStatus.OK);
        Mockito.when(authenticationController.createAuthenticationToken(any(AuthenticationRequest.class)))
                .thenReturn(r);

        mvc.perform(
                RestDocumentationRequestBuilders.post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testReq).getBytes(StandardCharsets.UTF_8))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userName").description("User name of the user"),
                                fieldWithPath("password").description("Corresponding password to the user")
                        ),
                        responseFields(
                                subsectionWithPath("user.userName").description("user name of the user"),
                                subsectionWithPath("user.lastName").description("last name of the user"),
                                subsectionWithPath("user.firstName").description("first name of the user"),
                                subsectionWithPath("user.email").description("email  of the user"),
                                fieldWithPath("tokenDuration").description("duration of the authentication token"))
                ));
    }

    /**
     * Testing the case where bad credentials have been provided
     * @throws Exception
     */
    @Test
    public void BadCredentialException() throws Exception{
        AuthenticationRequest testReq = new AuthenticationRequest("obiouslyWrongUsername","obviouslyWrongPassword");

        Mockito.when(authenticationController.createAuthenticationToken(any(AuthenticationRequest.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        mvc.perform(
                RestDocumentationRequestBuilders.post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testReq).getBytes(StandardCharsets.UTF_8))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("userName").description("Wrongly entered user name"),
                                fieldWithPath("password").description("Wrongly entered password")
                        ),
                        responseFields(
                                fieldWithPath("date").description("Occurence time of the error"),
                                fieldWithPath("status").description("Status code"),
                                fieldWithPath("message").description("Message of the error"),
                                fieldWithPath("url").description("source url that triggered the error")
                        ))
        );
    }

    /**
     * Testing the logout of the user
     * @throws Exception
     */
    @Test
    public void LogUserOut() throws Exception {
        ResponseEntity responseEntity= new ResponseEntity(new APIResponse("The user logged out"),HttpStatus.OK);
        Mockito.when(authenticationController.logOut())
                .thenReturn(responseEntity);

        mvc.perform(
                RestDocumentationRequestBuilders.get("/api/auth/logout"))
                .andExpect(status().isOk())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("responseMsg").description("simple api response confirming that the user logged out")
                        )
                ));
    }

    /**
     * Testing the registration of the user
     * @throws Exception
     */
    @Test
    public void registerUser() throws Exception{
        Date now = new Date();
        User registerUser = new User("registeredUser","registeredFirstName","RegisteredLastName","registeredPassword","registeredEmail",null,null);
        AuthenticationResponse response = new AuthenticationResponse(registerUser.getAsDetails(), now);
        ResponseEntity responseEntity = new ResponseEntity(response, HttpStatus.OK);
        Mockito.when(authenticationController.signUp(any(User.class))).thenReturn(responseEntity);

        mvc.perform(
                RestDocumentationRequestBuilders.post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(registerUser).getBytes(StandardCharsets.UTF_8)))
                .andExpect(status().isOk())
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("userName").description("user name of the user"),
                                        fieldWithPath("lastName").description("last name of the user"),
                                        fieldWithPath("firstName").description("first name of the user"),
                                        fieldWithPath("password").description("encrypted password  of the user"),
                                        fieldWithPath("email").description("email of the user"),
                                        fieldWithPath("experiences").description("is ignored"),
                                        fieldWithPath("journeys").description("is ignored")
                                ),
                                responseFields(
                                        subsectionWithPath("user.userName").description("user name of the user"),
                                        subsectionWithPath("user.lastName").description("last name of the user"),
                                        subsectionWithPath("user.firstName").description("first name of the user"),
                                        subsectionWithPath("user.email").description("email  of the user"),
                                        fieldWithPath("tokenDuration").description("duration of the authentication token")
                                )
                ));
    }

    static RestDocumentationResultHandler doRegisterException(String identifier ){
        return document(identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                        fieldWithPath("userName").description("user name of the user"),
                        fieldWithPath("lastName").description("last name of the user"),
                        fieldWithPath("firstName").description("first name of the user"),
                        fieldWithPath("password").description("encrypted password  of the user"),
                        fieldWithPath("email").description("email of the user"),
                        fieldWithPath("experiences").description("is ignored"),
                        fieldWithPath("journeys").description("is ignored")
                ),
                responseFields(
                        fieldWithPath("date").description("Occurence time of the error"),
                        fieldWithPath("status").description("Status code"),
                        fieldWithPath("message").description("Message of the error"),
                        fieldWithPath("url").description("source url that triggered the error")
                )
        );
    }

    /**
     * Testing the case where the register has invalid inputs
     * @throws Exception
     */
    @Test
    public void RegisterException() throws Exception{
        User registerUser = new User("BadUserName","BadFirstName","BadLastName","BadPassword","BadEmail",null,null);
        Mockito.when(authenticationController.signUp(any(User.class))).thenThrow(new UserIllegalDataException("Invalid fields present"));

        mvc.perform(
                        RestDocumentationRequestBuilders.post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(registerUser).getBytes(StandardCharsets.UTF_8)))
                .andExpect(status().isBadRequest())
                .andDo(doRegisterException("{method-name}"));
    }

    /**
     * Testing the case where a user already exists
     * @throws Exception
     */
    @Test
    public void UserAlreadyExistException() throws Exception{
        User registerUser = new User("ExistingUserName","registeredFirstName","RegisteredLastName","registeredPassword","registeredEmail",null,null);
        Mockito.when(authenticationController.signUp(any(User.class))).thenThrow(new UserIllegalDataException("the user already exists"));

        mvc.perform(
                        RestDocumentationRequestBuilders.post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(registerUser).getBytes(StandardCharsets.UTF_8)))
                .andExpect(status().isBadRequest())
                .andDo(doRegisterException("{method-name}"));
    }
}