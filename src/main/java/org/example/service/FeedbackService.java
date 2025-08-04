package org.example.service;

import org.example.dto.FeedbackDTO;

public interface FeedbackService {

    Long createFeedback(Long appointmentId, FeedbackDTO feedbackDTO);
    FeedbackDTO getFeedbackByAppointmentId(Long appointmentId);
    void deleteFeedback(Long id);

}
