package org.example.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDTO {

    private Long userChatId;
    private LocalDateTime appointmentDateTime;
    private String phone;
    private String serviceType;
    private String description;

}
