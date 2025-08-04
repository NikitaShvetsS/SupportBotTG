package org.example.session;

import lombok.Getter;
import lombok.Setter;
import org.example.entity.Gender;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Getter
@Setter
@Component
public class RegistrationSession {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String phoneNumber;
    private String email;
    private Long telegramChatId;
    private UserStage stage;

}
