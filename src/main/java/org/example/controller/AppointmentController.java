package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.AppointmentRequestDTO;
import org.example.dto.AppointmentResponseDTO;
import org.example.entity.Appointment;
import org.example.service.AppointmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/add")
    public AppointmentResponseDTO createAppointment(@RequestBody AppointmentRequestDTO dto){
        log.info("Creating new appointment");
        return appointmentService.createAppointment(dto);
    }

    @PutMapping("/updateById")
    public AppointmentResponseDTO updateAppointment(@RequestParam Long id, @RequestBody AppointmentRequestDTO dto){
        log.info("Updating appointment by id");
        return appointmentService.updateAppointment(id, dto);
    }

    @GetMapping("/{chatId}")
    public List<Appointment> getAppointmentsByChatId(@RequestParam Long chatId){
        log.info("Getting appointments by chat id");
        return appointmentService.getAppointmentsByChatId(chatId);
    }
    @GetMapping("/{id}")
    public AppointmentResponseDTO getAppointmentById(@RequestParam Long id){
        log.info("Getting appointment by appointment id");
        return appointmentService.getAppointmentById(id);
    }

    @GetMapping("/all")
    public List<AppointmentResponseDTO> getAllAppointments(){
        log.info("Getting all appointments");
        return appointmentService.getAllAppointments();
    }
    @DeleteMapping("/{id}")
    public void deleteAppointment(@RequestParam Long id){
        log.info("Deleting appointment by id");
        appointmentService.deleteAppointment(id);
    }
    @GetMapping("/allByUserId")
    public List<AppointmentResponseDTO> getAllUserAppointments(@RequestParam Long id){
        log.info("Getting all appointments by user");
        return  appointmentService.getAllUserAppointments(id);
    }
    @GetMapping("/in24hours")
    public List<Appointment> getAppointmentsIn24Hours(){
        log.info("Getting appointments in next 24 hours");
        return appointmentService.getAppointmentsIn24Hours();
    }

    @GetMapping("/tomorrow")
    public List<Appointment> getAppointmentsForTomorrow(){
        log.info("Getting appointments for tomorrow");
        return appointmentService.getAppointmentsForTomorrow();
    }


}
