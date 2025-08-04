package org.example.dto;

import lombok.*;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class AppointmentResponseDTO {

    private Long id;
    private LocalDateTime appointmentDateTime;
    private String serviceType;
    private String description;
    private boolean confirmed;
    private LocalDateTime bookedAt;

}
