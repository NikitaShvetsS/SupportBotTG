package org.example.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class CommandHandler {
    private final MessageHandler messageHandler;
    public SendMessage handle(Message message) {
        String text = message.getText();
        Long chatId = message.getChatId();

       return switch (text) {
            case "/start" ->
                    messageHandler.sendWelcomeMessage(chatId);
            case "/help" ->
                    messageHandler.sendHelperMessage(chatId);
            case "/appointments" ->
                    messageHandler.sendMyAppointmentsMessage(chatId);
            case "/menu" ->
                    messageHandler.sendMenuMessage(chatId);
            default ->
                    messageHandler.defaultMessage(chatId);
        };
    }
}
