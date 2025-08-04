package org.example.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.example.bot.Keyboard;
import org.example.bot.MessageUtils;
import org.example.dto.AppointmentResponseDTO;
import org.example.entity.User;
import org.example.exception.UserNotFoundException;
import org.example.mapper.UserMapper;
import org.example.service.impl.AppointmentServiceImpl;
import org.example.service.impl.UserServiceImpl;
import org.example.session.*;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ButtonHandler {

    private final Keyboard keyboard;
    private final MessageUtils messageUtils;
    private final SessionService sessionService;
    private final AppointmentServiceImpl appointmentService;
    private final UserMapper mapper;
    private final UserServiceImpl userService;
    private final RegistrationHandler registrationHandler;



    private final String infoText = "Welcome! This bot helps you easily book and manage appointments. Here’s how to use it step-by-step:\n" +
            "\n" +
            "What you can do:\nregister yourself (required before using the bot),\nbook a new appointment, view all your appointments,\ncancel an existing appointment,\nget notifications when your appointments are confirmed,\nreturn to main menu anytime.\n" +
            "\n" +
            "How to use:\n" +
            "\t1.\tStart — Press /start or select /menu to begin.\n" +
            "\t2.\tRegister — If you haven’t registered yet, the bot will ask for your contact info. Simply click “Send Contact” — it’s safe and required.\n" +
            "\t3.\tBook an Appointment — Choose a service, pick a date and time, optionally leave a comment, and wait for confirmation from the admin.\n" +
            "\t4.\tCheck Your Appointments — Press “My appointments” to see your full list.\n" +
            "\t5.\tReturn to Main Menu — Press “Menu” to go back and choose another action.\n" +
            "\n" +
            "Important Notes:\nYou can only manage your own appointments. You’ll be notified when your appointment is confirmed or changed. If you need help, just return to this Info section.\n" +
            "\n" +
            "Your data is safe.\nWe only use your contact to manage your bookings — nothing more.\n" +
            "\n" +
            "If you’re stuck — just press /menu and start again.\n";


    public SendMessage isUserRegistered(Long chatId){
        try {
            User user = mapper.toEntity(userService.getUserByChatId(chatId));

            if(user == null || !user.isRegistered()){
                return messageUtils.buildMessage(chatId,
                        "You are not registered yet. Before making an appointment let's start your registration ",
                        keyboard.registrationKeyboard());
            } else {
                return null;
            }
        } catch (UserNotFoundException e) {
            return messageUtils.buildMessage(chatId,
                    "You are not registered yet. Before making an appointment let's start your registration ",
                    keyboard.registrationKeyboard());
        } catch (Exception e) {
            return messageUtils.buildMessage(chatId,
                    "Internal error occurred while checking registration. Please try again.");
        }
    }

    public SendMessage handleMain(Message message) {
        String text = message.getText();
        Long chatId = message.getChatId();

        switch (text.toLowerCase()) {
            case "information about this bot" -> {
                return messageUtils.buildMessage(chatId, infoText, keyboard.infoKeyboard());
            }
            case "make an appointment" -> {
                SendMessage check = isUserRegistered(chatId);
                if (check != null) return check;

                UserSession session = sessionService.getSession(chatId);
                session.setStage(AppointmentStage.SELECT_DATE);
                return messageUtils.buildMessage(chatId, "Choose a date:", keyboard.getDateSelectionKeyboard());
            }
            case "consultation in bot" -> {
                SendMessage check = isUserRegistered(chatId);
                if (check != null) return check;

                return messageUtils.buildMessage(chatId,
                        "Please wait a moment. Our specialist will connect you as soon as possible", keyboard.defaultKeyboard());
            }
            case "consultation with our specialist" -> {
                SendMessage check = isUserRegistered(chatId);
                if (check != null) return check;

                return messageUtils.buildMessage(chatId,
                        "Please wait a moment. Our technical specialist will connect you as soon as possible");
            }
            case "menu" -> {
                return messageUtils.buildMessage(chatId,
                        "Please choose an option", keyboard.startKeyboard());
            }
            case "my appointments" -> {
                SendMessage check = isUserRegistered(chatId);
                if (check != null) return check;

                User user = mapper.toEntity(userService.getUserByChatId(chatId));
                if (user == null) return messageUtils.buildMessage(chatId, "User not found.");

                List<AppointmentResponseDTO> appointments = appointmentService.getAllUserAppointments(user.getId());
                if (appointments.isEmpty()) return messageUtils.buildMessage(chatId, "You have no appointments");

                String formattedText = formatAppointments(appointments);
                return messageUtils.buildMessage(chatId, formattedText);

            }
            case "start the registration" -> {
                return  registrationHandler.handle(message);
            }
            default -> {
                return messageUtils.buildMessage(chatId, "Sorry, I didn’t understand you. Please use the menu.");
            }
        }
    }
    private String formatAppointments(List<AppointmentResponseDTO> appointments) {
        StringBuilder sb = new StringBuilder("🗓 Your appointments:\n\n");
        int count = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        for (AppointmentResponseDTO dto : appointments) {
            sb.append("Appointment №").append(count++).append("\n")
                    .append("Date: ").append(dto.getAppointmentDateTime().format(formatter)).append("\n")
                    .append("Service: ").append(dto.getServiceType()).append("\n")
                    .append("Comment: ").append(dto.getDescription()).append("\n")
                    .append(dto.isConfirmed() ? "Confirm" : "Not confirmed")
                    .append("\n\n");
        }
        return sb.toString();
    }

}
