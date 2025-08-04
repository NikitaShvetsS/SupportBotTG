package org.example.repository;

import org.example.entity.Appointment;
import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByAppointmentDateTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Appointment> findByUser(User user);
    List<Appointment> findAllByAppointmentDateTime(LocalDate appointmentDateTime);
}
