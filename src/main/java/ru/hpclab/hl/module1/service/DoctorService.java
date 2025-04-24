package ru.hpclab.hl.module1.service;

import org.springframework.stereotype.Service;
import ru.hpclab.hl.module1.dto.DoctorDTO;
import ru.hpclab.hl.module1.entity.DoctorEntity;
import ru.hpclab.hl.module1.mapper.DoctorMapper;
import ru.hpclab.hl.module1.repository.AppointmentRepository;
import ru.hpclab.hl.module1.repository.DoctorRepository;
import ru.hpclab.hl.module1.service.statistics.ObservabilityService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final ObservabilityService observabilityService;

    public DoctorService(DoctorRepository doctorRepository,
                         AppointmentRepository appointmentRepository,
                         ObservabilityService observabilityService) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.observabilityService = observabilityService;
    }

    public List<DoctorDTO> getAllDoctors() {
        observabilityService.start("service.doctor.getAll");
        try {
            return doctorRepository.findAll().stream()
                    .map(DoctorMapper::toDTO)
                    .collect(Collectors.toList());
        } finally {
            observabilityService.stop("service.doctor.getAll");
        }
    }

    public DoctorDTO getDoctorById(Long id) {
        observabilityService.start("service.doctor.getById");
        try {
            Optional<DoctorEntity> doctorEntity = doctorRepository.findById(id);
            return doctorEntity.map(DoctorMapper::toDTO).orElse(null);
        } finally {
            observabilityService.stop("service.doctor.getById");
        }
    }

    public DoctorDTO saveDoctor(DoctorDTO doctorDTO) {
        observabilityService.start("service.doctor.save");
        try {
            DoctorEntity entity = DoctorMapper.toEntity(doctorDTO);
            return DoctorMapper.toDTO(doctorRepository.save(entity));
        } finally {
            observabilityService.stop("service.doctor.save");
        }
    }

    public DoctorDTO updateDoctor(Long id, DoctorDTO newDoctorDTO) {
        observabilityService.start("service.doctor.update");
        try {
            return doctorRepository.findById(id)
                    .map(existingDoctor -> {
                        existingDoctor.setFio(newDoctorDTO.getFio());
                        existingDoctor.setSpecialization(newDoctorDTO.getSpecialization());
                        existingDoctor.setWorkSchedule(newDoctorDTO.getWorkSchedule());
                        return DoctorMapper.toDTO(doctorRepository.save(existingDoctor));
                    })
                    .orElse(null);
        } finally {
            observabilityService.stop("service.doctor.update");
        }
    }

    public void deleteDoctor(Long id) {
        observabilityService.start("service.doctor.delete");
        try {
            doctorRepository.deleteById(id);
        } finally {
            observabilityService.stop("service.doctor.delete");
        }
    }
}
