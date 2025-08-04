package org.example.service.impl;

import org.example.dto.AppointmentRequestDTO;
import org.example.dto.AppointmentResponseDTO;
import org.example.entity.Appointment;
import org.example.entity.User;
import org.example.exception.AppointmentNotFoundException;
import org.example.mapper.AppointmentMapper;
import org.example.repository.AppointmentRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class wAppointmentServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AppointmentMapper appointmentMapper;
    @Mock
    private AppointmentRepository appointmentRepository;
    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Test
    void createAppointment() {

        AppointmentRequestDTO dto = new AppointmentRequestDTO();
        dto.setUserChatId(1L);

        Appointment appointmentEntity = new Appointment();
        Appointment savedAppointment = new Appointment();
        AppointmentResponseDTO responseDTO = new AppointmentResponseDTO();

        User mockUser = new User();
        mockUser.setId(1L);

        when(userRepository.findByTelegramChatId(dto.getUserChatId())).thenReturn(Optional.of(mockUser));
        when(appointmentMapper.toEntity(dto)).thenReturn(appointmentEntity);
        when(appointmentRepository.save(ArgumentMatchers.any(Appointment.class))).thenReturn(savedAppointment);
        when(appointmentMapper.toDto(savedAppointment)).thenReturn(responseDTO);

        AppointmentResponseDTO result = appointmentService.createAppointment(dto);

        assertEquals(responseDTO, result);
        verify(appointmentRepository).save(appointmentEntity);


    }

    @Test
    void updateAppointment() {

        Long appointmentId = 1L;

        AppointmentRequestDTO dto = new AppointmentRequestDTO();
        dto.setAppointmentDateTime(LocalDateTime.of(2025, 8, 5, 15, 0));
        dto.setServiceType("dentist");
        dto.setDescription("Checkup");

        Appointment existingAppointment = new Appointment();
        existingAppointment.setId(appointmentId);
        existingAppointment.setAppointmentDateTime(LocalDateTime.of(2025, 8, 4, 13, 0));
        existingAppointment.setServiceType("dentist");
        existingAppointment.setDescription("-");

        Appointment savedAppointment = new Appointment();
        savedAppointment.setId(appointmentId);
        savedAppointment.setAppointmentDateTime(dto.getAppointmentDateTime());
        savedAppointment.setServiceType(dto.getServiceType());
        savedAppointment.setDescription(dto.getDescription());

        AppointmentResponseDTO responseDTO = new AppointmentResponseDTO();
        responseDTO.setId(appointmentId);
        responseDTO.setAppointmentDateTime(dto.getAppointmentDateTime());
        responseDTO.setServiceType(dto.getServiceType());
        responseDTO.setDescription(dto.getDescription());

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(existingAppointment));
        when(appointmentRepository.save(existingAppointment)).thenReturn(savedAppointment);
        when(appointmentMapper.toDto(savedAppointment)).thenReturn(responseDTO);

        AppointmentResponseDTO result = appointmentService.updateAppointment(appointmentId, dto);

        assertEquals(responseDTO, result);

        verify(appointmentRepository).save(existingAppointment);

    }

    @Test
    void getAppointmentsForTomorrow() {

        LocalDateTime start = LocalDateTime.now().plusDays(1).toLocalDate().atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        List<Appointment> mockAppointments = List.of(new Appointment(), new Appointment());

        when(appointmentRepository.findByAppointmentDateTimeBetween(start, end)).thenReturn(mockAppointments);

        List<Appointment> result = appointmentService.getAppointmentsForTomorrow();

        assertEquals(mockAppointments, result);

        verify(appointmentRepository).findByAppointmentDateTimeBetween(start, end);

    }

    @Test
    void getAppointmentById() {

        Long appointmentId = 1L;
        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        AppointmentResponseDTO dto = new AppointmentResponseDTO();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointmentMapper.toDto(appointment)).thenReturn(dto);

        AppointmentResponseDTO result = appointmentService.getAppointmentById(appointmentId);

        assertEquals(dto, result);
        verify(appointmentRepository).findById(appointmentId);
        verify(appointmentMapper).toDto(appointment);

    }

    @Test
    void getAppointmentById_shouldThrowException_whenAppointmentNotFound() {
        Long appointmentId = 1L;

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class,
                () -> appointmentService.getAppointmentById(appointmentId));

        verify(appointmentRepository).findById(appointmentId);
        verifyNoMoreInteractions(appointmentMapper);
    }

    @Test
    void getAppointmentsByChatId() {

        Long chatId = 123L;

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setTelegramChatId(chatId);

        List<Appointment> mockAppointments = List.of(new Appointment(), new Appointment());

        when(userRepository.findByTelegramChatId(chatId)).thenReturn(Optional.of(mockUser));

        when(appointmentRepository.findByUser(mockUser)).thenReturn(mockAppointments);

        List<Appointment> result = appointmentService.getAppointmentsByChatId(chatId);

        assertEquals(mockAppointments, result);

        verify(userRepository, times(1)).findByTelegramChatId(chatId);
        verify(appointmentRepository, times(1)).findByUser(mockUser);


    }

    @Test
    void getAllUserAppointments() {

        Long userId = 1L;

        User mockUser = new User();
        mockUser.setId(userId);

        Appointment app1 = new Appointment();
        app1.setId(1L);
        Appointment app2 = new Appointment();
        app2.setId(2L);
        List<Appointment> mockAppointments = List.of(app1, app2);
        AppointmentResponseDTO dto1 = new AppointmentResponseDTO();
        AppointmentResponseDTO dto2 = new AppointmentResponseDTO();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(appointmentRepository.findByUser(mockUser)).thenReturn(mockAppointments);

        when(appointmentMapper.toDto(mockAppointments.get(0))).thenReturn(dto1);
        when(appointmentMapper.toDto(mockAppointments.get(1))).thenReturn(dto2);

        List<AppointmentResponseDTO> result = appointmentService.getAllUserAppointments(userId);

        assertEquals(2, result.size());
        assertEquals(dto1.getId(), result.get(0).getId());
        assertEquals(dto2.getId(), result.get(1).getId());

        verify(userRepository, times(1)).findById(userId);
        verify(appointmentRepository, times(1)).findByUser(mockUser);
        verify(appointmentMapper, times(1)).toDto(app1);
        verify(appointmentMapper, times(1)).toDto(app2);
    }

    //TODO исправить ошибки в тесте
    @Test
    void getAllAppointments() {

        Appointment app1 = new Appointment();
        app1.setId(1L);
        Appointment app2 = new Appointment();
        app2.setId(2L);
        List<Appointment> mockAppointments = List.of(app1, app2);
        AppointmentResponseDTO dto1 = new AppointmentResponseDTO();
        AppointmentResponseDTO dto2 = new AppointmentResponseDTO();

        when(appointmentRepository.findAll()).thenReturn(mockAppointments);
        when(appointmentMapper.toDto(mockAppointments.get(0))).thenReturn(dto1);
        when(appointmentMapper.toDto(mockAppointments.get(1))).thenReturn(dto2);

        List<AppointmentResponseDTO> result = appointmentService.getAllAppointments();

        assertEquals(2, result.size());
        assertEquals(dto1.getId(), result.get(0).getId());
        assertEquals(dto2.getId(), result.get(1).getId());

        verify(appointmentRepository, times(1)).findAll();
        verify(appointmentMapper, times(1)).toDto(app1);
        verify(appointmentMapper, times(1)).toDto(app2);

    }

    @Test
    void deleteAppointment() {

        Long appointmentId = 1L;

        appointmentService.deleteAppointment(appointmentId);

        verify(appointmentRepository, times(1)).deleteById(appointmentId);

    }
}