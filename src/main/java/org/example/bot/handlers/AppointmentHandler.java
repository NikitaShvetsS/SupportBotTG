package org.example.bot.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bot.Keyboard;
import org.example.bot.MessageUtils;
import org.example.dto.AppointmentRequestDTO;
import org.example.service.impl.AppointmentServiceImpl;
import org.example.session.*;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppointmentHandler {

    private final SessionService sessionService;
    private final MessageUtils messageUtils;
    private final Keyboard keyboard;
    private final AppointmentServiceImpl appointmentService;

    public SendMessage handle(Message message) {
        Long chatId = message.getChatId();
        String text = message.getText();
        UserSession session = sessionService.getSession(chatId);
        log.info("Handling message for chatId {} at stage {}", chatId, session.getStage());

        switch (session.getStage()) {
            case SELECT_DATE -> {
                    session.setSelectedDate(LocalDate.parse(text));
                    session.setStage(AppointmentStage.SELECT_TIME);
                    return messageUtils.buildMessage(chatId, "Now select a time:", keyboard.getTimeSelectionKeyboard());
            }
            case SELECT_TIME -> {
                session.setSelectedTime(LocalTime.parse(text));
                session.setStage(AppointmentStage.ENTER_NAME);
                return messageUtils.buildMessage(chatId, "Please enter your full name:");
            }
            case ENTER_NAME -> {
                session.setFullName(text);
                session.setStage(AppointmentStage.ENTER_SERVICE_TYPE);
                return messageUtils.buildMessage(chatId, "Now enter desired service:");
            }
            case ENTER_SERVICE_TYPE -> {
                session.setServiceType(text);
                session.setStage(AppointmentStage.ENTER_PHONE);
                return messageUtils.buildMessage(chatId, "Now enter your phone number:");
            }
            case ENTER_PHONE -> {
                session.setPhoneNumber(text);
                session.setStage(AppointmentStage.ENTER_ADD_INF0);
                return messageUtils.buildMessage(chatId, "Now enter additional request(if it needed) or enter '-':");

            }
            case ENTER_ADD_INF0 -> {
                session.setAddInfo(text);
                session.setStage(AppointmentStage.CONFIRMATION);
                return messageUtils.buildMessage(chatId,
                        "Confirm your appointment:\n\n" +
                                "Date: " + session.getSelectedDate() + "\n" +
                                "Time: " + session.getSelectedTime() + "\n" +
                                "Name: " + session.getFullName() + "\n" +
                                "Phone: " + session.getPhoneNumber() + "\n" +
                                "Service Type: " + session.getServiceType() + "\n" +
                                "Additional request: " + session.getAddInfo() + "\n\n" +
                                "Type 'confirm' to save or 'cancel' to discard.", keyboard.confirmKeyboard());
            }
            case CONFIRMATION -> {
                if (text.equalsIgnoreCase("confirm")) {
                    session.setStage(AppointmentStage.NONE);
                    AppointmentRequestDTO appointment= new AppointmentRequestDTO();
                    appointment.setUserChatId(chatId);
                    appointment.setAppointmentDateTime(LocalDateTime.of(session.getSelectedDate(), session.getSelectedTime()));
                    appointment.setPhone(session.getPhoneNumber());
                    appointment.setServiceType(session.getServiceType());
                    appointment.setDescription(session.getAddInfo());
                    try {
                        appointmentService.createAppointment(appointment);
                        sessionService.clearAppointmentSession(chatId);
                        return messageUtils.buildMessage(chatId, "Your appointment is confirmed!", keyboard.postConfirmKeyboard());
                    } catch (Exception e) {
                        return messageUtils.buildMessage(chatId, "Something went wrong while saving your appointment. Please try again.");
                    }
                } else if (text.equalsIgnoreCase("cancel")) {
                    sessionService.clearAppointmentSession(chatId);
                    return messageUtils.buildMessage(chatId, "Your appointment was cancelled.", keyboard.canceledKeyboard());

                }else if (text.equalsIgnoreCase("edit")) {
                    session.setStage(AppointmentStage.SELECT_DATE);
                    return messageUtils.buildMessage(chatId, "Let's start again. Choose the date:", keyboard.getDateSelectionKeyboard());
                } else {
                    return messageUtils.buildMessage(chatId, "Please type 'confirm' or 'cancel'.");
                }
            }
            case NONE -> {
                return messageUtils.buildMessage(chatId, "Please start by typing /start.");
            }
            default -> {
                session.setStage(AppointmentStage.SELECT_DATE);
                return messageUtils.buildMessage(chatId, "Choose a date:", keyboard.getDateSelectionKeyboard());
            }
        }
    }
}
