package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.bot.handlers.MessageHandler;
import org.example.entity.Appointment;
import org.example.service.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final MessageHandler messageHandler;
    private final AppointmentServiceImpl appointmentService;

    @Scheduled(cron = "0 */10 * * * *")
    @Override
    public void sendAppointmentReminders() {
        List<Appointment> upcomingAppointments = appointmentService.getAppointmentsIn24Hours();
        for (Appointment appointment : upcomingAppointments) {
            if (appointment.isConfirmed()) {
                continue;
            }

            Long chatId = appointment.getUser().getTelegramChatId();
            if (chatId == null) {
                System.out.println("No telegramChatId for appointment id = " + appointment.getId());
                continue;
            }

            String message = String.format(
                    "Привет, %s!\nЗавтра в %s у вас назначена встреча.\nПодтвердите, пожалуйста, будете ли вы.",
                    appointment.getUser().getFirstName(),
                    appointment.getAppointmentDateTime().format(DateTimeFormatter.ofPattern("dd.MM в HH:mm"))
            );

            messageHandler.sendNotificationMessage(chatId, message);
        }
    }


}
