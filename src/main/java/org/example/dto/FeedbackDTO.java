package org.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackDTO {

    private Long appointmentId;
    private int rating;
    private String comment;

}
