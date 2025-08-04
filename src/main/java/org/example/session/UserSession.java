package org.example.session;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class UserSession {

    private Long chatId;
    private LocalDate selectedDate;
    private LocalTime selectedTime;
    private AppointmentStage stage;
    private String fullName;
    private String phoneNumber;
    private String serviceType;
    private String addInfo;


}
