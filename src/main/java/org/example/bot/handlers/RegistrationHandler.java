package org.example.bot.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bot.Keyboard;
import org.example.bot.MessageUtils;
import org.example.dto.UserRequestDTO;
import org.example.entity.Gender;
import org.example.service.UserService;
import org.example.session.RegistrationSession;
import org.example.session.SessionService;
import org.example.session.UserStage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationHandler {

    private final SessionService sessionService;
    private final UserService userService;
    private final MessageUtils messageUtils;
    private final Keyboard keyboard;

    public SendMessage handle(Message message) {
        Long chatId = message.getChatId();
        String text = message.getText();
        RegistrationSession session = sessionService.getUserSession(chatId);
        log.info("Handling message for chatId {} at stage {}", chatId, session.getStage());
        if (session.getStage() == UserStage.NONE) {
            session.setStage(UserStage.SELECT_START);
            return messageUtils.buildMessage(chatId, "Enter your first name:");
        }

        switch (session.getStage()) {
            case SELECT_START, ENTER_FIRSTNAME -> {
                session.setFirstName(text);
                session.setStage(UserStage.ENTER_LASTNAME);
                return messageUtils.buildMessage(chatId, "Enter your last name:");
            }
            case ENTER_LASTNAME -> {
                session.setLastName(text);
                session.setStage(UserStage.ENTER_DATEOFBIRTH);
                return messageUtils.buildMessage(chatId, "Enter your date of birth (YYYY-MM-DD):");
            }
            case ENTER_DATEOFBIRTH -> {
                try{
                    LocalDate date = LocalDate.parse(text);
                    session.setDateOfBirth(date);
                    session.setStage(UserStage.ENTER_GENDER);
                    return messageUtils.buildMessage(chatId, "Enter your gender:", keyboard.genderKeyboard());
                }catch (DateTimeParseException e){
                    log.warn("Invalid date format from chatId {}: {}", chatId, text);
                    return messageUtils.buildMessage(chatId, "Invalid date format. Please enter in YYYY-MM-DD format");
                }
            }
            case ENTER_GENDER -> {
                try {
                    Gender gender = Gender.valueOf(text);
                    session.setGender(gender);
                    session.setStage(UserStage.ENTER_PHONE);
                    return messageUtils.buildMessage(chatId, "Enter your phone number:");
                }catch (IllegalArgumentException e){
                    return messageUtils.buildMessage(chatId, "Please choose a gender from the keyboard");
                }
            }
            case ENTER_PHONE -> {
                session.setPhoneNumber(text);
                session.setStage(UserStage.ENTER_EMAIL);
                return messageUtils.buildMessage(chatId, "Enter your email:");
            }
            case ENTER_EMAIL -> {
                session.setEmail(text);
                session.setStage(UserStage.NONE);
                UserRequestDTO dto = new UserRequestDTO();
                dto.setFirstName(session.getFirstName());
                dto.setLastName(session.getLastName());
                dto.setDateOfBirth(session.getDateOfBirth());
                dto.setGender(session.getGender());
                dto.setPhoneNumber(session.getPhoneNumber());
                dto.setEmail(session.getEmail());
                dto.setChatId(chatId);
                dto.setRegistered(true);
                userService.addUser(dto);
                sessionService.clearRegisterSession(chatId);
                return messageUtils.buildMessage(chatId,
                        "Registration complete! Now you can make an appointment.", keyboard.startKeyboard());
            }
            default -> {
                return messageUtils.buildMessage(chatId, "Please enter valid input.");
            }
        }
    }
}

