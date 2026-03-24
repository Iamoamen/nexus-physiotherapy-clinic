package com.nexus.clinic.repository;

import com.nexus.clinic.entity.Appointment;
import com.nexus.clinic.entity.AppointmentStatus;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Page<Appointment> findByStatusOrderByCreatedAtDesc(AppointmentStatus status, Pageable pageable);

    Page<Appointment> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.status = :status")
    long countByStatus(AppointmentStatus status);

    List<Appointment> findByPreferredDateAndTherapistId(LocalDate date, Long therapistId);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.createdAt >= :since")
    long countSince(LocalDateTime since);
}
