package org.example.service;

import org.example.dto.AppointmentRequestDTO;
import org.example.dto.AppointmentResponseDTO;
import org.example.entity.Appointment;

import java.util.List;

public interface AppointmentService {

    AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto);
    AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestDTO dto);
    public List<Appointment> getAppointmentsByChatId(Long chatId);
    AppointmentResponseDTO getAppointmentById(Long id);
    List<AppointmentResponseDTO> getAllAppointments();
    void deleteAppointment(Long id);
    List<AppointmentResponseDTO> getAllUserAppointments(Long id);
    List<Appointment> getAppointmentsIn24Hours();

    List<Appointment> getAppointmentsForTomorrow();
}
