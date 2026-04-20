package com.example.clinic_management_system.repository;

import com.example.clinic_management_system.entity.RecordFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecordFileRepository extends JpaRepository<RecordFile, String> {

    @Query("""
        select r
        from RecordFile r
        where r.medicalRecord.medicalRecordId = :recordId
    """)
    List<RecordFile> findAllByRecordId(@Param("recordId") String recordId);
}
