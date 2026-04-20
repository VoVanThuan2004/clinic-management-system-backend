package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.response.RecordFileResponse;
import com.example.clinic_management_system.dto.response.UploadResult;
import com.example.clinic_management_system.entity.MedicalRecord;
import com.example.clinic_management_system.entity.RecordFile;
import com.example.clinic_management_system.exception.BadRequestException;
import com.example.clinic_management_system.exception.ResourceNotFoundException;
import com.example.clinic_management_system.mapper.RecordFileMapper;
import com.example.clinic_management_system.repository.MedicalRecordRepository;
import com.example.clinic_management_system.repository.RecordFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecordFileServiceImpl implements RecordFileService {
    private final RecordFileRepository recordFileRepository;
    private final CloudinaryService cloudinaryService;
    private final MedicalRecordRepository medicalRecordRepository;
    private final RecordFileMapper recordFileMapper;

    @Override
    @Transactional
    public void uploadRecordFile(String recordId, List<MultipartFile> files) {
        // Kiểm tra hồ sơ bệnh lý có tồn tại
        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(recordId);
        if (medicalRecord.isEmpty()) {
            throw new ResourceNotFoundException("Hồ sơ bệnh lý không tồn tại");
        }

        // Validate files
        if (files == null || files.isEmpty()) {
            throw new BadRequestException("Danh sách file không được rỗng");
        }

        if (files.size() > 5) {
            throw new BadRequestException("Tối đa 5 file mỗi lần upload");
        }

        long MAX_SIZE = 5 * 1024 * 1024;
        List<String> allowedTypes = List.of("image/jpeg", "image/png", "image/jpg");

        List<RecordFile> recordFiles = new ArrayList<>();
        for (MultipartFile file : files) {

            if (file.getSize() > MAX_SIZE) {
                throw new BadRequestException("File vượt quá 5MB");
            }

            String contentType = file.getContentType();
            if (contentType == null || !allowedTypes.contains(contentType)) {
                throw new BadRequestException("Chỉ chấp nhận JPG, PNG");
            }

            try {
                BufferedImage image = ImageIO.read(file.getInputStream());
                if (image == null) {
                    throw new BadRequestException("File không phải ảnh hợp lệ");
                }
            } catch (IOException e) {
                throw new BadRequestException("Không đọc được file");
            }

            // Upload lên cloudinary
            UploadResult uploadResult = cloudinaryService.uploadFile(file);

            recordFiles.add(RecordFile.builder()
                            .fileUrl(uploadResult.getSecureUrl())
                            .fileType(contentType)
                            .publicId(uploadResult.getPublicId())
                            .medicalRecord(medicalRecord.get())
                    .build());
        }

        // Lưu danh sách ảnh xuống DB
        recordFileRepository.saveAll(recordFiles);
    }

    @Override
    public List<RecordFileResponse> getRecordFiles(String recordId) {
        // Kiểm tra hồ sơ bệnh lý có tồn tại
        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(recordId);
        if (medicalRecord.isEmpty()) {
            throw new ResourceNotFoundException("Hồ sơ bệnh lý không tồn tại");
        }

        // Query data
        List<RecordFile> recordFiles = recordFileRepository.findAllByRecordId(recordId);

        // Mapping data trả về
        List<RecordFileResponse> recordFileResponses = recordFileMapper.toResponseList(recordFiles);

        return recordFileResponses;
    }

    @Override
    @Transactional
    public void deleteRecordFile(String fileId) {
        // 1. Kiểm tra file có tồn tại
        Optional<RecordFile> recordFile = recordFileRepository.findById(fileId);
        if (recordFile.isEmpty()) {
            throw new ResourceNotFoundException("File không tồn tại");
        }

        // 2. Xóa file ảnh trên cloudinary
        cloudinaryService.deleteFile(recordFile.get().getFileUrl());
        recordFileRepository.delete(recordFile.get());
    }
}
