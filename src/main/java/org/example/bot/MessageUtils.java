package org.example.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class MessageUtils {
    public SendMessage buildMessage(Long chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
    }
    public SendMessage buildMessage(Long chatId, String text, ReplyKeyboardMarkup keyboard) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(keyboard)
                .build();
    }
}
