package org.example.mapper;

import lombok.RequiredArgsConstructor;
import org.example.dto.AppointmentRequestDTO;
import org.example.dto.AppointmentResponseDTO;
import org.example.entity.Appointment;
import org.example.entity.User;
import org.example.exception.UserNotFoundException;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentMapper {

    private final UserRepository userRepository;

    public Appointment toEntity(AppointmentRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        User user = userRepository.findByTelegramChatId(dto.getUserChatId())
                .orElseThrow(() -> new UserNotFoundException("User with chatId {} not found" + dto.getUserChatId()));

        return Appointment.builder()
                .appointmentDateTime(dto.getAppointmentDateTime())
                .serviceType(dto.getServiceType())
                .description(dto.getDescription())
                .confirmed(false)
                .bookedAt(java.time.LocalDateTime.now())
                .user(user)
                .build();
    }

    public AppointmentResponseDTO toDto(Appointment appointment) {
        if (appointment == null) {
            return null;
        }
        return AppointmentResponseDTO.builder()
                .id(appointment.getId())
                .appointmentDateTime(appointment.getAppointmentDateTime())
                .serviceType(appointment.getServiceType())
                .description(appointment.getDescription())
                .confirmed(appointment.isConfirmed())
                .bookedAt(appointment.getBookedAt())
                .build();
    }

}
