package org.example.mapper;

import org.example.dto.FeedbackDTO;
import org.example.entity.Appointment;
import org.example.entity.Feedback;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class FeedbackMapper {

    public Feedback toEntity(FeedbackDTO dto, Appointment appointment) {
        if (dto == null) {
            return null;
        }
        Feedback feedback = new Feedback();
        feedback.setAppointment(appointment);
        feedback.setRating(dto.getRating());
        feedback.setComment(dto.getComment());
        feedback.setCreatedAt(LocalDateTime.now());
        return feedback;
    }

    public FeedbackDTO toDto(Feedback feedback) {
        if (feedback == null) {
            return null;
        }
        FeedbackDTO dto = new FeedbackDTO();
        dto.setAppointmentId(feedback.getAppointment() != null && feedback.getAppointment().getUser() != null
                ? feedback.getAppointment().getId()
                : null);
        dto.setRating(feedback.getRating());
        dto.setComment(feedback.getComment());
        return dto;
    }

}
