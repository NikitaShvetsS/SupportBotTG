package org.example.service.impl;

import lombok.AllArgsConstructor;
import org.example.dto.AppointmentRequestDTO;
import org.example.dto.AppointmentResponseDTO;
import org.example.entity.Appointment;
import org.example.entity.User;
import org.example.exception.AppointmentNotFoundException;
import org.example.exception.UserNotFoundException;
import org.example.mapper.AppointmentMapper;
import org.example.repository.AppointmentRepository;
import org.example.repository.UserRepository;
import org.example.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final UserRepository userRepository;

    @Override
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto) {
        Appointment appointment = appointmentMapper.toEntity(dto);
        User user = userRepository.findByTelegramChatId(dto.getUserChatId())
                .orElseThrow(() -> new UserNotFoundException("User with chatId {} not found" + dto.getUserChatId()));
        if (user == null){
            throw new UserNotFoundException("User with chatId {} not found" + dto.getUserChatId());
        }
        appointment.setUser(user);
        appointment.setBookedAt(LocalDateTime.now());
        Appointment saved = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(saved);
    }

    @Override
    public AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestDTO dto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));
        appointment.setAppointmentDateTime(dto.getAppointmentDateTime());
        appointment.setServiceType(dto.getServiceType());
        appointment.setDescription(dto.getDescription());
        Appointment updated = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(updated);
    }
    @Override
    public List<Appointment> getAppointmentsForTomorrow() {
        LocalDateTime start = LocalDateTime.now().plusDays(1).toLocalDate().atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        return appointmentRepository.findByAppointmentDateTimeBetween(start, end);
    }
    @Override
    public AppointmentResponseDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));
        return appointmentMapper.toDto(appointment);
    }
    @Override
    public List<Appointment> getAppointmentsIn24Hours() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime targetTimeStart = now.plusHours(24).withSecond(0).withNano(0);
        LocalDateTime targetTimeEnd = targetTimeStart.plusMinutes(10); // 10 минутный интервал

        return appointmentRepository.findByAppointmentDateTimeBetween(targetTimeStart, targetTimeEnd);
    }
    @Override
    public List<Appointment> getAppointmentsByChatId(Long chatId){
        User user = userRepository.findByTelegramChatId(chatId)
                .orElseThrow(()-> new UserNotFoundException("User with chat id {} not found" + chatId.toString()));
        return appointmentRepository.findByUser(user);
    }
    @Override
    public List<AppointmentResponseDTO> getAllUserAppointments(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User with userId " + userId + " not found"));
        List<Appointment> appointments = appointmentRepository.findByUser(user);
        return appointments.stream()
            .map(appointmentMapper::toDto)
            .collect(Collectors.toList());
    }
    @Override
    public List<AppointmentResponseDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(appointmentMapper::toDto)
                .toList();
    }
    @Override
    public void deleteAppointment(Long id) {
        ResponseEntity.ok("User with id: {}" + id + "has been deleted");
        appointmentRepository.deleteById(id);
    }
}
