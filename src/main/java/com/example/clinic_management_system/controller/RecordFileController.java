package com.example.clinic_management_system.controller;

import com.example.clinic_management_system.dto.response.ApiResponse;
import com.example.clinic_management_system.dto.response.RecordFileResponse;
import com.example.clinic_management_system.service.RecordFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/record-files")
public class RecordFileController {
    private final RecordFileService recordFileService;

    // Upload file lên hồ sơ bệnh lý
    @PostMapping("")
    @Transactional
    public ResponseEntity<ApiResponse<?>> uploadRecordFile(
            @RequestParam String recordId,
            @RequestPart("files") List<MultipartFile> files
    ) {
        recordFileService.uploadRecordFile(recordId, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder()
                        .status("success")
                        .code(HttpStatus.CREATED.value())
                        .message("Upload files ảnh xét nghiệm thành công")
                .build());
    }

    // Lấy danh sách files trong 1 hồ sơ bệnh lý
    @GetMapping("/{recordId}")
    public ResponseEntity<ApiResponse<List<RecordFileResponse>>> getRecordFiles(@PathVariable String recordId) {
        List<RecordFileResponse> recordFileResponses = recordFileService.getRecordFiles(recordId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<List<RecordFileResponse>>builder()
                        .status("success")
                        .code(HttpStatus.OK.value())
                        .message("Lấy danh sách files upload thành công")
                        .data(recordFileResponses)
                .build());
    }

    // Xóa file
    @DeleteMapping("/{fileId}")
    public ResponseEntity<ApiResponse<?>> deleteRecordFile(@PathVariable String fileId) {
        recordFileService.deleteRecordFile(fileId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                        .status("success")
                        .code(HttpStatus.OK.value())
                        .message("Xóa file thành công")
                .build());
    }

}
