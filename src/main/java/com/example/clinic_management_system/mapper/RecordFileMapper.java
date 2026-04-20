package com.example.clinic_management_system.mapper;

import com.example.clinic_management_system.dto.response.RecordFileResponse;
import com.example.clinic_management_system.entity.RecordFile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecordFileMapper {

    public RecordFileResponse toResponse(RecordFile recordFile) {
        return RecordFileResponse.builder()
                .fileId(recordFile.getFileId())
                .fileUrl(recordFile.getFileUrl())
                .build();
    }

    public List<RecordFileResponse> toResponseList(List<RecordFile> recordFiles) {
        return recordFiles.stream()
                .map(this::toResponse)
                .toList();
    }
}
