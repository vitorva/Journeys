/**
 * @team Journeys
 * @file BaseControllerTest.java
 * @date January 21st, 2022
 */

package com.journeys.main.controller;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class BaseControllerTest {

    static RestDocumentationResultHandler doDocumentException(String identifier ){
        return document(identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("date").description("Occurence time of the error"),
                        fieldWithPath("status").description("Status code"),
                        fieldWithPath("message").description("Message of the error"),
                        fieldWithPath("url").description("source url that triggered the error")
                )
        );
    }
}