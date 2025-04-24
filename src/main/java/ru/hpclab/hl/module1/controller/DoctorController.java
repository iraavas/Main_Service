package ru.hpclab.hl.module1.controller;

import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.dto.DoctorDTO;
import ru.hpclab.hl.module1.service.DoctorService;
import ru.hpclab.hl.module1.service.statistics.ObservabilityService;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final ObservabilityService observabilityService;

    public DoctorController(DoctorService doctorService, ObservabilityService observabilityService) {
        this.doctorService = doctorService;
        this.observabilityService = observabilityService;
    }

    @GetMapping
    public List<DoctorDTO> getAllDoctors() {
        observabilityService.start("controller.doctors.getAll");
        try {
            return doctorService.getAllDoctors();
        } finally {
            observabilityService.stop("controller.doctors.getAll");
        }
    }

    @GetMapping("/{id}")
    public DoctorDTO getDoctorById(@PathVariable Long id) {
        observabilityService.start("controller.doctors.getById");
        try {
            return doctorService.getDoctorById(id);
        } finally {
            observabilityService.stop("controller.doctors.getById");
        }
    }

    @PostMapping
    public DoctorDTO addDoctor(@RequestBody DoctorDTO doctorDTO) {
        observabilityService.start("controller.doctors.add");
        try {
            return doctorService.saveDoctor(doctorDTO);
        } finally {
            observabilityService.stop("controller.doctors.add");
        }
    }

    @PutMapping("/{id}")
    public DoctorDTO updateDoctor(@PathVariable Long id, @RequestBody DoctorDTO doctorDTO) {
        observabilityService.start("controller.doctors.update");
        try {
            return doctorService.updateDoctor(id, doctorDTO);
        } finally {
            observabilityService.stop("controller.doctors.update");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        observabilityService.start("controller.doctors.delete");
        try {
            doctorService.deleteDoctor(id);
        } finally {
            observabilityService.stop("controller.doctors.delete");
        }
    }
}
