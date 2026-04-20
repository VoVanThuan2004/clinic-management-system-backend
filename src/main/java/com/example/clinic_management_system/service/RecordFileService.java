package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.response.RecordFileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecordFileService {
    void uploadRecordFile(String recordId, List<MultipartFile> files);

    List<RecordFileResponse> getRecordFiles(String recordId);

    void deleteRecordFile(String fileId);
}
