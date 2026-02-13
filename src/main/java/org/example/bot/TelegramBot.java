package org.example.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bot.handlers.*;
import org.example.session.AppointmentStage;
import org.example.session.SessionService;
import org.example.session.UserStage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final CommandHandler commandHandler;
    private final ButtonHandler buttonHandler;
    private final RegistrationHandler registrationHandler;
    private final AppointmentHandler appointmentHandler;
    private final SessionService sessionService;


    @Value("${telegram.bot.username}")
    private String username;

    @Value("${telegram.bot.token}")
    private String token;

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        Message message = update.getMessage();
        String text = message.getText();
        Long chatId = message.getChatId();

        SendMessage response;

        try {
            if (text.startsWith("/")) {
                response = commandHandler.handle(message);
            } else if (isInRegistration(chatId)) {
                response = registrationHandler.handle(message);
            } else if (isInAppointment(chatId)) {
                response = appointmentHandler.handle(message);
            } else {
                response = buttonHandler.handleMain(message);
            }
            execute(response);
        } catch (Exception e) {
            e.printStackTrace();
            SendMessage errorMessage = new SendMessage(chatId.toString(), "⚠️ Internal error. Please try again later.");
            try {
                execute(errorMessage);
            } catch (TelegramApiException ex) {
                ex.printStackTrace();
            }
        }
    }
    private boolean isInRegistration(Long chatId) {
        return sessionService.getUserSession(chatId).getStage() != UserStage.NONE;
    }

    private boolean isInAppointment(Long chatId) {
        return sessionService.getSession(chatId).getStage() != AppointmentStage.NONE;
    }


}