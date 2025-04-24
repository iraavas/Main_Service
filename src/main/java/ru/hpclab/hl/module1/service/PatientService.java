package ru.hpclab.hl.module1.service;

import org.springframework.stereotype.Service;
import ru.hpclab.hl.module1.dto.PatientDTO;
import ru.hpclab.hl.module1.entity.PatientEntity;
import ru.hpclab.hl.module1.mapper.PatientMapper;
import ru.hpclab.hl.module1.repository.PatientRepository;
import ru.hpclab.hl.module1.service.statistics.ObservabilityService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final ObservabilityService observabilityService;

    public PatientService(PatientRepository patientRepository, ObservabilityService observabilityService) {
        this.patientRepository = patientRepository;
        this.observabilityService = observabilityService;
    }

    public List<PatientDTO> getAllPatients() {
        observabilityService.start("service.patient.getAll");
        try {
            return patientRepository.findAll().stream()
                    .map(PatientMapper::toDTO)
                    .collect(Collectors.toList());
        } finally {
            observabilityService.stop("service.patient.getAll");
        }
    }

    public PatientDTO getPatientById(Long id) {
        observabilityService.start("service.patient.getById");
        try {
            Optional<PatientEntity> patientEntity = patientRepository.findById(id);
            return patientEntity.map(PatientMapper::toDTO).orElse(null);
        } finally {
            observabilityService.stop("service.patient.getById");
        }
    }

    public PatientDTO savePatient(PatientDTO patientDTO) {
        observabilityService.start("service.patient.save");
        try {
            PatientEntity entity = PatientMapper.toEntity(patientDTO);
            return PatientMapper.toDTO(patientRepository.save(entity));
        } finally {
            observabilityService.stop("service.patient.save");
        }
    }

    public PatientDTO updatePatient(Long id, PatientDTO newPatientDTO) {
        observabilityService.start("service.patient.update");
        try {
            return patientRepository.findById(id)
                    .map(existingPatient -> {
                        existingPatient.setFio(newPatientDTO.getFio());
                        existingPatient.setDateOfBirth(newPatientDTO.getDateOfBirth());
                        existingPatient.setInsuranceNumber(newPatientDTO.getInsuranceNumber());
                        return PatientMapper.toDTO(patientRepository.save(existingPatient));
                    })
                    .orElse(null);
        } finally {
            observabilityService.stop("service.patient.update");
        }
    }

    public void deletePatient(Long id) {
        observabilityService.start("service.patient.delete");
        try {
            patientRepository.deleteById(id);
        } finally {
            observabilityService.stop("service.patient.delete");
        }
    }
}
