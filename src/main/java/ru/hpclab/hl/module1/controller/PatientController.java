package ru.hpclab.hl.module1.controller;

import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.dto.PatientDTO;
import ru.hpclab.hl.module1.service.PatientService;
import ru.hpclab.hl.module1.service.statistics.ObservabilityService;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;
    private final ObservabilityService observabilityService;

    public PatientController(PatientService patientService, ObservabilityService observabilityService) {
        this.patientService = patientService;
        this.observabilityService = observabilityService;
    }

    @GetMapping
    public List<PatientDTO> getAllPatients() {
        observabilityService.start("controller.patients.getAll");
        try {
            return patientService.getAllPatients();
        } finally {
            observabilityService.stop("controller.patients.getAll");
        }
    }

    @GetMapping("/{id}")
    public PatientDTO getPatientById(@PathVariable Long id) {
        observabilityService.start("controller.patients.getById");
        try {
            return patientService.getPatientById(id);
        } finally {
            observabilityService.stop("controller.patients.getById");
        }
    }

    @PostMapping
    public PatientDTO addPatient(@RequestBody PatientDTO patientDTO) {
        observabilityService.start("controller.patients.add");
        try {
            return patientService.savePatient(patientDTO);
        } finally {
            observabilityService.stop("controller.patients.add");
        }
    }

    @PutMapping("/{id}")
    public PatientDTO updatePatient(@PathVariable Long id, @RequestBody PatientDTO patientDTO) {
        observabilityService.start("controller.patients.update");
        try {
            return patientService.updatePatient(id, patientDTO);
        } finally {
            observabilityService.stop("controller.patients.update");
        }
    }

    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        observabilityService.start("controller.patients.delete");
        try {
            patientService.deletePatient(id);
        } finally {
            observabilityService.stop("controller.patients.delete");
        }
    }
}
