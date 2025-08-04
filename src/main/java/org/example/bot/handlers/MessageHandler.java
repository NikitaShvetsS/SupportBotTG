package org.example.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.example.bot.Keyboard;
import org.example.bot.MessageUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


@Component
@RequiredArgsConstructor
public class MessageHandler {

    private final MessageUtils messageUtils;
    private final Keyboard keyboard;

    public SendMessage sendWelcomeMessage(Long chatId) {
        return messageUtils.buildMessage(chatId, "Welcome to Support Bot!", keyboard.startKeyboard());
    }
    public SendMessage sendMenuMessage(Long chatId) {
        return messageUtils.buildMessage(chatId, "Please select an option!", keyboard.startKeyboard());
    }
    public SendMessage sendHelperMessage(Long chatId){
        return messageUtils.buildMessage(chatId, "Please select an option!", keyboard.helpKeyboard());
    }
    public SendMessage defaultMessage(Long chatId){
        return messageUtils.buildMessage(chatId, "Please read the information about this bot", keyboard.defaultKeyboard());
    }
    public SendMessage sendMyAppointmentsMessage(Long chatId){
        return messageUtils.buildMessage(chatId, "Please select an option!", keyboard.myAppointmentsKeyboard());
    }

    public SendMessage sendNotificationMessage(Long chatId, String messageText){
        return messageUtils
                .buildMessage(chatId,
                        messageText + "\nPlease select if you can come to this appointment",
                        keyboard.notificationKeyboard());
    }

}
