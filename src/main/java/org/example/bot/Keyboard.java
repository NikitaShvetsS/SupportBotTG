package org.example.bot;

import lombok.RequiredArgsConstructor;
import org.example.entity.Gender;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Keyboard {

    public ReplyKeyboardMarkup startKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("Information about this bot"));
        rows.add(row);
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Make an appointment"));
        rows.add(row1);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Consultation in bot"));
        rows.add(row2);
        markup.setKeyboard(rows);
        markup.setResizeKeyboard(true);
        return markup;
    }
    public ReplyKeyboardMarkup helpKeyboard(){
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Information about this bot"));
        rows.add(row1);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Consultation with our specialist"));
        rows.add(row2);
        markup.setKeyboard(rows);
        markup.setResizeKeyboard(true);
        return markup;
    }
    public ReplyKeyboardMarkup defaultKeyboard(){
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Information about this bot"));
        rows.add(row1);
        markup.setKeyboard(rows);
        markup.setResizeKeyboard(true);
        return markup;
    }
    public ReplyKeyboardMarkup notificationKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Yes"));
        row1.add(new KeyboardButton("No"));
        rows.add(row1);
        markup.setKeyboard(rows);
        markup.setResizeKeyboard(true);
        return markup;
    }
    public ReplyKeyboardMarkup confirmKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("confirm"));
        row1.add(new KeyboardButton("cancel"));
        rows.add(row1);
        markup.setKeyboard(rows);
        markup.setResizeKeyboard(true);
        return markup;
    }
    public ReplyKeyboardMarkup getDateSelectionKeyboard() {
        List<LocalDate> dates1 = Arrays.asList(
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(4)
        );
        List<KeyboardRow> keyboard = new ArrayList<>();
        for (LocalDate date : dates1) {
            KeyboardRow row = new KeyboardRow();
            row.add(new KeyboardButton(date.toString()));
            keyboard.add(row);
        }
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setKeyboard(keyboard);
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(true);
        return markup;
    }
    public ReplyKeyboardMarkup getTimeSelectionKeyboard() {
        List<String> times = Arrays.asList(
                "10:00", "11:00", "12:00", "13:00",
                "14:00", "15:00", "16:00", "17:00"
        );
        List<KeyboardRow> keyboard = new ArrayList<>();
        for (int i = 0; i < times.size(); i += 2) {
            KeyboardRow row = new KeyboardRow();
            row.add(new KeyboardButton(times.get(i)));
            if (i + 1 < times.size()) {
                row.add(new KeyboardButton(times.get(i + 1)));
            }
            keyboard.add(row);
        }
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setKeyboard(keyboard);
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(true);
        return markup;
    }

    public ReplyKeyboardMarkup canceledKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Make an appointment"));
        rows.add(row1);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Menu"));
        rows.add(row2);
        markup.setKeyboard(rows);
        markup.setResizeKeyboard(true);
        return markup;

    }
    public ReplyKeyboardMarkup postConfirmKeyboard(){
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("My appointments"));
        rows.add(row1);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Menu"));
        rows.add(row2);
        markup.setKeyboard(rows);
        markup.setResizeKeyboard(true);
        return markup;
    }
    public ReplyKeyboardMarkup infoKeyboard(){
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Make an appointment"));
        rows.add(row1);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Consultation in bot"));
        rows.add(row2);
        markup.setKeyboard(rows);
        markup.setResizeKeyboard(true);
        return markup;
    }
    public ReplyKeyboardMarkup genderKeyboard(){
        List<KeyboardRow> keyboard = new ArrayList<>();
        for (Gender gender: Gender.values()){
            KeyboardRow row = new KeyboardRow();
            row.add(new KeyboardButton(gender.name()));
            keyboard.add(row);
        }
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setKeyboard(keyboard);
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(true);
        return markup;
    }
    public ReplyKeyboardMarkup registrationKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Start the registration"));
        rows.add(row1);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Menu"));
        rows.add(row2);
        markup.setKeyboard(rows);
        markup.setResizeKeyboard(true);
        return markup;
    }
    public ReplyKeyboardMarkup rateKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("1"));
        row1.add(new KeyboardButton("2"));
        row1.add(new KeyboardButton("3"));
        row1.add(new KeyboardButton("4"));
        row1.add(new KeyboardButton("5"));
        rows.add(row1);
        markup.setKeyboard(rows);
        markup.setResizeKeyboard(true);
        return markup;
    }

    public ReplyKeyboardMarkup myAppointmentsKeyboard() {

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("My appointments"));
        rows.add(row1);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Menu"));
        rows.add(row2);
        markup.setKeyboard(rows);
        markup.setResizeKeyboard(true);
        return markup;

    }
}
