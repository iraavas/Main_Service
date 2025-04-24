package ru.hpclab.hl.module1.controller;

import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.dto.AppointmentDTO;
import ru.hpclab.hl.module1.service.AppointmentService;
import ru.hpclab.hl.module1.service.statistics.ObservabilityService;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final ObservabilityService observabilityService;

    public AppointmentController(AppointmentService appointmentService, ObservabilityService observabilityService) {
        this.appointmentService = appointmentService;
        this.observabilityService = observabilityService;
    }

    @GetMapping
    public List<AppointmentDTO> getAllAppointments() {
        observabilityService.start("controller.appointments.getAll");
        try {
            return appointmentService.getAllAppointments();
        } finally {
            observabilityService.stop("controller.appointments.getAll");
        }
    }

    @GetMapping("/{id}")
    public AppointmentDTO getAppointmentById(@PathVariable Long id) {
        observabilityService.start("controller.appointments.getById");
        try {
            return appointmentService.getAppointmentById(id);
        } finally {
            observabilityService.stop("controller.appointments.getById");
        }
    }

    @PostMapping
    public AppointmentDTO addAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        observabilityService.start("controller.appointments.add");
        try {
            return appointmentService.saveAppointment(appointmentDTO);
        } finally {
            observabilityService.stop("controller.appointments.add");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteAppointment(@PathVariable Long id) {
        observabilityService.start("controller.appointments.delete");
        try {
            appointmentService.deleteAppointment(id);
        } finally {
            observabilityService.stop("controller.appointments.delete");
        }
    }

    @PutMapping("/{id}")
    public AppointmentDTO updateAppointment(@PathVariable Long id, @RequestBody AppointmentDTO appointmentDTO) {
        observabilityService.start("controller.appointments.update");
        try {
            return appointmentService.updateAppointment(id, appointmentDTO);
        } finally {
            observabilityService.stop("controller.appointments.update");
        }
    }
}
