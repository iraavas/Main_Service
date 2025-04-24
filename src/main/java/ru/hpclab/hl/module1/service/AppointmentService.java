package ru.hpclab.hl.module1.service;

import org.springframework.stereotype.Service;
import ru.hpclab.hl.module1.controller.exception.DoctorException;
import ru.hpclab.hl.module1.dto.AppointmentDTO;
import ru.hpclab.hl.module1.entity.AppointmentEntity;
import ru.hpclab.hl.module1.entity.DoctorEntity;
import ru.hpclab.hl.module1.entity.PatientEntity;
import ru.hpclab.hl.module1.mapper.AppointmentMapper;
import ru.hpclab.hl.module1.model.AppointmentStatus;
import ru.hpclab.hl.module1.repository.AppointmentRepository;
import ru.hpclab.hl.module1.repository.DoctorRepository;
import ru.hpclab.hl.module1.repository.PatientRepository;
import ru.hpclab.hl.module1.service.statistics.ObservabilityService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final ObservabilityService observabilityService;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository,
                              ObservabilityService observabilityService) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.observabilityService = observabilityService;
    }

    public List<AppointmentDTO> getAllAppointments() {
        observabilityService.start("service.appointment.getAll");
        try {
            return appointmentRepository.findAll().stream()
                    .map(AppointmentMapper::toDTO)
                    .collect(Collectors.toList());
        } finally {
            observabilityService.stop("service.appointment.getAll");
        }
    }

    public AppointmentDTO getAppointmentById(Long id) {
        observabilityService.start("service.appointment.getById");
        try {
            Optional<AppointmentEntity> appointmentEntity = appointmentRepository.findById(id);
            return appointmentEntity.map(AppointmentMapper::toDTO).orElse(null);
        } finally {
            observabilityService.stop("service.appointment.getById");
        }
    }

    public AppointmentDTO saveAppointment(AppointmentDTO appointmentDTO) {
        observabilityService.start("service.appointment.save");
        try {
            PatientEntity patient = patientRepository.findById(appointmentDTO.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Пациент не найден"));

            DoctorEntity doctor = doctorRepository.findById(appointmentDTO.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("Доктор не найден"));

            Long count = appointmentRepository.countAppointmentsForSpecializationAtTime(
                    appointmentDTO.getSpecialization(), appointmentDTO.getAppointmentDate());

            if (count > 0) {
                throw new DoctorException("Врач с специализацией " + appointmentDTO.getSpecialization() +
                        " уже занят в это время: " + appointmentDTO.getAppointmentDate());
            }

            AppointmentEntity appointmentEntity = AppointmentMapper.toEntity(appointmentDTO, patient, doctor);
            appointmentEntity.setStatus(AppointmentStatus.SCHEDULED);

            return AppointmentMapper.toDTO(appointmentRepository.save(appointmentEntity));
        } finally {
            observabilityService.stop("service.appointment.save");
        }
    }

    public AppointmentDTO updateAppointment(Long id, AppointmentDTO appointmentDTO) {
        observabilityService.start("service.appointment.update");
        try {
            return appointmentRepository.findById(id)
                    .map(existingAppointment -> {
                        PatientEntity patient = patientRepository.findById(appointmentDTO.getPatientId())
                                .orElseThrow(() -> new RuntimeException("Пациент не найден"));

                        DoctorEntity doctor = doctorRepository.findById(appointmentDTO.getDoctorId())
                                .orElseThrow(() -> new RuntimeException("Доктор не найден"));

                        existingAppointment.setPatient(patient);
                        existingAppointment.setDoctor(doctor);
                        existingAppointment.setAppointmentDate(appointmentDTO.getAppointmentDate());
                        existingAppointment.setDiagnosis(appointmentDTO.getDiagnosis());

                        return AppointmentMapper.toDTO(appointmentRepository.save(existingAppointment));
                    })
                    .orElse(null);
        } finally {
            observabilityService.stop("service.appointment.update");
        }
    }

    public void deleteAppointment(Long id) {
        observabilityService.start("service.appointment.delete");
        try {
            appointmentRepository.deleteById(id);
        } finally {
            observabilityService.stop("service.appointment.delete");
        }
    }
}
