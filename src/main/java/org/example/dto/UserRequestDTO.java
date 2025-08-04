package org.example.dto;

import lombok.*;
import org.example.entity.Gender;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String phoneNumber;
    private String email;
    private Long chatId;
    private boolean isRegistered;

}
