package org.example.session;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SessionService {

    private final Map<Long, UserSession> sessions = new HashMap<>();
    private final Map<Long, RegistrationSession> registeredSession = new HashMap<>();
    public RegistrationSession getUserSession(Long chatId) {
        return registeredSession.computeIfAbsent(chatId, id -> {
            RegistrationSession rs = new RegistrationSession();
            rs.setTelegramChatId(chatId);
            rs.setStage(UserStage.NONE);
            return rs;
        });
    }

    public UserSession getSession(Long chatId) {
        return sessions.computeIfAbsent(chatId, id -> {
            UserSession s = new UserSession();
            s.setChatId(id);
            s.setStage(AppointmentStage.NONE);
            return s;
        });
    }

    public void clearRegisterSession(Long chatId) {
        registeredSession.remove(chatId);
    }
    public void clearAppointmentSession(Long chatId) {
        sessions.remove(chatId);
    }

}
