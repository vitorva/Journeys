/**
 * @team Journeys
 * @file JourneyUpdateRequest.java
 * @date January 21st, 2022
 */

package com.journeys.main.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class JourneyUpdateRequest {
    private String journeyId;
    private String poiId;
    private String description;
    private List<MultipartFile> images;
    private String date;

}