/**
 * @team Journeys
 * @file APIResponse.java
 * @date January 21st, 2022
 */

package com.journeys.main.dto;

import net.minidev.json.JSONObject;

public class APIResponse extends JSONObject {

    /**
     * Constructor of APIResponse
     * @param response the response message
     */
    public APIResponse(String response) {
        this.put("responseMsg", response);
    }
}