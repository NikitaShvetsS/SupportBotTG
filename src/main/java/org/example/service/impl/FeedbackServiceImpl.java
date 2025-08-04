package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.bot.Keyboard;
import org.example.bot.MessageUtils;
import org.example.dto.FeedbackDTO;
import org.example.entity.Appointment;
import org.example.entity.Feedback;
import org.example.exception.AppointmentNotFoundException;
import org.example.exception.FeedbackNotFoundException;
import org.example.mapper.FeedbackMapper;
import org.example.repository.AppointmentRepository;
import org.example.repository.FeedbackRepository;
import org.example.service.FeedbackService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final AppointmentRepository appointmentRepository;
    private final FeedbackMapper feedbackMapper;
    private final MessageUtils messageUtils;
    private final Keyboard keyboard;

    @Override
    public Long createFeedback(Long appointmentId, FeedbackDTO feedbackDTO) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));
        Feedback feedback = feedbackMapper.toEntity(feedbackDTO, appointment);
        feedback.setAppointment(appointment);
        return feedbackRepository.save(feedback).getId();
    }

    @Override
    public FeedbackDTO getFeedbackByAppointmentId(Long appointmentId) {
        Feedback feedback = feedbackRepository
                .findByAppointmentId(appointmentId)
                .orElseThrow(() -> new FeedbackNotFoundException("Feedback not found"));
        return feedbackMapper.toDto(feedback);
    }

    @Override
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }

    public List<SendMessage> buildFeedbackMessages() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        List<Appointment> yesterdayAppointments = appointmentRepository.findAllByAppointmentDateTime(yesterday.toLocalDate());

        List<SendMessage> messages = new ArrayList<>();

        for (Appointment appt : yesterdayAppointments) {
            boolean feedbackExists = feedbackRepository.findByAppointmentId(appt.getId()).isPresent();
            if (!feedbackExists) {
                SendMessage msg = messageUtils.buildMessage(
                        appt.getUser().getTelegramChatId(),
                        "Thank you for your visit! Please rate your experience: 1-5",
                        keyboard.rateKeyboard()
                );
                messages.add(msg);
            }
        }

        return messages;
    }

}
